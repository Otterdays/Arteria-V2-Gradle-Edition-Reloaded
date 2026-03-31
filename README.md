# Arteria Gradle Edition V2

> **Native Android Idle RPG** — Kotlin + Jetpack Compose on cutting-edge tooling. Parallel native product to the main Expo/React Native app.

```
   ___    ____  ________  ____  ___
  / _ |  / __ \/  _/_  __/ __ \/   |
 / __ | / /_/ // /  / / / /_/ / /| |
/ ___ |/ _, _// /  / / / _, _/ ___ |
/_/  |_/_/ |_/___/ /_/ /_/ |_/_/  |_|

      Gradle Edition V2 — Native Track
```

---

## What is Arteria?

**Arteria** is a feature-rich **idle RPG** for Android. Players manage skills, craft items, engage in combat, and progress through a game world — all while the game ticks forward even when closed. The main game runs on **Expo** (React Native); this folder is a **parallel native Android** implementation targeting the latest Gradle and Compose tooling.

### Key Features

- **Idle Mechanics** — Offline progression, skill ticks, and resource generation
- **Account & Profile Persistence** — Room-based save/load with session management
- **Docking Station UI** — Beautiful character selection with animated space backdrop
- **Jetpack Compose** — Modern declarative UI with Material Design 3
- **JDK 21 & Gradle 9.6** — Bleeding-edge tooling for maximum platform adoption
- **Future GPU Island** — C++ / OpenGL ES 3 planned for advanced rendering (Phase 5+)

---

## Quick Start

### Prerequisites

- **Android Studio** 2024.1 or later
- **JDK 21** (auto-provisioned via Gradle Foojay toolchain)
- **Android SDK 36.1** (API 36 Baklava, automatically installed by Studio)
- **Gradle 9.6 nightly** (pinned in `gradle/wrapper/gradle-wrapper.properties`)

### Build and Run

```bash
# Open this folder in Android Studio
#   File > Open > .../Arteria-V2-Gradle-Edition-Reloaded/.claude/worktrees/naughty-banzai

# Or, build from command line
./gradlew :app:assembleDebug      # Build APK
./gradlew :app:installDebug       # Install to device/emulator
./gradlew :app:testDebugUnitTest  # Run unit tests

# Verify Room persistence tests
./gradlew :app:connectedAndroidTest
```

### First Run

1. Start the Android emulator (API 36 or later recommended)
2. Run `:app:assembleDebug` → `:app:installDebug`
3. Launch **Arteria** on the emulator
4. Create a new account and observe the persistent account selection screen
5. Explore the docking station with animated nebula background

---

## Architecture Overview

### Current State (Phases 0–1)

The app is a **Jetpack Compose shell** with persistent profile management:

```
┌─────────────────────────────────────────────────┐
│         Android OS (API 26–36)                   │
├─────────────────────────────────────────────────┤
│  MainActivity.kt  (ComponentActivity)            │
│    ↓ (Compose)                                  │
│  ArteriaApp (NavHost)                           │
│    ├── AccountSelectionScreen (with animation)  │
│    ├── AccountCreationScreen                    │
│    ├── PlayPlaceholderScreen                    │
│    └── [future: Skills, Bank, Combat, Settings]│
├─────────────────────────────────────────────────┤
│  Room Database (Profile Persistence)            │
│    └── ProfileEntity, ProfileDao, ProfileDB     │
├─────────────────────────────────────────────────┤
│  :core Module (empty library — future engine)   │
└─────────────────────────────────────────────────┘
```

**Key Technologies:**

| Component | Version | Purpose |
|-----------|---------|---------|
| **Gradle** | 9.6.0-nightly | Build system |
| **AGP** | 9.1.0 | Android Gradle Plugin |
| **Kotlin** | (bundled in AGP 9.1) | Language (JVM bytecode targets Java 21) |
| **Jetpack Compose** | 2024.06.00 (BOM) | Declarative UI |
| **Navigation Compose** | 2.8.0 | Screen routing |
| **Room** | 2.6.1 | Local persistence |
| **JDK** | 21 (ADOPTIUM) | Daemon + bytecode target |

