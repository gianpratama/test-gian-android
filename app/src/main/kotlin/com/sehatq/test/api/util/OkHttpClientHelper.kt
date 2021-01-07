package com.sehatq.test.api.util

import com.sehatq.test.App
import com.sehatq.test.BuildConfig
import com.ashokvarma.gander.GanderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientHelper {

    /**
     * Initialize the [OkHttpClient] for retrofit.
     *
     * Server uses SSL, we need to install the certificate on the [OkHttpClient].
     * Debug and mock builds doesn't have valid certificates, so we'll allow all connections there.
     *
     * To whitelist specific domains to enable non-HTTPS connections,
     * check the **res/xml/network_security_config.xml** file.
     * @see <a href="https://developer.android.com/training/articles/security-config">link</a>
     */
    fun initOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        // TODO: Configure SSL pinning as needed
//        val certificatePinner = if (BuildConfig.FLAVOR.equals(JNIUtil.API_PRODUCTION, ignoreCase = true)) {
//            CertificatePinner.Builder()
//                    .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
//                    .add("*.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
//                    .build()
//        } else {
//            CertificatePinner.Builder()
//                    .add("test.example.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
//                    .add("*.example.com", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
//                    .build()
//        }
//        okHttpClientBuilder.certificatePinner(certificatePinner)

        // Add logging for debug builds
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)
        }

        // Add Gander interceptor
        okHttpClientBuilder.addInterceptor(GanderInterceptor(App.context)
                .showNotification(true))

        // Set timeout duration
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }
}
