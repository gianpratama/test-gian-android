package com.sehatq.test.model.core

import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("paging")
    var paging: Paging? = null

    @SerializedName("code")
    var code: Int? = null
}
