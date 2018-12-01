buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(android())
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
        classpath(bintrayRelease())
        classpath(bintray())
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)
    }
    register<Wrapper>("wrapper") {
        gradleVersion = VERSION_GRADLE
    }
}