### Future Architecture (Phases 2–6+)

Once engine logic and UI shells complete, an optional **GPU island** can be added:

```
C++ / OpenGL ES 3 layer (GPU island)
  ├─ Renderer (EGL + GLES 3 context)
  ├─ Shader system
  ├─ Model + Texture pipeline
  └─ Motion/key event handlers
     ↕ (Compose can composite over GL surface)
```

See **[ARCHITECTURE.md](DOCS/ARCHITECTURE.md)** for detailed system design and rendering roadmap.

---

## Project Structure

```
Arteria-Gradle-Edition-V2/
├── README.md (this file)
├── settings.gradle.kts          # Gradle module declaration
├── build.gradle.kts             # Root Gradle config
├── gradle.properties            # Gradle properties
├── gradle/
│   ├── wrapper/
│   │   └── gradle-wrapper.properties  # Pinned Gradle 9.6 nightly URL
│   └── gradle-daemon-jvm.properties   # JDK 21 + ADOPTIUM config
│
├── app/                          # Main application module
│   ├── build.gradle.kts
│   ├── src/main/
│   │   ├── java/com/arteria/game/
│   │   │   ├── MainActivity.kt         # Entry point (ComponentActivity)
│   │   │   ├── ui/
│   │   │   │   ├── ArteriaApp.kt       # NavHost root
│   │   │   │   ├── account/            # Account selection/creation/session
│   │   │   │   ├── components/         # Docking UI, backgrounds
│   │   │   │   └── theme/              # Material 3 + Cinzel typography
│   │   │   ├── navigation/
│   │   │   │   └── NavRoutes.kt        # Route definitions + payload types
│   │   │   ├── data/profile/           # Room stack
│   │   │   │   ├── ProfileEntity.kt
│   │   │   │   ├── ProfileDao.kt
│   │   │   │   ├── ProfileDatabase.kt
│   │   │   │   ├── ProfileRepository.kt
│   │   │   │   └── RoomProfileRepository.kt
│   │   │   └── ...
│   │   ├── res/
│   │   │   ├── values/colors.xml       # Theme colors
│   │   │   ├── font/
│   │   │   │   └── cinzel.ttf          # Display font (OFL license)
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   ├── src/test/                       # JVM unit tests
│   │   └── com/arteria/game/...
│   └── src/androidTest/                # Device/emulator tests
│       └── com/arteria/game/...
│
├── core/                         # Library module (empty, future engine)
│   └── build.gradle.kts
│
├── DOCS/                         # Documentation hub
│   ├── SUMMARY.md                # Start here — AI handoff guide
│   ├── SCRATCHPAD.md             # Live status, blockers, next actions
│   ├── ROADMAP.md                # Phased delivery (Phases 0–10)
│   ├── ARCHITECTURE.md           # System design deep-dive
│   ├── MIGRATION_SPEC.md         # TS/RN → Kotlin mapping guide
│   ├── SBOM.md                   # Dependency inventory + versions
│   ├── REFERENCES.md             # External documentation links
│   └── ARTERIA-V1-DOCS/          # Read-only mirror of main repo docs
│
└── build-apk-for-transfer.*      # Helper scripts for APK builds
```

---

## Documentation Roadmap

The project maintains **extensive internal documentation** designed for AI-assisted development:

| Document | Purpose | Audience |
|----------|---------|----------|
| **[DOCS/SUMMARY.md](DOCS/SUMMARY.md)** | Hub & AI handoff guide | New developers & AI agents |
| **[DOCS/SCRATCHPAD.md](DOCS/SCRATCHPAD.md)** | Live status, blockers, next actions | Current & next developer |
| **[DOCS/ROADMAP.md](DOCS/ROADMAP.md)** | Full phased plan (Phases 0–10) | Project leads & planners |
| **[DOCS/ARCHITECTURE.md](DOCS/ARCHITECTURE.md)** | System design, render pipeline, module inventory | Architects & engineers |
| **[DOCS/MIGRATION_SPEC.md](DOCS/MIGRATION_SPEC.md)** | How to port React Native / TypeScript to Kotlin | Backend/engine developers |
| **[DOCS/SBOM.md](DOCS/SBOM.md)** | Dependency inventory, versions, upgrade policy | DevOps & maintainers |
| **[DOCS/REFERENCES.md](DOCS/REFERENCES.md)** | External official documentation | All |

