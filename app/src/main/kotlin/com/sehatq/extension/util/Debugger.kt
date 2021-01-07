package com.sehatq.extension.util

import com.sehatq.test.util.Debugger

/**
 * Allows quick logging with the message String.
 *
 * ```
 * Debugger.log("Message here!")
 * ```
 */
fun Debugger.log(message: String?) {
    log(message = message)
}
