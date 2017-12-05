plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(targetSdk)
    buildToolsVersion(buildTools)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(targetSdk)
        applicationId = "com.example.bundler"
        versionCode = 1
        versionName = "1.0"
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDir("res")
            resources.srcDir("src")
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("META-INF/services/javax.annotation.processing.Processor")
    }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))

    implementation(support("appcompat-v7", supportVersion))
    implementation(support("cardview-v7", supportVersion))
    implementation(support("design", supportVersion))

    val kotaVersion = "0.21"
    val reveallayoutVersion = "0.4.0"
    implementation(hendraanggrian("reveallayout", reveallayoutVersion))
    implementation(hendraanggrian("kota-appcompat-v7", kotaVersion))
    implementation(hendraanggrian("kota-design", kotaVersion))

    implementation(project(":bundler"))
    kapt(project(":bundler-compiler"))

    val butterknifeVersion = "8.8.1"
    implementation("com.jakewharton:butterknife:$butterknifeVersion")
    kapt("com.jakewharton:butterknife-compiler:$butterknifeVersion")

    val parcelerVersion = "1.1.6"
    implementation("org.parceler:parceler-api:$parcelerVersion")
    kapt("org.parceler:parceler:$parcelerVersion")
}

fun DependencyHandler.hendraanggrian(module: String, version: String) = "com.hendraanggrian:$module:$version"