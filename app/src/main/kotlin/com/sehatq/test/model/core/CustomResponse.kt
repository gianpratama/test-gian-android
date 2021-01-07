package com.sehatq.test.model.core

import com.google.gson.annotations.SerializedName

open class CustomResponse<RESPONSE> {
  @SerializedName("meta")
  var meta: Meta? = null

  @SerializedName("data")
  var data: RESPONSE? = null
}