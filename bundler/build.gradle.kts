plugins {
    android("library")
    kotlin("android")
    bintray
    `bintray-release`
}

android {
    compileSdkVersion(SDK_TARGET)
    buildToolsVersion(BUILD_TOOLS)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        versionName = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("proguard-rules.pro")
        javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
            }
        }
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDir("res")
            resources.srcDir("src")
        }
    }
    lintOptions {
        isCheckTestSources = true
    }
    libraryVariants.all {
        generateBuildConfig?.enabled = false
    }
}

dependencies {
    api(project(":$RELEASE_ARTIFACT-annotations"))
    api(androidx("fragment"))

    testImplementation(junit())
    androidTestImplementation(junit())
    androidTestImplementation(kotlin("stdlib", VERSION_KOTLIN))
}

inline val runtimeJar: File?
    get() {
        try {
            val javaHome = File(System.getProperty("java.home")).canonicalFile
            File(javaHome, "lib/rt.jar").let { if (it.exists()) return it }
            File(javaHome, "jre/lib/rt.jar").let { if (it.exists()) return it }
            return null
        } catch (e: java.io.IOException) {
            throw RuntimeException(e)
        }
    }

publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = RELEASE_ARTIFACT
    publishVersion = RELEASE_VERSION
    desc = RELEASE_DESC
    website = RELEASE_WEBSITE
}
