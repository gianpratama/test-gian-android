package com.sehatq.test.model.remote

import com.sehatq.test.model.core.AppResponse
import com.google.gson.annotations.SerializedName

class ProductSampleResponse : AppResponse() {
    var dataSampleList: ArrayList<ProductResponse>? = null
}