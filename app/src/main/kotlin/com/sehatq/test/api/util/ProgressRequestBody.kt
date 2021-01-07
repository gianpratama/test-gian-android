package com.sehatq.test.api.util

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * A customized [RequestBody] that can display progress updates.
 * Obtained from: http://stackoverflow.com/a/33384551/5315490
 */
class ProgressRequestBody(
        private val mediaType: MediaType?,
        private val file: File,
        private val key: String,
        private val onUploadProgressListener: OnUploadProgressListener? = null,
        private val ignoreFirstNumberOfWriteToCalls: Int = 0) : RequestBody() {

    private var numberWriteToCalls: Int = 0

    override fun contentType(): MediaType? {
        return mediaType
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        numberWriteToCalls++

        val fileLength = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        val stream = FileInputStream(file)
        try {
            val handler = Handler(Looper.getMainLooper())
            var lastProgressPercent = 0f
            var uploaded: Long = 0

            var read = stream.read(buffer)
            while (read != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = stream.read(buffer)

                // when using HttpLoggingInterceptor it calls writeTo and passes data into a local buffer just for logging purposes.
                // the second call to write to is the progress we actually want to track
                if (numberWriteToCalls > ignoreFirstNumberOfWriteToCalls) {
                    val progress = uploaded.toFloat() / fileLength.toFloat() * 100f

                    // prevent publishing too many updates, which slows upload, by checking if the upload has progressed by at least 1 percent
                    if (progress - lastProgressPercent > 1 || progress == 100f) {
                        // publish progress
                        onUploadProgressListener?.let { handler.post { it.onProgressUpdate(key, Math.round(progress)) } }
                        lastProgressPercent = progress
                    }
                }
            }
        } finally {
            stream.close()
        }
    }

    /** The interface for upload callbacks */
    interface OnUploadProgressListener {

        /** Called when progress is updated */
        fun onProgressUpdate(key: String, percentage: Int)
    }

    companion object {

        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}
