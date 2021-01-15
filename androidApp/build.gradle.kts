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

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Deps.Version.coroutinesNative}")

    implementation("com.github.moxy-community:moxy:${Deps.Version.moxyMvp}")
    implementation("com.github.moxy-community:moxy-androidx:${Deps.Version.moxyMvp}")
    kapt("com.github.moxy-community:moxy-compiler:${Deps.Version.moxyMvp}")
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