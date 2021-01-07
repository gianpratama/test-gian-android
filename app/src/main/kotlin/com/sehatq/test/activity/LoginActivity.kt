package com.sehatq.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import com.sehatq.test.R
import com.sehatq.test.R.id
import com.sehatq.test.R.string
import com.sehatq.test.activity.core.CoreActivity
import com.sehatq.test.fragment.FacebookLoginFragment
import com.sehatq.test.fragment.FacebookLoginFragment.FbLoginListener
import com.sehatq.test.fragment.GoogleLoginFragment
import com.sehatq.test.util.Common
import com.sehatq.test.util.Constant
import com.sehatq.test.util.ValidationHelper
import kotlinx.android.synthetic.main.fargment_login.btn_login
import kotlinx.android.synthetic.main.fargment_login.checkbox_remember
import kotlinx.android.synthetic.main.fargment_login.ed_password
import kotlinx.android.synthetic.main.fargment_login.ed_username
import kotlinx.android.synthetic.main.fargment_login.layout_password
import kotlinx.android.synthetic.main.fargment_login.layout_user_name

class LoginActivity : CoreActivity() {

    override val viewRes = R.layout.fargment_login

//    private val fragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Hawk.deleteAll()
        
        initGoogleLogin()
        initFacebookLogin()

        isCheckUserName()

        checkbox_remember.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val userName = ed_username.text.toString()
                Hawk.put((Constant.IS_USER_NAME), userName)
            } else {
                Hawk.delete(Constant.IS_USER_NAME)
            }
        }

        btn_login.setOnClickListener {
            if (isDataValid) DashboardActivity.launchIntent(this)
        }
    }

    override fun onResume() {
        super.onResume()
        isCheckUserName()
    }

    private fun initFacebookLogin() {
        val fm = supportFragmentManager
        val prev = fm.findFragmentByTag("fblogin")
        if (prev == null) {
            val fragment = FacebookLoginFragment()
            fragment.setListener(object : FbLoginListener {
                override fun onFbLoginSuccess() {
                }

                override fun onFbLoginFailure(message: String?) {

                }
            })

            // commit transaction
            fm.beginTransaction()
                .replace(id.frag_facebook_login_container, fragment, "fblogin")
                .commit()
        }
    }

    private fun initGoogleLogin() {
        val fm = supportFragmentManager
        val prev = fm.findFragmentByTag("fblogin")
        if (prev == null) {
            val fragment = GoogleLoginFragment()

            // commit transaction
            fm.beginTransaction()
                .replace(id.frag_google_login_container, fragment, "fblogin")
                .commit()
        }
    }

    private fun isCheckUserName() {
        if (Hawk.contains(Constant.IS_USER_NAME)) {
            val userName: String = Hawk.get(Constant.IS_USER_NAME)
            ed_username.setText(userName)
        } else {
            ed_username.setText("")
            ed_password.setText("")
        }
    }

    /** Validate data input **/
    private val isDataValid: Boolean
        get() {
            var isValid = true
            clearEditTextError()

            if (ValidationHelper.isEmpty(ed_username)) {
                ed_username.requestFocus()
                layout_user_name.error = resources.getString(R.string.error_input_username)
                isValid = false
            }

            if (ValidationHelper.isEmpty(ed_password)) {
                if (isValid) ed_username.requestFocus()
                layout_password.error = resources.getString(R.string.error_input_password)
                isValid = false
            }


            return isValid
        }

    /** Clears all [com.google.android.material.textfield.TextInputLayout] on the activity from the error  */
    private fun clearEditTextError() {
        layout_user_name.isErrorEnabled = false
        layout_password.isErrorEnabled = false
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)

            context.startActivity(intent)
        }
    }
}
