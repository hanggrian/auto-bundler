plugins {
    `java-library`
    id("com.novoda.bintray-release")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

publish {
    userOrg = bintrayUser
    groupId = bintrayGroup
    artifactId = "$bintrayArtifact-annotations"
    publishVersion = bintrayPublish
    desc = bintrayDesc
    website = bintrayWeb
}