plugins {
    id("com.android.application")
    kotlin("android")
    //id("kotlin-kapt")
}

dependencies {
    repositories {
        maven { url = uri("https://dl.bintray.com/icerockdev/moko") }
    }


    implementation(project(":shared"))
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.android.material:material:1.2.1")

    implementation("org.kodein.di:kodein-di-framework-android-x:${Deps.Version.kodein}")


    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Deps.Version.coroutinesNative}")

    implementation("dev.icerock.moko:mvvm-viewbinding:${Deps.Version.iceRockMvvm}")


    //implementation("com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:1.4.1")


    implementation("com.xwray:groupie:${Deps.Version.groupie}")
    implementation("com.xwray:groupie-viewbinding:${Deps.Version.groupie}")


    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")


}

android {
    compileSdkVersion(30)
    defaultConfig {


        applicationId = "org.dda.testwork.androidApp"
        minSdkVersion(20)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}