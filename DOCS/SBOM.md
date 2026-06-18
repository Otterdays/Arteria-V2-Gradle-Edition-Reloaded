<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-06-18 | Cursor Agent | Composer | **Stable dependency bump pass:** AGP **9.2.1**, KSP **2.3.9**, Gradle **9.6.0-20260617124657+0000**, Compose BOM **2026.05.01**, Lifecycle **2.11.0**, Navigation **2.9.8**, `core-ktx` **1.19.0**, Datastore **1.2.1**, coroutines-test **1.11.0**; app **1.10.5 (18)** + changelog/README; `:app:compileDebugKotlin` verified. |
| 2026-06-18 | Cursor Agent | Composer | **June 2026 dependency review (no-bump):** Live Gradle pins verified vs Google Maven / JetBrains / Gradle services; refreshed **Next available** columns + update-channel map; **Installed** unchanged (`app` **1.10.4** / `versionCode` **17**). |
| 2026-04-29 | Composer | GPT-5.2 | **Equipment expansion (SBOM bump):** `GameDatabase` **v6** + `MIGRATION_5_6` (`equippedHead`, `equippedRing`, `equippedRing2`); changelog + SCRATCHPAD handoff appended; no Maven coordinate edits. |
| 2026-04-29 | Codex | GPT-5 | **SBOM audit/sync:** Reconciled `DOCS/SBOM.md` with live Gradle + `GameDatabase.kt`: AGP **9.2.0** (was stale 9.1.0); toolchain “Next available” for AGP amended; `GameDatabase` **v5** + `MIGRATION_2_3`–`MIGRATION_4_5` documented; release `isMinifyEnabled` noted; appended security snapshot row. |
| 2026-04-01 | Cursor Agent | Composer | **Phase 2 close:** `:core` converted to JVM Kotlin (`kotlin("jvm")` 2.3.20); engine moved from `app/.../core` to `core/src/main/kotlin`; `TickEngineTest` + `XPTableTest`; `app` → `implementation(project(":core"))`; root `org.jetbrains.kotlin.jvm` apply false; `:core:test` + `:app:testDebugUnitTest` green. |
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

> Last updated: 2026-06-18 (stable dependency bump pass — coordinates applied + verified compile)
> App release (player-facing): **`versionName` 1.10.5** / **`versionCode` 18** (`app/build.gradle.kts`)
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
| Gradle wrapper | `9.6.0-20260617124657+0000` | `nightly` | `2026-07-18` |
| AGP | `9.2.1` | `stable` | `2026-07-18` |
| Kotlin Compose plugin | `2.3.20` | `stable` | `2026-07-18` |
| KSP plugin | `2.3.9` | `stable` | `2026-07-18` |
| Compose BOM | `2026.05.01` | `stable` (monthly BOM) | `2026-07-18` |
| Lifecycle / Navigation / Room track | `2.11.0` / `2.9.8` / `2.8.4` | `stable` | `2026-07-18` |
| Daemon JDK | `21 (ADOPTIUM)` | `pinned` | `2026-07-18` |

**`[AMENDED 2026-06-18 — bump applied]:`** June review recommendations **installed** in Gradle: AGP **9.2.1**, KSP **2.3.9**, Gradle **9.6.0-20260617124657+0000**, Compose BOM **2026.05.01**, Lifecycle **2.11.0**, Navigation **2.9.8**, `core-ktx` **1.19.0**, Datastore **1.2.1**, coroutines-test **1.11.0**. **Held:** Kotlin **2.4.0**, Room **3.0.0-rc01**.

