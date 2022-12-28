import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishBasePlugin
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("android") version libs.versions.kotlin apply false
    kotlin("android.extensions") version libs.versions.kotlin apply false
    kotlin("kapt") version libs.versions.kotlin apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.kover) apply false
    alias(libs.plugins.maven.publish) apply false
}

allprojects {
    group = RELEASE_GROUP
    version = RELEASE_VERSION
}

subprojects {
    plugins.withType<LibraryPlugin>().configureEach {
        configure<LibraryExtension>(::configureAndroid)
    }
    plugins.withType<AppPlugin>().configureEach {
        configure<BaseAppModuleExtension>(::configureAndroid)
    }
    plugins.withType<KotlinAndroidPluginWrapper> {
        kotlinExtension.jvmToolchain(libs.versions.jdk.get().toInt())
        (the<BaseExtension>() as ExtensionAware).extensions.getByType<KotlinJvmOptions>().jvmTarget =
            JavaVersion.toVersion(libs.versions.android.jdk.get()).toString()
    }
    plugins.withType<KotlinPluginWrapper> {
        kotlinExtension.jvmToolchain(libs.versions.jdk.get().toInt())
    }
    plugins.withType<MavenPublishBasePlugin> {
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.S01)
            signAllPublications()
            pom(::configurePom)
        }
    }
}

tasks {
    register(LifecycleBasePlugin.CLEAN_TASK_NAME) {
        delete(buildDir)
    }
    dokkaHtmlMultiModule {
        outputDirectory.set(buildDir.resolve("dokka/dokka/"))
    }
}

fun configureAndroid(extension: BaseExtension) {
    extension.setCompileSdkVersion(libs.versions.android.target.get().toInt())
    extension.defaultConfig {
        minSdk = libs.versions.android.min.get().toInt()
        targetSdk = libs.versions.android.target.get().toInt()
        version = RELEASE_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    extension.compileOptions {
        targetCompatibility = JavaVersion.toVersion(libs.versions.android.jdk.get())
        sourceCompatibility = JavaVersion.toVersion(libs.versions.android.jdk.get())
    }
}
