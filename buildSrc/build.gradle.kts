plugins {
    id("org.jetbrains.kotlin.jvm") version("1.4.21")
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    implementation("com.android.tools.build:gradle:4.1.1")
}