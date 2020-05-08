plugins {
    android("application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(SDK_TARGET)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(SDK_TARGET)
        applicationId = "com.example.$RELEASE_ARTIFACT"
        versionName = RELEASE_VERSION
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDir("src")
            res.srcDir("res")
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
    maven("https://jitpack.io")
}

dependencies {
    api(kotlin("stdlib", VERSION_KOTLIN))

    implementation(project(":$RELEASE_ARTIFACT-ktx"))
    kapt(project(":$RELEASE_ARTIFACT-compiler"))

    implementation(kotlin("stdlib", VERSION_KOTLIN))

    implementation(androidx("core", "core-ktx", VERSION_ANDROIDX))
    implementation(androidx("appcompat"))
    implementation(androidx("preference", "preference-ktx", version = VERSION_ANDROIDX))
    implementation(material())

    /*val butterknifeVersion = "9.0.0-rc2"
    implementation("com.jakewharton:butterknife:$butterknifeVersion")
    kapt("com.jakewharton:butterknife-compiler:$butterknifeVersion")*/

    val parcelerVersion = "1.1.12"
    implementation("org.parceler:parceler-api:$parcelerVersion")
    kapt("org.parceler:parceler:$parcelerVersion")
}
