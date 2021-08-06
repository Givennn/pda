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

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))

    implementation(project(":library-android"))
    implementation(project(":library-kotlin"))

    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)

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

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}
