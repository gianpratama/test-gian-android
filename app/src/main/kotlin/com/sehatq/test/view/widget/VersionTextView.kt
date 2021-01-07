package com.sehatq.test.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.sehatq.test.BuildConfig
import com.sehatq.test.util.Config

/**
 * A [TextView] that shows current app version.
 *
 * Debug builds will show more detailed version, but release build will only show the version name.
 */
class VersionTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context!!, attrs, defStyleAttr) {

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    /** The app's version as a simple readable String */
    private var appVersion = ""

    init {
        initAppVersion()
        if (BuildConfig.DEBUG) appVersion += " - DEBUG"
        super.setText(appVersion)
    }

    /** Obtain the application version as a String */
    private fun initAppVersion() {
        if (isInEditMode) {
            appVersion = "App Version"
            return
        }

        appVersion = if (!Config.versionName.toLowerCase().startsWith("v")) "" else "v"
        if (BuildConfig.DEBUG) {
            appVersion += Config.versionName + ", build " +
                    Config.versionCode + "\n" +
                    Config.getSimpleBuildTime()
        } else {
            appVersion += Config.versionName
        }
    }
}
