package com.sehatq.test

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.sehatq.test.activity.core.UncaughtExceptionActivity
import com.ashokvarma.gander.Gander
import com.ashokvarma.gander.persistence.GanderPersistence
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.drawee.backends.pipeline.Fresco
import com.orhanobut.hawk.Hawk

/**
 * The [Application] class,
 * important initializations should take place here.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        Fresco.initialize(this)

        Hawk.init(this).build()


        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        // Initialize Gander
        Gander.setGanderStorage(GanderPersistence.getInstance(this))

        // Use custom Activity on fatal errors
        CaocConfig.Builder.create()
                .errorActivity(UncaughtExceptionActivity::class.java)
                .apply()
    }

    companion object {

        /**
         * The application [Context] made static.
         * Do **NOT** use this as the context for a view,
         * this is mostly used to simplify calling of resources
         * (esp. String resources) outside activities.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}
