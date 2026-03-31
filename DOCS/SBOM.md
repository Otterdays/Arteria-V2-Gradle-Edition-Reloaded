<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Full SBOM rebuild from live Gradle files for clean, current bill of materials. |

*Future contributors: append a row here when you materially change this doc.*

---

# SBOM (Software Bill of Materials) — Arteria V2 Gradle Edition Reloaded

> Last updated: 2026-03-30
> Source of truth: `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, `core/build.gradle.kts`, `gradle/wrapper/gradle-wrapper.properties`, `gradle/gradle-daemon-jvm.properties`
> Scope: Declared build/runtime/test dependencies and bundled non-Maven assets.

---

## Build Toolchain

| Component | Declared version | Source |
|-----------|------------------|--------|
| Gradle wrapper | `9.6.0-nightly-20260322000231+0000` | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin | `9.1.0` | root `build.gradle.kts` |
| Compose plugin | `org.jetbrains.kotlin.plugin.compose:2.0.0` | root `build.gradle.kts` |
| KSP plugin | `com.google.devtools.ksp:2.0.0-1.0.24` | root `build.gradle.kts` |
| Foojay toolchain resolver | `org.gradle.toolchains.foojay-resolver-convention:1.0.0` | `settings.gradle.kts` |
| Daemon JVM toolchain | `JDK 21` / vendor `ADOPTIUM` | `gradle/gradle-daemon-jvm.properties` |
| Java bytecode target | `JavaVersion.VERSION_21` | `app/build.gradle.kts`, `core/build.gradle.kts` |

---

## Android Targets

| Property | Value | Source |
|----------|-------|--------|
| `applicationId` | `com.arteria.game` | `app/build.gradle.kts` |
| `namespace` (`:app`) | `com.arteria.game` | `app/build.gradle.kts` |
| `namespace` (`:core`) | `com.arteria.game.core` | `core/build.gradle.kts` |
| `compileSdk` | `36.1` | `app/build.gradle.kts`, `core/build.gradle.kts` |
| `targetSdk` | `36` | `app/build.gradle.kts` |
| `minSdk` | `26` | `app/build.gradle.kts`, `core/build.gradle.kts` |

---

## Declared Dependencies

### `:app` module

| Scope | Coordinates | Declared |
|-------|-------------|----------|
| `implementation` | `androidx.core:core-ktx` | `1.18.0` |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-ktx` | `2.10.0` |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-compose` | `2.10.0` |
| `implementation` | `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.10.0` |
| `implementation` | `androidx.activity:activity-compose` | `1.13.0` |
| `implementation` | `androidx.compose:compose-bom` | `platform(2026.03.00)` |
| `implementation` | `androidx.compose.ui:ui` | `via BOM` |
| `implementation` | `androidx.compose.ui:ui-graphics` | `via BOM` |
| `implementation` | `androidx.compose.ui:ui-tooling-preview` | `via BOM` |
| `implementation` | `androidx.compose.material3:material3` | `via BOM` |
| `implementation` | `androidx.navigation:navigation-compose` | `2.9.7` |
| `implementation` | `androidx.room:room-runtime` | `2.8.0` |
| `implementation` | `androidx.room:room-ktx` | `2.8.0` |
| `ksp` | `androidx.room:room-compiler` | `2.8.0` |
| `testImplementation` | `junit:junit` | `4.13.2` |
| `testImplementation` | `org.jetbrains.kotlinx:kotlinx-coroutines-test` | `1.10.2` |
| `androidTestImplementation` | `androidx.test.ext:junit` | `1.3.0` |
| `androidTestImplementation` | `androidx.test.espresso:espresso-core` | `3.7.0` |
| `androidTestImplementation` | `androidx.room:room-testing` | `2.8.0` |
| `androidTestImplementation` | `androidx.compose:compose-bom` | `platform(2026.03.00)` |
| `androidTestImplementation` | `androidx.compose.ui:ui-test-junit4` | `via BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-tooling` | `via BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-test-manifest` | `via BOM` |

### `:core` module

| Scope | Coordinates | Declared |
|-------|-------------|----------|
| `implementation` | `androidx.core:core-ktx` | `1.18.0` |
| `testImplementation` | `junit:junit` | `4.13.2` |

---

## Bundled Non-Maven Assets

| Asset | Location | License |
|-------|----------|---------|
| Cinzel variable font | `app/src/main/res/font/cinzel.ttf` | SIL Open Font License 1.1 |

---

## Exclusions (Not in This SBOM)

| Technology | Status |
|------------|--------|
| React Native / Expo / Metro / Hermes | Not part of this native Gradle project |
| Node.js package ecosystem (`package.json`) | None declared in this project root |
| C++/NDK runtime libraries | Not active in current Gradle modules |

---

## Security Snapshot

| Date | Method | Result |
|------|--------|--------|
| 2026-03-30 | Coordinate-level review of declared deps | No known high-risk third-party dependencies introduced; stack is AndroidX/Google/Kotlin ecosystem plus JUnit |

---

## Update Procedure

1. Read live declarations from `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, and `core/build.gradle.kts`.
2. Update this SBOM in the same change as dependency edits.
3. Run verification: `./gradlew :app:assembleDebug` and relevant test tasks.