**`[AMENDED 2026-06-18]:` June dependency review (no-bump pass):**
- **Installed** pins match live `build.gradle.kts` / wrapper — no Gradle file edits this session.
- **Stable upgrades available (low-risk patch/minor):** AGP **9.2.1**; KSP **2.3.9**; Compose BOM **2026.05.01**; `core-ktx` **1.19.0**; `navigation-compose` **2.9.8**; `datastore-preferences` **1.2.1**; `kotlinx-coroutines-test` **1.11.0**; Gradle nightly **9.6.0-20260617124657+0000** (optional).
- **Stable upgrades available (minor, verify matrix):** Lifecycle **2.11.0** (stable 2026-06-17; Compose `compileSdk` path expects AGP **≥ 9.2.0** — already satisfied).
- **Hold / verify before bump:** Kotlin **2.4.0** (latest stable; KSP **2.4.x** pairing still open — google/ksp#2965); Room **3.0.0-rc01** (`androidx.room3` — breaking package/API vs `androidx.room` **2.8.4** maintenance line).
- **Docs-only / not in Maven:** Compose BOM **2026.06.00** appears on BOM-mapping docs but **failed resolution** on Google Maven as of 2026-06-18 — treat **2026.05.01** as latest resolvable stable BOM.
- **Already current stable:** `activity-compose` **1.13.0**; Room **2.8.4** (2.x line); JUnit **4.13.2**; `androidx.test.ext:junit` **1.3.0**; Espresso **3.7.0**.

**`[AMENDED 2026-03-31]:` Dependency sweep result (no-bump pass):**
- `AGP 9.1.0` remains the stable baseline in this repo; `9.2.x` is alpha track.
- Lifecycle, Navigation, and Room have newer alpha lines available, but no required stable migration for current Phase 5 work.
- Compose BOM remains on monthly stable cadence (`2026.03.01` currently pinned); next review on the scheduled monthly window.
- No dependency edits were applied in this sweep.

**`[AMENDED 2026-04-29]:`** The **2026-03-31** sweep bullet about `AGP 9.1.0` was accurate **at that date**. The repo now pins **`AGP 9.2.0`** in root `build.gradle.kts` — treat the **Update channel map** + **Build Toolchain** rows as current **Installed** truth; keep the sweep block as historical context.

---

## Build Toolchain

| Component | Installed | Next available | Source |
|-----------|-----------|----------------|--------|
| Gradle wrapper | `9.6.0-20260617124657+0000` | `9.6.0-rc-3` (RC); rolling nightly | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin | `9.2.1` | `9.3.0-rc01` (preview) | root `build.gradle.kts` |
| Compose plugin | `org.jetbrains.kotlin.plugin.compose:2.3.20` | `2.4.0` (stable); `2.3.21` (2.3.x bugfix) | root `build.gradle.kts` |
| Kotlin JVM plugin | `org.jetbrains.kotlin.jvm:2.3.20` (`apply false`) | `2.4.0` (stable); `2.3.21` (2.3.x bugfix) | root `build.gradle.kts` — used by `:core` |
| KSP plugin | `com.google.devtools.ksp:2.3.9` | `2.3.9` (current stable); verify before Kotlin **2.4.0** | root `build.gradle.kts` |
| Foojay toolchain resolver | `org.gradle.toolchains.foojay-resolver-convention:1.0.0` | `verify before bump` | `settings.gradle.kts` |
| Daemon JVM toolchain | `JDK 21` / vendor `ADOPTIUM` | `JDK 26` (local experimental path) | `gradle/gradle-daemon-jvm.properties` |
| Java bytecode target | `JavaVersion.VERSION_21` (`:app`); JVM **21** toolchain (`:core`) | `VERSION_26` (only when AGP/Kotlin matrix supports) | `app/build.gradle.kts`; `core/build.gradle.kts` (`kotlin { jvmToolchain(21) }`) |

**`[AMENDED 2026-03-31]:`** The SBOM previously listed Gradle `9.6.0-nightly-20260322000231+0000`. A screenshot referenced `9.6.0-202603311012943+0000`; that snapshot is **not** published on `services.gradle.org` (404). The wrapper was updated to **`9.6.0-20260331012943+0000`** (2026-03-31 snapshot).

---

## Android Targets

| Property | Value | Source |
|----------|-------|--------|
| `applicationId` | `com.arteria.game` | `app/build.gradle.kts` |
| `namespace` (`:app`) | `com.arteria.game` | `app/build.gradle.kts` |
| `namespace` (`:core`) | *(not applicable — `:core` is JVM Kotlin, not Android)* | **`[AMENDED 2026-04-01]:`** prior Android-library `namespace` row superseded by JVM module. |
| `compileSdk` | `37` | `app/build.gradle.kts` only **`[AMENDED 2026-06-18]:`** bumped from **36.1** — required by `core-ktx` **1.19.0** + Lifecycle **2.11.0** AAR metadata; **`[AMENDED 2026-04-01]:`** `:core` no longer uses `compileSdk`. |
| `targetSdk` | `36` | `app/build.gradle.kts` **`[AMENDED 2026-06-18]:`** unchanged — compile **37** / target **36** until Android 17 runtime opt-in. |
| `minSdk` | `26` | `app/build.gradle.kts` only **`[AMENDED 2026-04-01]:`** `:core` is not an Android module. |
| `GameDatabase` (Room) | `version = 2`; `MIGRATION_1_2` adds `lastOfflineTickAppliedAt` on `game_meta` | `app/.../data/game/GameDatabase.kt` |
| `GameDatabase` (Room) — **current** | **`[AMENDED 2026-04-29]:`** `version = 6`; **`MIGRATION_1_2` … `MIGRATION_5_6`** (adds **`equippedHead` / `equippedRing` / `equippedRing2`** on **`game_meta`**; prior migrations cover offline audit + equipment/companion + resonance + encounter combat columns) | `app/.../data/game/GameDatabase.kt`; `ArteriaApp.kt` |
| `ProfileDatabase` (Room) | `version = 1` | `app/.../data/profile/ProfileDatabase.kt` |
| `buildTypes.release` | `isMinifyEnabled = true` (R8/minify + `proguard-android-optimize.txt` + `proguard-rules.pro`) | `app/build.gradle.kts` |

**`[AMENDED 2026-03-31]:`** Game persistence schema bumped to **v2** for Phase 4 (offline audit column). Existing installs migrate on upgrade via `addMigrations(GameDatabase.MIGRATION_1_2)` in `ArteriaApp`.

**`[AMENDED 2026-04-29 — equipment loadout expansion]:`** `GameDatabase` is now **version 6** (`GameDatabase.kt`); `ArteriaApp.kt` additionally registers **`MIGRATION_5_6`** which adds **`equippedHead`**, **`equippedRing`**, **`equippedRing2`** nullable columns on `game_meta`.

**`[SUPERSEDED 2026-04-29 — ROOM NOW v6]:`** The paragraph claiming **`GameDatabase` version 5** only described the pre-expansion milestone; **`MIGRATION_5_6`** + version **6** are now live (`GameDatabase.kt`, `ArteriaApp.kt`). Preserve this line as migration-history context only — authoritative level is the **Android Targets** **`GameDatabase` (Room) — current** row.

**`[AMENDED 2026-04-29 — historical milestone text]:`** `GameDatabase` reached **version 5** when **`MIGRATION_4_5`** landed (`ArteriaApp.kt` registrations through **`MIGRATION_4_5`** covered equipment/companion, resonance, and encounter combat persistence). The v2 isolated paragraph remains the **offline-audit milestone** narrative.

**`[AMENDED 2026-04-01]:`** `:app` enables **`buildConfig = true`** (`app/build.gradle.kts`) so UI can read `com.arteria.game.BuildConfig` for version labels.

---

## Declared Dependencies

### `:app` module

| Scope | Coordinates | Installed | Next available |
|-------|-------------|-----------|----------------|
| `implementation` | `project(":core")` | workspace module | n/a |
| `implementation` | `androidx.core:core-ktx` | `1.19.0` | `1.19.0` (current stable) |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-ktx` | `2.11.0` | `2.11.0` (current stable) |
| `implementation` | `androidx.lifecycle:lifecycle-runtime-compose` | `2.11.0` | `2.11.0` (current stable) |
| `implementation` | `androidx.lifecycle:lifecycle-viewmodel-compose` | `2.11.0` | `2.11.0` (current stable) |
| `implementation` | `androidx.activity:activity-compose` | `1.13.0` | `1.13.0` (current stable) |
| `implementation` | `androidx.datastore:datastore-preferences` | `1.2.1` | `1.2.1` (current stable) |
| `implementation` | `androidx.compose:compose-bom` | `platform(2026.05.01)` | `2026.05.01` (current stable; `2026.06.00` docs-only / not in Maven) |
| `implementation` | `androidx.compose.ui:ui` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.ui:ui-graphics` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.ui:ui-tooling-preview` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.material:material-icons-core` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.compose.material3:material3` | `via BOM` | `via next BOM` |
| `implementation` | `androidx.navigation:navigation-compose` | `2.9.8` | `2.9.8` (current stable); `2.10.0-alpha05` |
| `implementation` | `androidx.room:room-runtime` | `2.8.4` | `2.8.4` (2.x stable); `androidx.room3:3.0.0-rc01` (breaking) |
| `implementation` | `androidx.room:room-ktx` | `2.8.4` | `2.8.4` (2.x stable); `androidx.room3:3.0.0-rc01` (breaking) |
| `ksp` | `androidx.room:room-compiler` | `2.8.4` | `2.8.4` (2.x stable); `androidx.room3:3.0.0-rc01` (breaking) |
| `testImplementation` | `junit:junit` | `4.13.2` | `4.13.2` |
| `testImplementation` | `org.jetbrains.kotlinx:kotlinx-coroutines-test` | `1.11.0` | `1.11.0` (current stable) |
| `androidTestImplementation` | `androidx.test.ext:junit` | `1.3.0` | `1.3.0` |
| `androidTestImplementation` | `androidx.test.espresso:espresso-core` | `3.7.0` | `3.7.0` |
| `androidTestImplementation` | `androidx.room:room-testing` | `2.8.4` | `3.0.0-alpha01` |
| `androidTestImplementation` | `androidx.compose:compose-bom` | `platform(2026.05.01)` | `2026.05.01` (current stable) |
| `androidTestImplementation` | `androidx.compose.ui:ui-test-junit4` | `via BOM` | `via next BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-tooling` | `via BOM` | `via next BOM` |
| `debugImplementation` | `androidx.compose.ui:ui-test-manifest` | `via BOM` | `via next BOM` |

### `:core` module (JVM Kotlin)

| Scope | Coordinates | Installed | Next available |
|-------|-------------|-----------|----------------|
| `plugins` | `org.jetbrains.kotlin.jvm` | `2.3.20` | `2.4.0` (stable); `2.3.21` (2.3.x bugfix) |
| `testImplementation` | `junit:junit` | `4.13.2` | `4.13.2` |

**`[AMENDED 2026-04-01]:`** Replaced prior Android-library `:core` stub (`com.android.library`, `androidx.core:core-ktx`) with headless JVM library — engine sources under `core/src/main/kotlin/com/arteria/game/core/`, tests under `core/src/test/kotlin/`.

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
| 2026-04-29 | SBOM doc vs live Gradle + Room | Reconciled stale **AGP 9.1.0** / **GameDatabase v2-only** narrative: **AGP 9.2.0**, **Room v5** + migration chain, release minify row; dependency coordinates unchanged vs `app/build.gradle.kts` |
| 2026-04-29 | Schema touchpoint — expanded equipment persistence | Verified **`game_meta`** expands via **`MIGRATION_5_6`** (Room **`GameDatabase` version 6**); no Maven coordinate changes bundled with this schema bump |
| 2026-06-18 | Stable dependency bump pass | Applied AGP **9.2.1**, KSP **2.3.9**, Gradle **9.6.0-20260617**, Compose BOM **2026.05.01**, Lifecycle **2.11.0**, Navigation **2.9.8**, `core-ktx` **1.19.0**, Datastore **1.2.1**, coroutines-test **1.11.0**; `:app:compileDebugKotlin` green; Kotlin **2.4.0** + Room **3.0** intentionally held |

**`[AMENDED 2026-04-29 — supersession note]:`** The **SBOM doc vs live Gradle + Room** row predates **`MIGRATION_5_6`** / **`equippedHead` / `equippedRing` / `equippedRing2`**. Treat the **`Schema touchpoint`** row + **Android Targets** **`GameDatabase` (Room) — current** cell as authoritative for **`GameDatabase` version 6**; keep chronological rows unmigrated for audit readability.

---

## Update Procedure

1. Read live declarations from `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, and `core/build.gradle.kts`.
2. Update this SBOM in the same change as dependency edits.
3. Run verification: `./gradlew :app:assembleDebug` and relevant test tasks.
4. **`[AMENDED 2026-03-31]:`** If user explicitly instructs to skip tests, log that waiver in `DOCS/SCRATCHPAD.md` in the same session.
