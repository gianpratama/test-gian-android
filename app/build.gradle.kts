plugins {
    id(AndroidPlugin.application)
    id(AndroidPlugin.kotlin)
    id(AndroidPlugin.kotlinExtensions)
    id(AndroidPlugin.kotlinKapt)
    id(AndroidPlugin.report)
    id(AndroidPlugin.dexcount)
}

// To view the dependency tree, open your terminal and run this command:
// gradlew htmlDependencyReport
//
// The result can be viewed in
// "\\build\reports\project\dependencies\index.html"

android {
    buildToolsVersion(Android.buildTools)
    compileSdkVersion(Android.compile)

    defaultConfig {
        applicationId = "com.sehatq.test"
        minSdkVersion(Android.min)
        targetSdkVersion(Android.target)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        // Add a BuildConfig timestamp
        buildConfigField("long", "M_TIMESTAMP", "${System.currentTimeMillis()}L")
    }

    // Enable Java 8 features
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Set the source path
    sourceSets["main"].java.srcDir("src/main/kotlin")
    sourceSets["test"].java.srcDir("src/test/kotlin")

    // Configure the build types
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            proguardFiles(getDefaultProguardFile(ProGuard.defaultAndroid))
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true

            proguardFiles(getDefaultProguardFile(ProGuard.defaultAndroid))
        }
    }

    externalNativeBuild {
        cmake {
            setPath("src/main/cpp/CMakeLists.txt")
            setVersion(Android.cMake)
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  testImplementation(LibTest.junit)
    androidTestImplementation(LibTest.testRunner)
    androidTestImplementation(LibTest.espresso)

    // .jar libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(LibKotlin.jdk)

    // AndroidX
    implementation(LibAndroidX.appCompat)
    implementation(LibAndroidX.legacyUtils)
    implementation(LibAndroidX.constraintLayout)
    implementation(LibAndroidX.material)
    implementation(LibAndroidX.mulitiDex)
    implementation(LibAndroidXLifecycle.extension)
    implementation(LibAndroidXLifecycle.viewModel)
    implementation(LibAndroidXLifecycle.liveData)

    implementation(LibAndroidX.navigationFragment)
    implementation(LibAndroidX.navigationFragmentKtx)
    implementation(LibAndroidX.navigationUi)
    implementation(LibAndroidX.navigationUiKtx)

    // Other libraries
    implementation(Lib.retrofit)
    implementation(Lib.retrofiConverterGson)
    implementation(Lib.retrofitLogger)
    implementation(Lib.gson)
    debugImplementation(Lib.gander)
    releaseImplementation(Lib.ganderNoOp)
    implementation(Lib.customCrashActivity)
    implementation(Lib.fresco)
    implementation(Lib.facebook)
    implementation(Lib.firebaseCore)
    implementation(Lib.firebaseAuth)
    implementation(Lib.playServiceAuth)
    implementation(Lib.fancyButtons)
    implementation(Lib.coil)
    implementation(Lib.rxjava2)
    implementation(Lib.rxandroid)
    implementation(Lib.rxjava)
    implementation(Lib.hawk)
}

dexcount {
    includeTotalMethodCount = true
}

apply (mapOf("plugin" to AndroidPlugin.gms))
