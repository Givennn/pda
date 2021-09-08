import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.gradle.internal.dsl.BaseFlavor

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
//    kotlin("kapt")
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
        buildConfigFieldFromGradleProperty("apiBaseUrl")
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
//    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")

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
//    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    testImplementation(TestingLib.JUNIT)

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}

/*
Takes value from Gradle project property and sets it as build config property
 */
fun BaseFlavor.buildConfigFieldFromGradleProperty(gradlePropertyName: String) {
    val propertyValue = project.properties[gradlePropertyName] as? String
    checkNotNull(propertyValue) { "Gradle property $gradlePropertyName is null" }

    val androidResourceName = "GRADLE_${gradlePropertyName.toSnakeCase()}".toUpperCase()
    buildConfigField("String", androidResourceName, propertyValue)
}

fun String.toSnakeCase() = this.split(Regex("(?=[A-Z])")).joinToString("_") { it.toLowerCase() }