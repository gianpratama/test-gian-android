package com.sehatq.extension.api

import com.sehatq.test.api.APICaller
import com.sehatq.test.api.util.OnAPIListener
import com.sehatq.test.model.core.AppError
import com.sehatq.test.model.core.AppResponse

/** Allows quick listener setup */
fun <RESPONSE : AppResponse> APICaller<RESPONSE>.withListener(onApiSuccess: ((response: RESPONSE) -> Unit)?, onApiFailure: ((error: AppError) -> Unit)?): APICaller<RESPONSE> {
    listener = object : OnAPIListener<RESPONSE> {
        override fun onApiSuccess(response: RESPONSE) {
            onApiSuccess?.invoke(response)
        }

        override fun onApiFailure(error: AppError) {
            onApiFailure?.invoke(error)
        }
    }
    return this
}
