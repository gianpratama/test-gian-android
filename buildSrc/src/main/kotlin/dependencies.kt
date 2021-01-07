const val versionKotlin = "1.3.50"

object ClassPath {

    object Version {
        const val androidGradle = "3.5.0"
        const val dexcount = "0.8.6"
        const val gms = "4.3.3"
    }

    const val androidGradle = "com.android.tools.build:gradle:${Version.androidGradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$versionKotlin"
    const val dexcount = "com.getkeepsafe.dexcount:dexcount-gradle-plugin:${Version.dexcount}"
    const val gms = "com.google.gms:google-services:${Version.gms}"
}

object Maven {

    const val jitpack = "https://jitpack.io"
}

object AndroidPlugin {

    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin = "kotlin-android"
    const val kotlinExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val report = "project-report"
    const val dexcount = "com.getkeepsafe.dexcount"
    const val gms = "com.google.gms.google-services"
}

object Android {
    const val buildTools = "29.0.2"
    const val compile = 29
    const val min = 21
    const val target = 29
    const val cMake = "3.10.2"
}

object LibTest {

    private object Version {
        const val junit = "4.12"
        const val testRunner = "1.1.0"
        const val espresso = "3.2.0"
    }

    const val junit = "junit:junit:${Version.junit}"
    const val testRunner = "androidx.test:runner:${Version.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
}

object LibKotlin {

    const val jdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$versionKotlin"
}

object LibAndroidX {

    private object Version {
        const val appCompat = "1.0.2"
        const val legacyUtils = "1.0.0"
        const val constraintLayout = "1.1.3"
        const val material = "1.0.0"
        const val multideDex = "2.0.1"

        const val navigationFragment = "2.2.2"
        const val navigationUi = "2.2.2"
        const val navigationFragmentKtx = "2.2.2"
        const val navigationUiKtx = "2.2.2"
    }

    const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val legacyUtils = "androidx.legacy:legacy-support-core-utils:${Version.legacyUtils}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val mulitiDex = "androidx.multidex:multidex:${Version.multideDex}"

    const val navigationFragment = "androidx.navigation:navigation-fragment:${Version.navigationFragment}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Version.navigationUi}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigationFragmentKtx}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigationUiKtx}"
}

object LibAndroidXLifecycle {

    private object Version {
        const val extension = "2.0.0"
        const val viewModel = "2.0.0"
        const val liveData = "2.0.0"
    }

    const val extension = "androidx.lifecycle:lifecycle-extensions:${Version.extension}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel:${Version.viewModel}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata:${Version.liveData}"
}

object Lib {

    private object Version {
        const val retrofit = "2.6.1"
        const val retrofiConverterGson = "2.6.1"
        const val retrofitLogger = "3.12.3"
        const val gson = "2.8.5"
        const val gander = "3.1.0"
//        const val crashlytics = "2.10.1"
        const val customCrashActivity = "2.2.0"
        const val imageCropper = "2.7.0"
        const val fresco = "2.2.0"
        const val facebook = "8.1.0"
        const val playServiceAuth = "19.0.0"
        const val fancyButtons = "1.9.1"
        const val firebaseAuth = "20.0.1"
        const val firebaseCore = "18.0.0"
        const val coil = "1.1.0"
        const val rxjava = "2.1.9"
        const val rxandroid = "2.0.2"
        const val rxjava2 = "2.3.0"
        const val hawk = "2.0.1"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofiConverterGson = "com.squareup.retrofit2:converter-gson:${Version.retrofiConverterGson}"
    const val retrofitLogger = "com.squareup.okhttp3:logging-interceptor:${Version.retrofitLogger}"
    const val gson = "com.google.code.gson:gson:${Version.gson}"
    const val gander = "com.ashokvarma.android:gander-persistence:${Version.gander}"
    const val ganderNoOp = "com.ashokvarma.android:gander-no-op:${Version.gander}"
//    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Version.crashlytics}"
    const val customCrashActivity = "cat.ereza:customactivityoncrash:${Version.customCrashActivity}"
    const val imageCropper = "com.theartofdev.edmodo:android-image-cropper:${Version.imageCropper}"
    const val fresco= "com.facebook.fresco:fresco:${Version.fresco}"
    const val facebook= "com.facebook.android:facebook-android-sdk:${Version.facebook}"
    const val firebaseCore= "com.google.firebase:firebase-core:${Version.firebaseCore}"
    const val firebaseAuth= "com.google.firebase:firebase-auth:${Version.firebaseAuth}"
    const val playServiceAuth= "com.google.android.gms:play-services-auth:${Version.playServiceAuth}"
    const val fancyButtons= "com.github.medyo:fancybuttons:${Version.fancyButtons}"
    const val coil= "io.coil-kt:coil:${Version.coil}"
    const val rxjava= "io.reactivex.rxjava2:rxjava:${Version.rxjava}"
    const val rxandroid= "io.reactivex.rxjava2:rxandroid:${Version.rxandroid}"
    const val rxjava2 = "com.squareup.retrofit2:adapter-rxjava2:${Version.rxjava2}"
    const val hawk = "com.orhanobut:hawk:${Version.hawk}"
}

object ProGuard {

    const val defaultAndroid = "proguard-android.txt"
}
