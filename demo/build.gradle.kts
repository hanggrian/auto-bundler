plugins {
    android("application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(SDK_TARGET)
        applicationId = "com.example.$RELEASE_ARTIFACT"
        versionCode = 1
        versionName = RELEASE_VERSION
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
    lintOptions {
        isAbortOnError = false
    }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))

    implementation(androidx("appcompat"))
    implementation(androidx("cardview"))
    implementation(material())

    implementation(anko("commons"))
    implementation(anko("design"))

    val reveallayoutVersion = "0.4.0"
    implementation("com.hendraanggrian:reveallayout:$reveallayoutVersion")

    implementation(project(":$RELEASE_ARTIFACT"))
    kapt(project(":$RELEASE_ARTIFACT-compiler"))

    val butterknifeVersion = "8.8.1"
    implementation("com.jakewharton:butterknife:$butterknifeVersion")
    kapt("com.jakewharton:butterknife-compiler:$butterknifeVersion")

    val parcelerVersion = "1.1.6"
    implementation("org.parceler:parceler-api:$parcelerVersion")
    kapt("org.parceler:parceler:$parcelerVersion")
}
