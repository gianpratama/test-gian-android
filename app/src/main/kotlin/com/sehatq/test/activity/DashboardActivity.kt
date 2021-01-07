package com.sehatq.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sehatq.test.R
import com.sehatq.test.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_dashboard_product.nav_view

class DashboardActivity : CoreActivity() {

    override val viewRes = R.layout.activity_dashboard_product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.nav_host_fragment)

        nav_view.itemIconTintList = null
        nav_view.setupWithNavController(navController)
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
        }
    }
}