package com.sehatq.test.util

import android.util.Log
import com.sehatq.test.BuildConfig
//import com.crashlytics.android.Crashlytics

/**
 * A debugger helper class.
 * By default, debugging functions will only be enabled for debuggable application.
 */
object Debugger {

    /** Determines if the debugger should be enabled by force */
    private var isForcedDebuggerEnabled = false

    /** Enables the debugger, even for non-debuggable apps */
    fun enableForcedDebugger() {
        isForcedDebuggerEnabled = true
    }

    /** Disables the forced enabled debugger */
    fun disableForcedDebugger() {
        isForcedDebuggerEnabled = false
    }

    /** @return true if debugging function should be called */
    private fun isDebuggable(): Boolean {
        return BuildConfig.DEBUG || isForcedDebuggerEnabled
    }

    /**
     * Prints a [Log] message.
     * Log messages printed via this method will only show up if it's debuggable.
     * @param type The specified log type, may be [Log.DEBUG], [Log.INFO], and other log types
     * @param tag The log tag to print
     * @param message The log message to print
     */
    fun log(type: Int = Log.DEBUG, tag: String? = "BaseProject", message: String?) {
        if (isDebuggable()) {
            var logMessage = message
            if (logMessage.isNullOrEmpty()) logMessage = "Message is null, what exactly do you want to print?"
            Log.println(type, tag, logMessage)
        }
    }

    /**
     * Submits an exception to Crashlytics, and prints its stack trace.
     * Stack traces printed via this method will only show up if it's debuggable.
     * @param throwable the throwable
     */
    fun logException(throwable: Throwable) {
//        Crashlytics.logException(throwable)
        if (isDebuggable()) throwable.printStackTrace()
    }
}
