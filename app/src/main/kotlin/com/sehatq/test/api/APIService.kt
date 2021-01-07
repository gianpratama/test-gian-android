package com.sehatq.test.api

import com.sehatq.test.api.util.OkHttpClientHelper
import com.sehatq.test.model.core.CustomResponse
import com.sehatq.test.model.remote.ProductData
import com.sehatq.test.util.JNIUtil
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * The API interface service.
 */
object APIService {

    /** The [APIInterface] object */
    val apiInterface: APIInterface by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(JNIUtil.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClientHelper().initOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        retrofit.create(APIInterface::class.java)
    }

    /** The interface for retrofit's API calls */
    interface APIInterface {

        /** Obtain the product list */
        @GET("home")
//        fun getProductList()
//                : Call<ArrayList<CoreResponse<ProductResponse>>>

        fun getProductList(): Observable<ArrayList<CustomResponse<ProductData>>>

    }
}
