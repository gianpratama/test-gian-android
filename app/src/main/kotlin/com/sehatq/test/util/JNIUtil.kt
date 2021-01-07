package com.sehatq.test.util

/**
 * An object for accessing endpoint constants in this app.
 */
object JNIUtil {

    init {
        System.loadLibrary("constant")
    }

    /** @return the web url */
    val webUrl: String
        get() = getWebUrlImpl()

    /** @return the API url */
    val apiUrl: String
        get() = getApiUrlImpl()

    /** @return the API key */
    val apiKey: String
        get() = getApiKeyImpl()

    private external fun getWebUrlImpl(): String
    private external fun getApiUrlImpl(): String
    private external fun getApiKeyImpl(): String
}
