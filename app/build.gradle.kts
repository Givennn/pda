plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }


    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))

    implementation(project(":library-android"))
    implementation(project(":library-kotlin"))
    implementation(project(":api-client"))

    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)
    implementation(SupportLibs.MATERIAL)
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation("com.trello.rxlifecycle4:rxlifecycle:4.0.0")
//    const val RX_LIFE_CYCLE_ANDROID = "com.trello.rxlifecycle4:rxlifecycle-android:4.0.0"
//    const val RX_LIFE_CYCLE_KOTLIN = "com.trello.rxlifecycle4:rxlifecycle-kotlin:4.0.0"

    //ktx
    implementation(SupportLibs.ANDROIDX_FRAGMENT_KTX)

    //nav
    implementation(SupportLibs.NAVIGATION_FRAGMENT_KTX)
    implementation(SupportLibs.NAVIGATION_UI_KTX)

    //API
    implementation(SupportLibs.MOSHI)
    implementation(SupportLibs.MOSHI_ADAPTER)
    implementation(SupportLibs.MOSHI_KOTLIN)
    implementation(SupportLibs.OK_HTTP)
    implementation(SupportLibs.RETROFIT)
    implementation(SupportLibs.RETROFIT_MOSHI_CONVERTER)
    implementation(SupportLibs.RETROFIT_RXJAVA_ADAPTER)
    implementation(SupportLibs.LOGGING_INTERCEPTOR)
    implementation(SupportLibs.THREE_TEN_BP)

    //RxJava
    implementation(SupportLibs.RX_JAVA)
    implementation(SupportLibs.RX_ANDROID)

    //log
    implementation(SupportLibs.TIMBER)

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}