### Start Here

**New to this project?** Read in this order:

1. [DOCS/SUMMARY.md](DOCS/SUMMARY.md) — 2 min overview + canonical paths
2. [DOCS/ROADMAP.md](DOCS/ROADMAP.md) — Understand the plan (skim phases 0–1, then 7–10)
3. [DOCS/ARCHITECTURE.md](DOCS/ARCHITECTURE.md) — System structure
4. [DOCS/MIGRATION_SPEC.md](DOCS/MIGRATION_SPEC.md) — If porting engine code
5. [DOCS/SBOM.md](DOCS/SBOM.md) — If changing dependencies

---

## Development Workflow

### UI / Frontend Features (Phases 1, 3, 7–8)

1. **Consult game design truth:**
   - Main repo `DOCU/MASTER_DESIGN_DOC.md` for feature specs
   - `DOCU/IMPROVEMENTS.md` for UX/QoL priorities
   - `DOCU/THEMING.md` or `zhip-ai-styling.md` for visual language

2. **Mirror React Native screens:**
   - Reference `apps/mobile/app/` layout/component patterns (not code copy, UX intent)
   - Align colors + typography with `apps/mobile/constants/theme.ts`
   - Use Compose equivalents (Buttons, TextFields, LazyColumn, etc.)

3. **Update this README and DOCS as you work**

### Engine / Backend Features (Phases 2, 4–5)

1. **Port TypeScript logic to Kotlin JVM:**
   - See [MIGRATION_SPEC.md](DOCS/MIGRATION_SPEC.md) for patterns
   - Primary sources: `packages/engine/src/` (TS) and `packages/data/src/` (content)

2. **Write tests in `:core`:**
   - No Android imports — run on JVM via `./gradlew :core:test`
   - Mirror test structure from `packages/engine/src/__tests__/`

3. **Integrate with UI:**
   - Pass engine state via `ViewModel` + `StateFlow` to Compose
   - See `AccountViewModel.kt` for the pattern

### Gradle & Dependency Updates

1. **Before changing Gradle or SDKs:**
   - Read [SBOM.md](DOCS/SBOM.md) "Update Policy"
   - Update the doc after the change
   - Run `./gradlew --version` + `:app:assembleDebug` + tests to verify

