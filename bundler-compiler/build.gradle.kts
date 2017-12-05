plugins {
    `java-library`
    kotlin("jvm")
    id("com.novoda.bintray-release")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    compile(kotlin("stdlib", kotlinVersion))
    compile(project(":bundler-annotations"))
    compile(google("auto-common", autocommonVersion))
    compile(google("guava", guavaVersion))
    compile(square("javapoet", javapoetVersion))

    compileOnly(support("support-annotations", supportVersion))
    compileOnly(files(org.gradle.internal.jvm.Jvm.current().getToolsJar()))

    testImplementation(junit(junitVersion))
    testImplementation(google("truth", truthVersion))
}

publish {
    userOrg = bintrayUser
    groupId = bintrayGroup
    artifactId = "$bintrayArtifact-compiler"
    publishVersion = bintrayPublish
    desc = bintrayDesc
    website = bintrayWeb
}