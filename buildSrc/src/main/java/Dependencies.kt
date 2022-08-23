@file:Suppress("SpellCheckingInspection")

object Sdk {
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 29
    const val COMPILE_SDK_VERSION = 29
}

object Versions {
    const val FRAGMENT_KTX = "1.2.5"
    const val ANDROIDX_TEST_EXT = "1.1.1"
    const val ANDROIDX_TEST = "1.2.0"
    const val APPCOMPAT = "1.3.0-alpha02"
    const val CONSTRAINT_LAYOUT = "2.0.0"
    const val RECYCLER_VIEW = "1.2.1"
    const val COORDINATOR_LAYOUT = "1.1.0"
    const val NAVIGATION = "2.3.0"
    const val CORE_KTX = "1.2.0"
    const val ESPRESSO_CORE = "3.2.0"
    const val JUNIT = "4.13"
    const val KTLINT = "0.37.2"

    // lib versions
    const val RETROFIT = "2.9.0"
    const val OK_HTTP = "4.9.0"
    const val STETHO = "1.5.0"
    const val TIMBER = "4.7.1"
    const val COIL = "2.1.0"
    const val EXO_PLAYER = "2.12.0"
    const val RX_JAVA = "3.0.7"
    const val KAML = "0.26.0"
}

object BuildPluginsVersion {
    const val AGP = "4.0.0"
    const val DETEKT = "1.10.0"
    const val KOTLIN = "1.4.21"
    const val KTLINT = "9.2.1"
    const val VERSIONS_PLUGIN = "0.28.0"
    const val SWAGGER_CODEGEN = "1.4.1"
}

object SupportLibs {
    const val MATERIAL = "com.google.android.material:material:1.4.0"

    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"

    //ktx
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"

    //Navigation
    const val NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_DYNAMIC_FEATURE_FRAGMENT_KTX =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_MOSHI_CONVERTER =
        "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"
    const val RETROFIT_RXJAVA3_ADAPTER =
        "com.squareup.retrofit2:adapter-rxjava3:${Versions.RETROFIT}"
    const val RETROFIT_SCALARS_CONVERTOR =
        "com.squareup.retrofit2:converter-scalars:${Versions.RETROFIT}"

    // ViewModel and LiveData
    const val LIFE_CYCLE = "androidx.lifecycle:lifecycle-extensions:2.0.0"

    //RxJava
    const val RX_JAVA = "io.reactivex.rxjava3:rxjava:${Versions.RX_JAVA}"
    const val RX_KOTLIN = "io.reactivex.rxjava2:rxkotlin:2.4.0-beta.1"
    const val RX_ANDROID = "io.reactivex.rxjava3:rxandroid:3.0.0"
    const val RX_LIFE_CYCLE = "com.trello.rxlifecycle4:rxlifecycle:4.0.0"
    const val RX_LIFE_CYCLE_ANDROID = "com.trello.rxlifecycle4:rxlifecycle-android:4.0.0"
    const val RX_LIFE_CYCLE_KOTLIN = "com.trello.rxlifecycle4:rxlifecycle-kotlin:4.0.0"

    //RxBinding
    const val RX_BINDING = "com.jakewharton.rxbinding4:rxbinding:4.0.0"
    const val EXO_PLAYER = "com.google.android.exoplayer:exoplayer:${Versions.RETROFIT}"

    // Retrofit will use okhttp 4 (it tas binary capability with okhttp 3)
    // See: https://square.github.io/okhttp/upgrading_to_okhttp_4/
    const val OK_HTTP = "com.squareup.okhttp3:okhttp:${Versions.OK_HTTP}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP}"
    const val STETHO = "com.facebook.stetho:stetho:${Versions.STETHO}"
    const val STETHO_OK_HTTP = "com.facebook.stetho:stetho-okhttp3:${Versions.STETHO}"
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val COIL = "io.coil-kt:coil:${Versions.COIL}"
    const val KAML = "com.charleskorn.kaml:kaml:${Versions.KAML}"
}

object TestingLib {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}
