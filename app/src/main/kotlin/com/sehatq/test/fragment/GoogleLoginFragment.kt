package com.sehatq.test.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.facebook.FacebookSdk
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sehatq.test.R
import com.sehatq.test.activity.DashboardActivity
import com.sehatq.test.fragment.core.CoreFragment
import com.sehatq.test.util.Common
import kotlinx.android.synthetic.main.fragment_google_login.sign_in_button
import java.io.IOException

class GoogleLoginFragment : CoreFragment(), View.OnClickListener {

  private val RC_SIGN_IN = 234

  //creating a GoogleSignInClient object
  var mGoogleSignInClient: GoogleSignInClient? = null

  //And also a Firebase Auth object
  var mAuth: FirebaseAuth? = null

  override val viewRes = R.layout.fragment_google_login

  override fun onClick(v: View?) {
    if (v === sign_in_button) {
      //getting the google signin intent
      val signInIntent = mGoogleSignInClient!!.signInIntent

      //starting the activity for result
      startActivityForResult(signInIntent, RC_SIGN_IN)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    //Then we will get the GoogleSignInClient object from GoogleSignIn class
    mGoogleSignInClient = GoogleSignIn.getClient(activity!!, gso)

    //first we intialized the FirebaseAuth object
    mAuth = FirebaseAuth.getInstance()

    sign_in_button.setOnClickListener(this)
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    //if the requestCode is the Google Sign In code that we defined at starting
    if (requestCode == RC_SIGN_IN) {

      //Getting the GoogleSignIn Task
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      try {
        //Google Sign In was successful, authenticate with Firebase
        val account = task.getResult(ApiException::class.java)
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        val acct = result!!.signInAccount
        var token: String? = ""
        if (acct != null) {
          token = acct.idToken
          DashboardActivity.launchIntent(context!!)
        }
        val runnable = Runnable {
          try {
            val scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE
            var accessToken: String? = null
            if (acct != null) {
              accessToken = GoogleAuthUtil.getToken(
                  FacebookSdk.getApplicationContext(), acct.account, scope, Bundle()
              )
            }
            firebaseAuthWithGoogle(account, accessToken)
            //                            Log.d(TAG, "accessToken:"+accessToken); //accessToken:ya29.Gl...
          } catch (e: IOException) {
            e.printStackTrace()
          } catch (e: GoogleAuthException) {
            e.printStackTrace()
          }
        }
        AsyncTask.execute(runnable)

        //authenticating with firebase
      } catch (e: ApiException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun firebaseAuthWithGoogle(
    acct: GoogleSignInAccount?,
    token: String?
  ) {
    //getting the auth credential
    val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
    val newToken = acct.idToken

    //Now using firebase we are signing in the user here
    mAuth!!.signInWithCredential(credential)
        .addOnCompleteListener(activity!!) { task ->
          if (task.isSuccessful) {
            val user = mAuth!!.currentUser
          } else {
            // If sign in fails, display a message to the user.
            Common.showToast("Authentication failed.")
          }
        }
  }
}