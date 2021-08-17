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

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.android.support.constraint:constraint-layout:2.0.4")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.trello.rxlifecycle4:rxlifecycle:4.0.0")

    //ktx
    implementation("androidx.fragment:fragment-ktx:1.3.6")

    //nav
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

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
