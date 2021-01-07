package com.sehatq.test.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.app.ActivityManagerCompat
import com.sehatq.test.App
import com.sehatq.test.BuildConfig
import java.util.*

/**
 * A singleton instance for globally initialized and used variables in this app.
 */
object Config {

    /** Determines if app has checked if device has a low RAM */
    private var isLowRamInitialized = false

    /** Determines if app build time has been initialized */
    private var isBuildTimeInitialized = false

    /** @return true if the device is a low-RAM device */
    private var isLowRamDevice: Boolean = true

    /** @return the app build time  */
    private var buildTime: Calendar? = null

    /** @return the app version name */
    val versionName: String
        get() = BuildConfig.VERSION_NAME

    /** @return the app version code */
    val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    /** @return the app build time, formatted for easier reading  */
    fun isLowRamDevice(): Boolean {
        if (!isLowRamInitialized) initLowRam()
        return isLowRamDevice
    }

    /** @return the app build time, formatted for easier reading  */
    fun getBuildTime(): Calendar {
        if (!isBuildTimeInitialized) initBuildTime()
        return buildTime!!
    }

    /** @return the app build time, formatted for easier reading  */
    fun getSimpleBuildTime(): String {
        if (!isBuildTimeInitialized) initBuildTime()
        return DateTimeHelper.getStringFromCalendar(buildTime, "yyyy MMMM dd, HH:mm:ss")!!
    }

    /** Initialize if device is a low-RAM device */
    private fun initLowRam() {
        isLowRamDevice = ActivityManagerCompat.isLowRamDevice(App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        isLowRamInitialized = true
    }

    /** Initialize the build time */
    private fun initBuildTime() {
        buildTime = DateTimeHelper.getCalendarFromMillis(BuildConfig.M_TIMESTAMP)
        isBuildTimeInitialized = true
    }

    /**
     * Obtain the available internal storage space.
     * @return the free internal storage space in megabytes
     */
    val freeInternalSpaceInMb: Long
        get() = freeInternalSpaceInBytes / (1024 * 1024)

    /**
     * Obtain the available internal storage space.
     * @return the free internal storage space in kilobytes
     */
    val freeInternalSpaceInKb: Long
        get() = freeInternalSpaceInBytes / 1024

    /**
     * Obtain the available internal storage space.
     * @return the free internal storage space in bytes
     */
    val freeInternalSpaceInBytes: Long
        get() {
            val stat = StatFs(Environment.getDataDirectory().path)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.blockSizeLong * stat.availableBlocksLong
            } else {
                @Suppress("DEPRECATION")
                return stat.blockSize.toLong() * stat.availableBlocks.toLong()
            }
        }

    /**
     * Obtain the device's screen width.
     * @return the screen width in pixel
     */
    val screenWidth: Int
        get() {
            val metrics = DisplayMetrics()
            (App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }

    /**
     * Obtain the device's screen height.
     * @return the screen height in pixel
     */
    val screenHeight: Int
        get() {
            val metrics = DisplayMetrics()
            (App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
            return metrics.heightPixels
        }
}
