package com.sehatq.test.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import com.sehatq.test.App
import com.sehatq.test.R
import kotlinx.android.synthetic.main.dialog_progress.view.*

/**
 * A customizable loading dialog.
 *
 * For global usage (that overrides onBackPressed() to do nothing),
 * use [com.sehatq.test.util.Common.showProgressDialog].
 *
 * For customized usage that can handle back press,
 * use [com.sehatq.test.util.Common.showProgressDialog] and pass your back press listener}.
 */
class ProgressDialog(context: Context) {

    /** The dialog */
    private val dialog: Dialog

    /** The view binding */
    private var view: View? = null

    /** The custom back press listener when progress dialog is showing */
    var onProgressBackPressed: OnProgressBackPressed? = null

    init {
        dialog = object : Dialog(context, R.style.AppTheme_Dialog_Transparent) {
            override fun onBackPressed() {
                onProgressBackPressed?.invoke()
            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        @SuppressLint("InflateParams")
        view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null).apply {
            dialog.setContentView(this)
        }
    }

    /**
     * Sets the progress dialog's progress bar indeterminate state.
     * @param isIndeterminate true to make progress bar indeterminate
     */
    fun setIndeterminate(isIndeterminate: Boolean) {
        view?.progress_bar?.isIndeterminate = isIndeterminate
    }

    /**
     * Sets the current progress of the progress dialog.
     * @param percent the progress percentage (0-100)
     */
    fun setProgress(percent: Int) {
        if (view?.progress_bar?.isIndeterminate!!) view?.progress_bar?.isIndeterminate = false
        view?.progress_bar?.progress = percent
    }

    /**
     * Sets the text for the progress dialog.
     * @param stringRes the string resource ID
     */
    fun setText(stringRes: Int) {
        try {
            val text = App.context.resources.getString(stringRes)
            setText(text)
        } catch (e: Resources.NotFoundException) {
            setText(null)
        }
    }

    /**
     * Sets the text for the progress dialog.
     * @param message the string message
     */
    fun setText(message: String?) {
        view?.txt_loading?.text = message
        when {
            message.isNullOrEmpty() -> view?.txt_loading?.visibility = View.GONE
            else -> view?.txt_loading?.visibility = View.VISIBLE
        }
    }

    /** Show the progress dialog */
    fun show() {
        dialog.show()
    }

    /** Dismisses the progress dialog */
    fun dismiss() {
        dialog.dismiss()
    }

    /** Determines if the progress dialog is currently showing */
    val isShowing: Boolean
        get() = dialog.isShowing

}

typealias OnProgressBackPressed = (() -> Unit)