2. **Dependency upgrades:**
   - Compose BOM → bump version in `app/build.gradle.kts`, then bump AndroidX libs to match
   - See [BOM mapping](https://developer.android.com/develop/ui/compose/bom/bom-mapping)
   - Run `./gradlew :app:assembleDebug` + tests

---

## Contributing

### Code Style

- **Kotlin:** Follow [Google's Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- **Compose:** Use [Material Design 3](https://m3.material.io/) tokens and components
- **Tests:** JVM unit tests in `:core`, Android-specific tests in `:app:androidTest`

### Commit Messages

- Use clear, descriptive messages (e.g., "feat: add profile persistence with Room", "fix: correct DockingBackground animation timing")
- Reference issue numbers or feature phases if applicable (e.g., "Phase 2: port TickSystem engine logic")

### Documentation Updates

Every commit touching code should note relevant changes in:

- **[DOCS/SCRATCHPAD.md](DOCS/SCRATCHPAD.md)** — Add to "Last Actions" if significant
- **[DOCS/SBOM.md](DOCS/SBOM.md)** — If dependencies changed
- **This README** — If user-facing features or setup changed

---

## Build Verification Checklist

Before pushing or submitting a pull request:

```bash
# 1. Build the app
./gradlew :app:assembleDebug

# 2. Run unit tests (JVM)
./gradlew :app:testDebugUnitTest
./gradlew :core:test

# 3. Install to emulator/device
./gradlew :app:installDebug

# 4. (Optional) Run instrumented tests
./gradlew :app:connectedAndroidTest

# 5. (Optional) Lint check
./gradlew :app:lint
```

---

## Troubleshooting

### Gradle Daemon Issues

**Error:** `The daemon is disabled.` or JDK version mismatch

**Solution:**
```bash
# Clear Gradle caches
./gradlew --stop
rm -rf ~/.gradle/daemon/

# Verify JDK 21 in use
./gradlew --version

# If wrong JDK, set JAVA_HOME or use foojay auto-provisioning:
# (Already configured in gradle/gradle-daemon-jvm.properties)
```

### Compose / Material3 Errors

**Error:** `Cannot find symbol: class Material3`

**Solution:**
```bash
# Sync Gradle and refresh IDE
File > Sync Now (or ./gradlew clean :app:build)

# Verify BOM version in app/build.gradle.kts matches [SBOM.md](DOCS/SBOM.md)
```

### Device/Emulator Issues

**No devices available:**
- Open Android Studio → Device Manager
- Create or start an API 36 emulator
- For physical devices: `adb devices` should list them

**APK won't install:**
```bash
./gradlew clean :app:assembleDebug  # Clean build
./gradlew :app:installDebug
```

### Room Database Errors

**Error:** `table not found` on first run

**Solution:**
- Room uses `@Database(version = 1)` with auto-creation
- Check `ProfileDatabase.kt` matches `ProfileEntity` schema
- For migrations, increment `version` and add `Migration` in `openBuilder()`

---

## License & Credits

### Project License

Arteria is developed by [Your Name/Organization]. See `LICENSE.md` (if present) for full terms.

### Third-Party Licenses

- **Cinzel font:** [SIL Open Font License 1.1](https://opensource.org/licenses/OFL-1.1) — bundled in `app/src/main/res/font/cinzel.ttf` from [Google Fonts](https://github.com/google/fonts/tree/main/ofl/cinzel)
- **Jetpack Compose:** Apache 2.0 — [androidx.compose](https://developer.android.com/jetpack/androidx/compose)
- **Room:** Apache 2.0 — [androidx.room](https://developer.android.com/training/data-storage/room)
- See [DOCS/SBOM.md](DOCS/SBOM.md) for full dependency list

### Main Arteria Project

This is **V2**, a native Android fork. The primary game runs on **React Native / Expo**:

- **Main repo:** [https://github.com/your-org/Arteria](https://github.com/your-org/Arteria) (update this link)
- **Game design docs:** See `DOCU/` in the main repo root
- **React Native app:** `apps/mobile/` in main repo

---

## Quick Links

- **[Android Developer Docs](https://developer.android.com/)**
- **[Jetpack Compose Docs](https://developer.android.com/jetpack/compose)**
- **[Gradle Build Tool](https://gradle.org/)**
- **[Kotlin Reference](https://kotlinlang.org/docs/home.html)**
- **[Android Gradle Plugin 9.1 Release Notes](https://developer.android.com/build/releases/agp-9-1-0-release-notes)**
- **[Room Persistence Library](https://developer.android.com/training/data-storage/room)**

---

## Support & Feedback

For issues, questions, or feature requests:

1. **Check [DOCS/SCRATCHPAD.md](DOCS/SCRATCHPAD.md)** for known blockers
2. **Read [DOCS/ROADMAP.md](DOCS/ROADMAP.md)** to see if it's planned
3. **File an issue** (if using GitHub) with:
   - Device / emulator API level
   - Gradle and JDK versions (`./gradlew --version`)
   - Build error or reproduction steps
   - Relevant log output

---

**Last Updated:** 2026-03-30
**Version:** 1.0 (Gradle Edition V2, Phases 0–1 complete)
**Status:** Compose shell + Room persistence — ready for Phase 2 (Engine port)

