package com.sehatq.test.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.sehatq.test.R
import com.sehatq.test.activity.DashboardActivity
import com.sehatq.test.fragment.core.CoreFragment
//import kotlinx.android.synthetic.main.fragment_fb_login.btn_fb_login
import kotlinx.android.synthetic.main.fragment_fb_login.btn_main_fb_login
import kotlinx.android.synthetic.main.fragment_fb_login.login_button
import mehdi.sakout.fancybuttons.FancyButton
import org.json.JSONException

class FacebookLoginFragment : Fragment(), View.OnClickListener {

  private var mListener: FbLoginListener? = null

  private var mCallbackManager: CallbackManager? = null

  private var btnFancyLoginFacebook: FancyButton? = null
  private var btnLoginFacebook: LoginButton? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_fb_login, container, false)

    btnLoginFacebook = view.findViewById(R.id.login_button)
    btnFancyLoginFacebook = view.findViewById(R.id.btn_main_fb_login)

    btnLoginFacebook!!.setOnClickListener(this)
    btnFancyLoginFacebook!!.setOnClickListener(this)

    intFacebookLogin()

    return view
  }

  private fun intFacebookLogin() {
    mCallbackManager = Factory.create()
    btnLoginFacebook?.fragment = this
    btnLoginFacebook?.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
      override fun onSuccess(loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { me, response ->
          try {
            if (response.error == null) {
              if (me != null) {
                DashboardActivity.launchIntent(context!!)
              }
            }
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,first_name,last_name,email,picture")
        request.parameters = parameters
        request.executeAsync()
      }

      override fun onCancel() {}
      override fun onError(error: FacebookException) {
      }
    })
  }

  override fun onClick(v: View?) {
    if (v == btn_main_fb_login) {
      btnLoginFacebook?.setReadPermissions("public_profile", "email")
      btnLoginFacebook?.performClick()
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
  }


  /**
   * Set callback listener ketika login menggunakan facebook sukses atau failure.
   *
   * @param listener callback listener
   */
  fun setListener(listener: FbLoginListener) {
    mListener = listener
  }

  /**
   * Callback listener setelah facebook login di eksekusi
   */
  interface FbLoginListener {
    fun onFbLoginSuccess()
    fun onFbLoginFailure(message: String?)
  }
}