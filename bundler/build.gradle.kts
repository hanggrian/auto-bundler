import org.gradle.internal.jvm.Jvm
import java.io.File
import java.io.IOException

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.novoda.bintray-release")
}

android {
    compileSdkVersion(targetSdk)
    buildToolsVersion(buildTools)
    defaultConfig {
        minSdkVersion(minSdk)
        targetSdkVersion(targetSdk)
        versionName = bintrayPublish
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
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
        }
        getByName("androidTest") {
            setRoot("tests")
            manifest.srcFile("tests/AndroidManifest.xml")
            java.srcDir("tests/src")
            res.srcDir("tests/res")
            resources.srcDir("tests/src")
        }
    }
}

dependencies {
    compile(kotlin("stdlib", kotlinVersion))
    compile(project(":bundler-annotations"))
    compile(support("support-fragment", supportVersion))

    androidTestImplementation(google("truth", truthVersion))
    androidTestImplementation(support("runner", runnerVersion, "test"))
    androidTestImplementation(support("espresso-core", espressoVersion, "test", "espresso"))

    testImplementation(junit(junitVersion))
    testImplementation(google("truth", truthVersion))
    testImplementation(google("compile-testing", compileVersion, "testing", "compile"))
    testImplementation(files(runtimeJar))
    testImplementation(files(org.gradle.internal.jvm.Jvm.current().toolsJar))
    testImplementation(project(":bundler-compiler"))
}

val runtimeJar: File?
    get() {
        try {
            val javaHome = File(System.getProperty("java.home")).canonicalFile
            File(javaHome, "lib/rt.jar").let { if (it.exists()) return it }
            File(javaHome, "jre/lib/rt.jar").let { if (it.exists()) return it }
            return null
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

publish {
    userOrg = bintrayUser
    groupId = bintrayGroup
    artifactId = bintrayArtifact
    publishVersion = bintrayPublish
    desc = bintrayDesc
    website = bintrayWeb
}