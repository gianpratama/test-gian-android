package com.sehatq.test.api.util

import com.sehatq.test.App
import com.sehatq.test.BuildConfig
import com.sehatq.test.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

abstract class CustomObserver<RESPONSE : Any> : Observer<RESPONSE> {

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(response: RESPONSE) {

        try {
            if (response != 0) {
                onSuccess(response)
            } else {
                onFailure(R.string.error_unknown)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace().toString() + ""
            else App.context.getString(R.string.error_unknown)

            onFailure(R.string.error_unknown)
        }
    }

    override fun onError(throwable: Throwable) {
        val message: String =
            if (throwable is UnknownHostException || throwable is SSLException || throwable is ConnectException) {
                App.context.getString(R.string.error_conn_generic)
            } else if (throwable is SocketTimeoutException) {
                App.context.getString(R.string.error_conn_time_out)
            } else {
                if (BuildConfig.DEBUG) throwable.printStackTrace().toString() + ""
                else App.context.getString(R.string.error_unknown)
            }

        onFailure(message)
    }

    /** Called when the obtained response is successful  */
    abstract fun onSuccess(response: RESPONSE)

    /** Called when the obtained response is a failure  */
    abstract fun onFailure(error: Any)
}