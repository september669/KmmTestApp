import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version Deps.Version.kotlin
}

kotlin {

    android()

    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }


    sourceSets {

        val commonMain by getting {
            dependencies {

                //  coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.Version.coroutinesNative}")

                //  atomic operations
                implementation("org.jetbrains.kotlinx:atomicfu:${Deps.Version.kotlinxAtomicfu}")

                //  logging
                api("org.dda.ankoLogger:AnkoLogger:0.2.1")

                //  http client
                api("io.ktor:ktor-client-core:${Deps.Version.ktor}")
                implementation("io.ktor:ktor-client-cio:${Deps.Version.ktor}")
                implementation("io.ktor:ktor-client-serialization:${Deps.Version.ktor}")

                //  Kotlin multiplatform / multi-format reflectionless serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Deps.Version.kotlinSerialization}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Deps.Version.kotlinSerialization}")

                //  DI
                implementation("org.kodein.di:kodein-di:${Deps.Version.kodein}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                //implementation("com.google.android.material:material:1.2.1")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Deps.Version.coroutinesNative}")

                implementation("io.ktor:ktor-client-android:${Deps.Version.ktor}")

                implementation("com.github.moxy-community:moxy:${Deps.Version.moxyMvp}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.1")
            }
        }
        val iosMain by getting {
            dependencies {
                //implementation("com.google.android.material:material:1.2.1")
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
            }
        }
        val iosTest by getting
    }
}

android {


    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(20)
        targetSdkVersion(30)

        multiDexEnabled = true

        buildConfigField("String", "LOGGING_TAG", "\"TEST_APP\"")

        buildConfigField("String", "API_URL", "\"https://front-task.chibbistest.ru/api/v1\"")

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)