package com.sehatq.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sehatq.test.R
import com.sehatq.test.R.id
import com.sehatq.test.activity.core.CoreActivity
import com.sehatq.test.fragment.FacebookLoginFragment
import com.sehatq.test.fragment.FacebookLoginFragment.FbLoginListener
import com.sehatq.test.fragment.GoogleLoginFragment
import kotlinx.android.synthetic.main.fargment_login.btn_login
import kotlinx.android.synthetic.main.include_toolbar_search.btn_back
import kotlinx.android.synthetic.main.include_toolbar_search.layout_search_product

class SearchProductActivity : CoreActivity(){

    override val viewRes = R.layout.activity_search_product

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SearchProductActivity::class.java)

            context.startActivity(intent)
        }
    }
}
