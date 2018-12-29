plugins {
    `java-library`
    kotlin("jvm")
    bintray
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

val ktlint by configurations.registering

dependencies {
    compile(kotlin("stdlib", VERSION_KOTLIN))
    compile(project(":$RELEASE_ARTIFACT-annotations"))
    compile(google("auto-common", VERSION_AUTOCOMMON))
    compile(google("guava", VERSION_GUAVA))
    compile(square("javapoet", VERSION_JAVAPOET))

    compileOnly(androidx("annotation"))
    compileOnly(files(org.gradle.internal.jvm.Jvm.current().toolsJar))

    testImplementation(junit())
    testImplementation(google("truth", VERSION_TRUTH))

    ktlint {
        invoke(ktlint())
    }
}

tasks {
    register<JavaExec>("ktlint") {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath = ktlint.get()
        main = "com.github.shyiko.ktlint.Main"
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
        classpath = ktlint.get()
        main = "com.github.shyiko.ktlint.Main"
        args("-F", "src/**/*.kt")
    }
}

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
    website = RELEASE_WEBSITE
}
