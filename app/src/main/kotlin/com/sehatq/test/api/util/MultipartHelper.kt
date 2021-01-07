package com.sehatq.test.api.util

import android.content.Context
import android.net.Uri
import com.sehatq.test.BuildConfig
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection

class MultipartHelper {

    /**
     * Generates the file multipart body.
     *
     * Note: Debug build may call the listener progress twice, it's a bug from the HTTP logging.
     * Release builds or builds without HTTP debugging will only show the progress once.
     * @return The request body
     */
    fun generatePart(
            context: Context,
            key: String,
            uri: Uri,
            onUploadProgressListener: ProgressRequestBody.OnUploadProgressListener? = null)
            : MultipartBody.Part {

        // Get the actual file by uri
        val file = File(uri.path!!)

        // Get the mime type
        var mimeType = context.contentResolver.getType(uri)
        // Sometimes it still return a null
        if (mimeType == null) mimeType = URLConnection.guessContentTypeFromName(uri.path)

        // Create RequestBody instance from file
        val requestFile: RequestBody
        val mediaType = mimeType?.let { MediaType.parse(it) }
        requestFile = if (onUploadProgressListener != null) {
            ProgressRequestBody(mediaType, file, key, onUploadProgressListener, if (BuildConfig.DEBUG) 1 else 0)
        } else {
            RequestBody.create(mediaType, file)
        }

        return MultipartBody.Part.createFormData(key, file.name, requestFile)
    }
}
