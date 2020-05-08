plugins {
    android("library")
    kotlin("android")
    dokka
    `bintray-release`
}

android {
    compileSdkVersion(SDK_TARGET)
    defaultConfig {
        minSdkVersion(SDK_MIN)
        targetSdkVersion(SDK_TARGET)
        versionName = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
            }
        }
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDir("src")
            res.srcDir("res")
        }
    }
    libraryVariants.all {
        generateBuildConfigProvider?.configure {
            enabled = false
        }
    }
}

val configuration = configurations.register("ktlint")

dependencies {
    api(kotlin("stdlib", VERSION_KOTLIN))
    api(project(":$RELEASE_ARTIFACT"))

    configuration {
        invoke(ktlint())
    }
}

tasks {
    register<JavaExec>("ktlint") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = configuration.get()
        main = "com.pinterest.ktlint.Main"
        args("src/**/*.kt")
    }
    "check" {
        dependsOn("ktlint")
    }
    register<JavaExec>("ktlintFormat") {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath = configuration.get()
        main = "com.pinterest.ktlint.Main"
        args("-F", "src/**/*.kt")
    }

    named<org.jetbrains.dokka.gradle.DokkaTask>("dokka") {
        outputDirectory = "$buildDir/docs"
        doFirst { file(outputDirectory).deleteRecursively() }
    }
}

publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = "$RELEASE_ARTIFACT-ktx"
    publishVersion = RELEASE_VERSION
    desc = RELEASE_DESC
    website = RELEASE_WEB
}
