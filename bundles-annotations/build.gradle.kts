group = RELEASE_GROUP
version = RELEASE_VERSION

plugins {
    `java-library`
    `maven-publish`
    signing
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("res")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

mavenPublishJvm("$RELEASE_ARTIFACT-annotations")