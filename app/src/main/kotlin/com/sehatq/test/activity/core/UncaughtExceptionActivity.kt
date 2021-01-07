package com.sehatq.test.activity.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.sehatq.test.BuildConfig
import com.sehatq.test.R
import com.sehatq.test.util.Config
import com.sehatq.test.util.DateTimeHelper
import kotlinx.android.synthetic.main.activity_uncaught_exception.*
import java.util.*

/**
 * A dialog styled activity to be shown when an uncaught exception occurs.
 */
class UncaughtExceptionActivity : AppCompatActivity(), View.OnClickListener {

    private var throwableStack: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uncaught_exception)
        initExtras()
        initView()
        initException()
    }

    private fun initExtras() {
        throwableStack = CustomActivityOnCrash.getStackTraceFromIntent(intent)
    }

    private fun initView() {
        btn_restart_app.setOnClickListener(this)
        txt_toggle_stacktrace.setOnClickListener(this)
        img_share.setOnClickListener(this)

        if (BuildConfig.DEBUG) {
            // Display / hide stacktrace for debugging builds
            layout_debug.visibility = View.VISIBLE
        } else {
            // Set additional rule for release builds' layout
            val lp = layout_error_generic.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initException() {
        // The error message
        txt_error.text = resources.getString(R.string.error_fatal_restarting)

        // Add the app name, version name, code, and build time to the stacktrace
        var stacktrace = resources.getString(R.string.app_name) + ", v" +
                Config.versionName + ", build " +
                Config.versionCode + " - " +
                Config.getSimpleBuildTime() + "\n\n"

        // The actual stacktrace
        stacktrace += throwableStack

        // Set the stacktrace text
        txt_stacktrace.text = stacktrace
    }

    override fun onClick(view: View) {
        when (view) {
            btn_restart_app -> restartApplication()
            txt_toggle_stacktrace -> when (scroll_stacktrace.visibility) {
                View.VISIBLE -> {
                    scroll_stacktrace.visibility = View.GONE
                    img_share.visibility = View.GONE
                }
                else -> {
                    scroll_stacktrace.visibility = View.VISIBLE
                    img_share.visibility = View.VISIBLE
                }
            }
            img_share -> shareErrorStack()
        }
    }

    /** Generate a share intent to share the error stacktrace  */
    private fun shareErrorStack() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val shareBody = txt_stacktrace.text.toString()
        val currentDate = DateTimeHelper.getStringFromCalendar(Calendar.getInstance(), "yyyy MMMM dd, HH:mm:ss")

        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "[" + resources.getString(R.string.app_name) + "] Bug Report " + currentDate)
            putExtra(Intent.EXTRA_TEXT, shareBody)

            // Show intent chooser
            startActivity(Intent.createChooser(this, resources.getString(R.string.action_share)))
        }
    }

    /** Restart the application by re-launching the main launcher activity class  */
    private fun restartApplication() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
