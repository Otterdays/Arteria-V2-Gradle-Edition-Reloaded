<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | Settings backlog: `androidx.datastore:datastore-preferences:1.2.0`; app prefs + theme/motion/audio/haptics/offline-report; OSS/Credits screens; reset/delete profile + `GameDao` deletes; `TickEngine.DEFAULT_MAX_OFFLINE_MS`; `:app:compileDebugKotlin` + `:app:testDebugUnitTest` green. |
| 2026-04-01 | Cursor Agent | Composer | `:app` `buildFeatures.buildConfig = true` for `BuildConfig.VERSION_NAME` / `VERSION_CODE` in Settings About row (no new Maven coordinates). |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Dependency sweep (no-bump pass): verified live Gradle declarations and held current pins; only newer options are alpha/nightly channels for core AndroidX/AGP lines. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Phase 4 close: documented `GameDatabase` schema v2 + `MIGRATION_1_2` in Android Targets appendix row. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | SBOM schema refresh: replaced `Declared` with `Installed` + `Next available` columns across toolchain and dependency tables. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Kotlin/dep modernization pass: Kotlin Compose plugin `2.3.20`, KSP `2.3.6`, Compose BOM `2026.03.01`, Room `2.8.4`; no tests executed per user preference. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Gradle wrapper bump to `9.6.0-20260331012943+0000`; added `build-with-jdk26.bat`; SBOM toolchain row amended. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Full SBOM rebuild from live Gradle files for clean, current bill of materials. |

*Future contributors: append a row here when you materially change this doc.*

---

# SBOM (Software Bill of Materials) — Arteria V2 Gradle Edition Reloaded

