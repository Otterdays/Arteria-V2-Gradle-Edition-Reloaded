pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// [TRACE: DOCS/SCRATCHPAD.md — Cursor JRE / AGP jlink on android-37]
// Prefer machine-local jdk.dir (gitignored local.properties) over IDE-injected JRE paths.
run {
    val localProperties = java.util.Properties()
    val localPropertiesFile = file("local.properties")
    if (localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
        localProperties.getProperty("jdk.dir")?.let { jdkDir ->
            val jdk = java.io.File(jdkDir.replace("\\\\", "\\"))
            val jlink = java.io.File(jdk, "bin/jlink.exe")
            if (jlink.isFile) {
                System.setProperty("org.gradle.java.home", jdk.absolutePath)
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Arteria"
include(":app")
include(":core")
