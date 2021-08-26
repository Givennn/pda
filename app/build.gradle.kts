import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

    // TODO update temp
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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
        dataBinding = true
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

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation(SupportLibs.ANDROIDX_CORE_KTX)
    implementation(SupportLibs.NAVIGATION_FRAGMENT_KTX)
    implementation(SupportLibs.NAVIGATION_UI_KTX)
    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.RECYCLER_VIEW)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.RETROFIT)
    implementation(SupportLibs.RETROFIT_MOSHI_CONVERTER)
    implementation(SupportLibs.RETROFIT_RXJAVA3_ADAPTER)
    implementation(SupportLibs.LOGGING_INTERCEPTOR)
    implementation(SupportLibs.OK_HTTP)
    implementation(SupportLibs.TIMBER)
//    implementation(SupportLibs.NAVIGATION_DYNAMIC_FEATURE_FRAGMENT_KTX)
//    implementation(SupportLibs.LIFE_CYCLE)
    implementation(SupportLibs.RX_JAVA)
//    implementation(SupportLibs.RX_ANDROID)
//    implementation(SupportLibs.RX_KOTLIN)
    implementation(SupportLibs.RX_LIFE_CYCLE)
    implementation(SupportLibs.RX_LIFE_CYCLE_ANDROID)
    implementation(SupportLibs.RX_LIFE_CYCLE_KOTLIN)
    implementation(SupportLibs.RX_BINDING)
    implementation(SupportLibs.COIL)
    implementation(SupportLibs.MATERIAL)
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    testImplementation(TestingLib.JUNIT)

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}