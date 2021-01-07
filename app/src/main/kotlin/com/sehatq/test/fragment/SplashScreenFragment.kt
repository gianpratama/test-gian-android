package com.sehatq.test.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sehatq.test.R
import com.sehatq.test.activity.DashboardActivity
import com.sehatq.test.activity.LoginActivity
import com.sehatq.test.fragment.core.CoreFragment
import com.sehatq.test.util.Config

class SplashScreenFragment : CoreFragment() {

    override val viewRes = R.layout.fragment_splash_screen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler()
        handler.postDelayed({
            onSplashScreenReady()
        }, 5000)
    }

    /** Called when app is ready to continue with its splash screen  */
    private fun onSplashScreenReady() {
        // Check for storage space before continuing
        // Low-end devices should have at least 64MB of free space, otherwise 128MB
        val storageThreshold = if (Config.isLowRamDevice()) 64 else 128
        if (Config.freeInternalSpaceInMb < storageThreshold)
            showStorageSpaceWarningDialog()
        else
            initSplashScreen()
    }

    private fun initSplashScreen() {
        activity?.let {
            LoginActivity.launchIntent(it)
            it.finish()
        }
    }

    /**
     * Display an [AlertDialog].
     * The dialog contains a warning message about storage space.
     * When the dialog is dismissed, the app will continue.
     */
    private fun showStorageSpaceWarningDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AppTheme_Dialog_Alert)
            builder.setTitle(resources.getString(R.string.info_warning))
            builder.setMessage(resources.getString(R.string.warning_storage_space))
            builder.setPositiveButton(resources.getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
            builder.setOnDismissListener { initSplashScreen() }
            builder.show()
        }
    }
}
