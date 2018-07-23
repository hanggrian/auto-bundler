plugins {
    `java-library`
    kotlin("jvm")
    `bintray-release`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7

    sourceSets {
        getByName("main") {
            java.srcDir("src")
            resources.srcDir("res")
        }
        get("test").java.srcDir("tests/src")
    }
}

val ktlint by configurations.creating

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

    ktlint(ktlint())
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
