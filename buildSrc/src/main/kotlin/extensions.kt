import org.gradle.api.artifacts.dsl.DependencyHandler

const val bintrayUser = "hendraanggrian"
const val bintrayGroup = "com.hendraanggrian"
const val bintrayArtifact = "bundler"
const val bintrayPublish = "0.8"
const val bintrayDesc = "Android extra field binder with annotation processor"
const val bintrayWeb = "https://github.com/hendraanggrian/bundler"

const val minSdk = 14
const val targetSdk = 27
const val buildTools = "27.0.1"

const val kotlinVersion = "1.1.61"
const val supportVersion = "27.0.1"
const val javapoetVersion = "1.9.0"

const val autocommonVersion = "0.8"
const val guavaVersion = "23.4-jre"
const val truthVersion = "0.36"
const val compileVersion = "0.12"

const val junitVersion = "4.12"
const val runnerVersion = "1.0.1"
const val espressoVersion = "3.0.1"

fun DependencyHandler.support(module: String, version: String, vararg suffixes: String) = "${StringBuilder("com.android.support").apply { suffixes.forEach { append(".$it") } }}:$module:$version"
fun DependencyHandler.square(module: String, version: String) = "com.squareup:$module:$version"
fun DependencyHandler.junit(version: String) = "junit:junit:$version"
fun DependencyHandler.google(module: String, version: String, vararg suffixes: String) = if (suffixes.isEmpty()) "com.google.${module.substringBefore("-")}:$module:$version"
else "com.google${StringBuilder().apply { suffixes.forEach { append(".$it") } }}:$module:$version"