version = LibraryKotlinCoordinates.LIBRARY_VERSION

plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
    id("com.yelp.codegen.plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")

    implementation(SupportLibs.MOSHI)
    implementation(SupportLibs.MOSHI_ADAPTER)
    implementation(SupportLibs.MOSHI_KOTLIN)
    implementation(SupportLibs.OK_HTTP)
    implementation(SupportLibs.RETROFIT)
    implementation(SupportLibs.RETROFIT_MOSHI_CONVERTER)
    implementation(SupportLibs.RETROFIT_RXJAVA_ADAPTER)
    implementation(SupportLibs.THREE_TEN_BP)

    implementation(SupportLibs.RX_JAVA)
    implementation(SupportLibs.RX_ANDROID)
    testImplementation(TestingLib.JUNIT)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

generateSwagger {
    platform = "kotlin"
    packageName = "com.panda.android.client"
    specName = "docs"
    specVersion = "1.0.0"
    inputFile = file("../swagger-build/docs.json")
    outputDir = file(project.layout.projectDirectory.dir("./src/main/java/"))
}