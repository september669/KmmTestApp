plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.multidex:multidex:2.0.1")

    val moxyVersion = "2.2.1"
    implementation("com.github.moxy-community:moxy:$moxyVersion")
    kapt("com.github.moxy-community:moxy-compiler:$moxyVersion")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "org.dda.testwork.androidApp"
        minSdkVersion(20)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}