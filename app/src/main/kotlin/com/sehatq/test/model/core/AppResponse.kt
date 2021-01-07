package com.sehatq.test.model.core

import com.sehatq.test.App
import com.sehatq.test.R
import com.sehatq.test.util.Debugger
import com.sehatq.test.util.ValidationHelper
import com.google.gson.annotations.SerializedName
import com.sehatq.test.model.remote.ProductResponse

import org.json.JSONException
import org.json.JSONObject

/**
 * The base response object that the app will receive.
 */
open class AppResponse() {

    @SerializedName("meta")
    var meta: Meta? = null

    /** Constructor of this class  */
    constructor(response: String) : this() {
        // Check if the response is a JSON before confirming it as a success
        var isValid = true
        if (ValidationHelper.isValidJson(response)) {
            try {
                val json = JSONObject(response)

                // Parse the metadata
                val metaJson = json.optJSONObject("meta")
                if (metaJson != null) {
                    meta = Meta()

                    // The response message
                    meta!!.message = metaJson.optString("message")

                    // The pagination metadata
                    val pagingJson = metaJson.optJSONObject("paging")
                    if (pagingJson != null) {
                        val paging = Paging()
                        paging.total = pagingJson.optInt("total")
                        paging.offset = pagingJson.optInt("offset")
                        paging.limit = pagingJson.optInt("limit")
                        meta!!.paging = paging
                    }
                } else {
                    // If the response doesn't have any metadata, treat it as invalid
                    isValid = false
                }
            } catch (e: JSONException) {
                Debugger.logException(e)
                isValid = false
            }
        } else {
            isValid = false
        }

        if (!isValid) {
            meta = Meta()
            meta!!.message = App.context.resources.getString(R.string.error_json)
        }
    }
}
