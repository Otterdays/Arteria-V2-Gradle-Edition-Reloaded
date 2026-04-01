package com.arteria.game.ui.components

/**
 * Static OSS notices for major dependencies (no Gradle plugin — KISS).
 * Amend when bumping coordinates; keep alphabetized by name.
 */
data class OssLibraryNotice(val name: String, val license: String, val url: String)

val ARTERIA_OSS_NOTICES: List<OssLibraryNotice> = listOf(
    OssLibraryNotice("Android Gradle Plugin", "Apache-2.0", "https://developer.android.com/studio/terms"),
    OssLibraryNotice("AndroidX Activity Compose", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/activity"),
    OssLibraryNotice("AndroidX Core KTX", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/core"),
    OssLibraryNotice("AndroidX DataStore Preferences", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/datastore"),
    OssLibraryNotice("AndroidX Lifecycle", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/lifecycle"),
    OssLibraryNotice("AndroidX Navigation Compose", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/navigation"),
    OssLibraryNotice("AndroidX Room", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/room"),
    OssLibraryNotice("Cinzel font (OFL)", "OFL-1.1", "https://github.com/google/fonts/tree/main/ofl/cinzel"),
    OssLibraryNotice("Jetpack Compose BOM / Material3", "Apache-2.0", "https://developer.android.com/jetpack/androidx/releases/compose"),
    OssLibraryNotice("Kotlin", "Apache-2.0", "https://kotlinlang.org/"),
)
