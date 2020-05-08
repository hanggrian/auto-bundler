plugins {
    `java-library`
    kotlin("jvm")
    dokka
    `bintray-release`
}

group = RELEASE_GROUP
version = RELEASE_VERSION

sourceSets {
    getByName("main") {
        java.srcDir("src")
        resources.srcDir("res")
    }
    get("test").java.srcDir("tests/src")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

val configuration = configurations.register("ktlint")

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(project(":$RELEASE_ARTIFACT-annotations"))
    implementation(google("auto", "auto-common", VERSION_AUTOCOMMON))
    implementation(google("guava", "guava", VERSION_GUAVA))
    implementation(hendraanggrian("javapoet-ktx", VERSION_JAVAPOET_KTX))

    compileOnly(androidx("annotation"))
    compileOnly(files(org.gradle.internal.jvm.Jvm.current().toolsJar))

    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
    testImplementation(google("truth", "truth", VERSION_TRUTH))

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

publishKotlinFix()
publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false
    repoName = RELEASE_REPO

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = "$RELEASE_ARTIFACT-compiler"
    publishVersion = RELEASE_VERSION
    desc = RELEASE_DESC
    website = RELEASE_WEB
}
