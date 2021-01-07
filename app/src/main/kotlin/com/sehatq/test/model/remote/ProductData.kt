package com.sehatq.test.model.remote

import com.sehatq.test.model.core.AppResponse
import com.google.gson.annotations.SerializedName

class ProductData {
    @SerializedName("category")
    var categoryProductList: ArrayList<CategoryProduct>? = null

    @SerializedName("productPromo")
    var productPromoList: ArrayList<ProductPromo>? = null
}