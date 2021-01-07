package com.sehatq.test.api.callback

import com.sehatq.test.api.util.OnAPIListener
import com.sehatq.test.api.callback.core.CoreCallback
import com.sehatq.test.model.core.AppResponse

import retrofit2.Response

/**
 * The default callback for the application.
 */
class AppCallback<RESPONSE: AppResponse>(listener: OnAPIListener<RESPONSE>?) : CoreCallback<RESPONSE>(listener) {

    override fun onSuccess(response: Response<RESPONSE>) {
        handleSuccess(response)
    }

    override fun onFailure(responseCode: Int, errorMessage: String) {
        handleFailure(responseCode, errorMessage)
    }
}
