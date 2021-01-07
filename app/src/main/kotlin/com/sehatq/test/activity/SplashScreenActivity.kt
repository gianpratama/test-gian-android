package com.sehatq.test.activity

import com.sehatq.test.R
import com.sehatq.test.activity.core.CoreActivity

/**
 * Sample splash screen activity.
 * This splash screen already handles its lifecycle, meaning that the
 * pending intent doesn't launch even after user has closed the app.
 */
class SplashScreenActivity : CoreActivity() {

    override val viewRes = R.layout.activity_splash_screen
}
