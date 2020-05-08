const val VERSION_AUTOCOMMON = "0.10"
const val VERSION_GUAVA = "28.2-jre"
const val VERSION_TRUTH = "1.0.1"

fun Dependencies.google(repo: String? = null, module: String, version: String) =
    "com.google${repo?.let { ".$it" }.orEmpty()}:$module:$version"
