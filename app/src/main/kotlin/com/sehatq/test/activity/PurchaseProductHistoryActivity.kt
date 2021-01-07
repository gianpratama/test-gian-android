package com.sehatq.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sehatq.test.R
import com.sehatq.test.activity.core.CoreActivity
import kotlinx.android.synthetic.main.include_toolbar_search.btn_back
import kotlinx.android.synthetic.main.include_toolbar_search.layout_search_product

class PurchaseProductHistoryActivity : CoreActivity(){

    override val viewRes = R.layout.activity_purchased_history_product

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, PurchaseProductHistoryActivity::class.java)

            context.startActivity(intent)
        }
    }
}
