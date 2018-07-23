import org.gradle.kotlin.dsl.kotlin
import java.io.File
import java.io.IOException
import org.gradle.language.base.plugins.LifecycleBasePlugin.*
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    android("library")
    kotlin("android")
    dokka
    `bintray-release`
    `git-publish`
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

val ktlint by configurations.creating

dependencies {
    api(kotlin("stdlib", VERSION_KOTLIN))
    api(project(":$RELEASE_ARTIFACT-annotations"))
    implementation(androidx("fragment"))

    ktlint(ktlint())
}

inline val runtimeJar: File?
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

tasks {
    "ktlint"(JavaExec::class) {
        get("check").dependsOn(this)
        group = VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("--android", "src/**/*.kt")
    }
    "ktlintFormat"(JavaExec::class) {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args("--android", "-F", "src/**/*.kt")
    }

    withType<DokkaTask> {
        outputDirectory = "$buildDir/docs"
        doFirst { file(outputDirectory).deleteRecursively() }
    }

    get("gitPublishCopy").dependsOn(":$RELEASE_ARTIFACT:dokka")
}

gitPublish {
    repoUri = RELEASE_WEBSITE
    branch = "gh-pages"
    contents.from(
        "src",
        "../$RELEASE_ARTIFACT/build/docs")
}

tasks["gitPublishCopy"].dependsOn(":$RELEASE_ARTIFACT:dokka")

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