> Last updated: 2026-04-01
> Source of truth: `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, `core/build.gradle.kts`, `gradle/wrapper/gradle-wrapper.properties`, `gradle/gradle-daemon-jvm.properties`
> Scope: Declared build/runtime/test dependencies and bundled non-Maven assets.

## [AMENDED 2026-03-31] SBOM Modernization Profile

| Field | Rule |
|------|------|
| Installed | Exact value from live Gradle files only |
| Update channel | `stable`, `alpha`, `nightly`, or `pinned` |
| Next review date | Monthly default cadence unless urgent CVE/toolchain break |
| Waiver logging | Any intentional holdback must be logged in `DOCS/SCRATCHPAD.md` |

### Update channel map (current stack)

| Component group | Installed baseline | Update channel | Next review date |
|------|------|------|------|
| Gradle wrapper | `9.6.0-20260331012943+0000` | `nightly` | `2026-04-30` |
| AGP | `9.1.0` | `stable` | `2026-04-30` |
| Kotlin Compose plugin | `2.3.20` | `stable` | `2026-04-30` |
| KSP plugin | `2.3.6` | `stable` | `2026-04-30` |
| Compose BOM | `2026.03.01` | `stable` (monthly BOM) | `2026-04-30` |
| Lifecycle / Navigation / Room track | `2.10.0` / `2.9.7` / `2.8.4` | `stable` | `2026-04-30` |
| Daemon JDK | `21 (ADOPTIUM)` | `pinned` | `2026-04-30` |

**`[AMENDED 2026-03-31]:` Dependency sweep result (no-bump pass):**
- `AGP 9.1.0` remains the stable baseline in this repo; `9.2.x` is alpha track.
- Lifecycle, Navigation, and Room have newer alpha lines available, but no required stable migration for current Phase 5 work.
- Compose BOM remains on monthly stable cadence (`2026.03.01` currently pinned); next review on the scheduled monthly window.
- No dependency edits were applied in this sweep.

---

## Build Toolchain

| Component | Installed | Next available | Source |
|-----------|-----------|----------------|--------|
| Gradle wrapper | `9.6.0-20260331012943+0000` | `nightly` (rolling) | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin | `9.1.0` | `9.2.0-alpha` | root `build.gradle.kts` |
| Compose plugin | `org.jetbrains.kotlin.plugin.compose:2.3.20` | `2.3.20` (current stable) | root `build.gradle.kts` |
| KSP plugin | `com.google.devtools.ksp:2.3.6` | `2.3.6` (current stable) | root `build.gradle.kts` |
| Foojay toolchain resolver | `org.gradle.toolchains.foojay-resolver-convention:1.0.0` | `verify before bump` | `settings.gradle.kts` |
| Daemon JVM toolchain | `JDK 21` / vendor `ADOPTIUM` | `JDK 26` (local experimental path) | `gradle/gradle-daemon-jvm.properties` |
| Java bytecode target | `JavaVersion.VERSION_21` | `VERSION_26` (only when AGP/Kotlin matrix supports) | `app/build.gradle.kts`, `core/build.gradle.kts` |

**`[AMENDED 2026-03-31]:`** The SBOM previously listed Gradle `9.6.0-nightly-20260322000231+0000`. A screenshot referenced `9.6.0-202603311012943+0000`; that snapshot is **not** published on `services.gradle.org` (404). The wrapper was updated to **`9.6.0-20260331012943+0000`** (2026-03-31 snapshot).

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
| `GameDatabase` (Room) | `version = 2`; `MIGRATION_1_2` adds `lastOfflineTickAppliedAt` on `game_meta` | `app/.../data/game/GameDatabase.kt` |
| `ProfileDatabase` (Room) | `version = 1` | `app/.../data/profile/ProfileDatabase.kt` |

**`[AMENDED 2026-03-31]:`** Game persistence schema bumped to **v2** for Phase 4 (offline audit column). Existing installs migrate on upgrade via `addMigrations(GameDatabase.MIGRATION_1_2)` in `ArteriaApp`.

**`[AMENDED 2026-04-01]:`** `:app` enables **`buildConfig = true`** (`app/build.gradle.kts`) so UI can read `com.arteria.game.BuildConfig` for version labels.

---

## Declared Dependencies

### `:app` module

| Scope | Coordinates | Installed | Next available |
|-------|-------------|-----------|----------------|
| `implementation` | `androidx.core:core-ktx` | `1.18.0` | `1.18.0` (current stable) |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-ktx` | `2.10.0` | `2.11.0-alpha` |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-compose` | `2.10.0` | `2.11.0-alpha` |
| `implementation` | `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.10.0` | `2.11.0-alpha` |
| `implementation` | `androidx.activity:activity-compose` | `1.13.0` | `1.13.0` (current stable) |
| `implementation` | `androidx.datastore:datastore-preferences` | `1.2.0` | `verify on monthly pass` |
| `implementation` | `androidx.compose:compose-bom` | `platform(2026.03.01)` | `next monthly BOM` |
| `implementation` | `androidx.compose.ui:ui` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.ui:ui-graphics` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.ui:ui-tooling-preview` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.material:material-icons-core` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.material3:material3` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.navigation:navigation-compose` | `2.9.7` | `2.10.0-alpha` |
| `implementation` | `androidx.room:room-runtime` | `2.8.4` | `3.0.0-alpha01` |
| `implementation` | `androidx.room:room-ktx` | `2.8.4` | `3.0.0-alpha01` |
| `ksp` | `androidx.room:room-compiler` | `2.8.4` | `3.0.0-alpha01` |
| `testImplementation` | `junit:junit` | `4.13.2` | `4.13.2` |
| `testImplementation` | `org.jetbrains.kotlinx:kotlinx-coroutines-test` | `1.10.2` | `1.10.2` |
| `androidTestImplementation` | `androidx.test.ext:junit` | `1.3.0` | `1.3.0` |
| `androidTestImplementation` | `androidx.test.espresso:espresso-core` | `3.7.0` | `3.7.0` |
| `androidTestImplementation` | `androidx.room:room-testing` | `2.8.4` | `3.0.0-alpha01` |
| `androidTestImplementation` | `androidx.compose:compose-bom` | `platform(2026.03.01)` | `next monthly BOM` |
| `androidTestImplementation` | `androidx.compose.ui:ui-test-junit4` | `via BOM` | `via next BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-tooling` | `via BOM` | `via next BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-test-manifest` | `via BOM` | `via next BOM` |

### `:core` module

| Scope | Coordinates | Installed | Next available |
|-------|-------------|-----------|----------------|
| `implementation` | `androidx.core:core-ktx` | `1.18.0` | `1.18.0` (current stable) |
| `testImplementation` | `junit:junit` | `4.13.2` | `4.13.2` |

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
| 2026-03-31 | Dependency update sweep vs pinned Gradle files | No dependency coordinate changes applied; current pins retained because available newer lines are primarily alpha/nightly and not required for active feature slice |
| 2026-03-31 | Coordinate-level review after Kotlin/dependency bump | Updated to Kotlin Compose `2.3.20`, KSP `2.3.6`, Compose BOM `2026.03.01`, Room `2.8.4`; no new non-AndroidX third-party dependencies introduced |
| 2026-03-30 | Coordinate-level review of declared deps | No known high-risk third-party dependencies introduced; stack is AndroidX/Google/Kotlin ecosystem plus JUnit |

---

## Update Procedure

1. Read live declarations from `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, and `core/build.gradle.kts`.
2. Update this SBOM in the same change as dependency edits.
3. Run verification: `./gradlew :app:assembleDebug` and relevant test tasks.
4. **`[AMENDED 2026-03-31]:`** If user explicitly instructs to skip tests, log that waiver in `DOCS/SCRATCHPAD.md` in the same session.
