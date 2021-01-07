package com.sehatq.test.model.remote

import com.google.gson.annotations.SerializedName

class ProductResponse {
    @SerializedName("data")
    var dataList: ProductData? = null
}