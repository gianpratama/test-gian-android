package com.sehatq.test.api

import com.sehatq.test.api.callback.AppCallback
import com.sehatq.test.api.callback.core.CoreCallback
import com.sehatq.test.api.util.OnAPIListener
import com.sehatq.test.model.core.AppResponse
import com.sehatq.test.model.remote.ProductResponse
import com.sehatq.test.util.JNIUtil
import retrofit2.Call
import retrofit2.Callback

class APICaller<RESPONSE : AppResponse> {

    /** The access token for API calls */
    private var accessToken: String? = null

    /** The api key for API calls */
    private var apiKey: String? = ""

    /**
     * Determines if this API call uses a specific access token,
     * instead of using the access token from saved profile.
     */
    private var isUsingCustomToken = false

    /** The listener object for API calls */
    internal var listener: OnAPIListener<RESPONSE>? = null

    /** The [Call] object */
    private var call: Call<*>? = null

    /** The [CoreCallback] */
    private var callback: Callback<*>? = null

    /**
     * Sets the access token.
     * When called, this will override the default token loaded from saved data.
     * @param accessToken the access token
     * @return this
     */
    fun withAccessToken(accessToken: String): APICaller<RESPONSE> {
        this.accessToken = accessToken
        isUsingCustomToken = true
        return this
    }

    /**
     * Sets the API key.
     * When called, this will override the default API key.
     * @param apiKey the API key
     * @return this
     */
    fun withApiKey(apiKey: String): APICaller<RESPONSE> {
        this.apiKey = apiKey
        return this
    }

    /**
     * Adds a listener for the helper class.
     * @param listener the [OnAPIListener] object
     * @return this
     */
    fun withListener(listener: OnAPIListener<RESPONSE>): APICaller<RESPONSE> {
        this.listener = listener
        return this
    }

    /**
     * Checks if the call has been executed / enqueued.
     * Be careful, a call that has been executed will return true even on failure.
     */
    val isExecuted: Boolean?
        get() = call?.isExecuted

    /** Cancels any ongoing call */
    fun cancel() {
        call?.cancel()
    }

    /** Clears the current callback and call */
    fun clear() {
        callback = null
        call = null
    }

    /**
     * Calls the product list API.
     * Obtain the product list.
     */
//    @Suppress("UNCHECKED_CAST")
//    fun getProductList() {
//        callback = AppCallback(listener as OnAPIListener<ProductResponse>)
//        call = APIService.apiInterface.getProductList()
//        (call as Call<ProductResponse>).enqueue(callback as AppCallback<ProductResponse>)
//    }
}
