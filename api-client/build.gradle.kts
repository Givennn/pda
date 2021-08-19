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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.0")

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