package com.sehatq.test.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.sehatq.test.App
import com.sehatq.test.R
import com.sehatq.test.view.OnProgressBackPressed
import com.sehatq.test.view.ProgressDialog

/**
 * A class that handles basic universal methods.
 */
object Common {

    /** The loading progress dialog object */
    @SuppressLint("StaticFieldLeak")
    var progressDialog: ProgressDialog? = null

    /**
     * Shows a loading progress dialog.
     * @param context the context
     * @param stringRes the dialog message string resource id
     * @param onBackPress the back button press listener when loading is shown
     */
    fun showProgressDialog(context: Context, stringRes: Int = -1, onBackPress: OnProgressBackPressed? = null) {
        dismissProgressDialog()
        progressDialog = ProgressDialog(context)
        progressDialog!!.setText(stringRes)
        progressDialog!!.onProgressBackPressed = onBackPress
        if (context is Activity && !context.isFinishing) progressDialog!!.show()
    }

    /** Hides the currently shown loading progress dialog */
    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    /**
     * Sets the progress dialog progress in percent.
     * @param progress The loading progress in percent
     */
    fun setProgressDialogProgress(progress: Int) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setProgress(progress)
            progressDialog!!.setText(progress.toString() + "%")
        }
    }

    /**
     * Sets the progress dialog progress indeterminate state.
     * @param isIndeterminate Determines if progress dialog is indeterminate
     */
    fun setProgressDialogIndeterminate(isIndeterminate: Boolean) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setIndeterminate(isIndeterminate)
        }
    }

    /**
     * Sets the progress dialog message.
     * @param message The dialog message string
     */
    fun setProgressDialogText(message: String) {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.setText(message)
        }
    }

    /**
     * Display a simple [Toast].
     * @param stringRes The message string resource id
     */
    fun showToast(stringRes: Int) {
        showToast(App.context.getString(stringRes))
    }

    /**
     * Display a simple [Toast].
     * @param message The message string
     */
    fun showToast(message: String) {
        Toast.makeText(App.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Display a simple [AlertDialog] with a simple OK button.
     * If the dismiss listener is specified, the dialog becomes uncancellable
     * @param context The context
     * @param title The title string
     * @param message The message string
     * @param dismissListener The dismiss listener
     */
    fun showMessageDialog(
            context: Context,
            title: String?,
            message: String?,
            dismissListener: DialogInterface.OnDismissListener? = null) {
        val builder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
        dialog.show()
    }
}
