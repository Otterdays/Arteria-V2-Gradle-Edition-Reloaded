# Arteria V2 · Gradle Edition Reloaded

<p align="center">
  <strong>Native Android idle RPG</strong> — offline ticks, Room persistence, and a Compose UI that does not apologize for looking good.
</p>

## Project Canon

- **Canonical local root:** `Arteria-V2-Gradle-Edition-Reloaded/`
- **Legacy alias:** `Arteria-Gradle-Edition-V2/` (same project identity in older docs)
- **Stack truth:** Native Android only (`Kotlin + Compose + Room + Gradle/AGP`)
- **Source-of-truth docs:** `DOCS/SUMMARY.md` → `DOCS/SBOM.md` → `DOCS/SCRATCHPAD.md` → `DOCS/ARCHITECTURE.md` → `DOCS/ROADMAP.md`

<div align="center">

```
   ___    ____  ________  ____  ___
  / _ |  / __ \/  _/_  __/ __ \/   |
 / __ | / /_/ // /  / / / /_/ / /| |
/ ___ |/ _, _// /  / / / _, _/ ___ |
/_/  |_/_/ |_/___/ /_/ /_/ |_/_/  |_|
```

*Gradle Edition V2 — native track · bleeding-edge toolchain, ship-shaped defaults*

<br/>

**Build & language**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-9.6.0--snapshot-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![AGP](https://img.shields.io/badge/AGP-9.1.0-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/build/releases/gradle-plugin)
[![Compose BOM](https://img.shields.io/badge/Compose_BOM-2026.03.01-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose/bom)
[![KSP](https://img.shields.io/badge/KSP-2.3.6-7F52FF?style=for-the-badge)](https://github.com/google/ksp)
[![Room](https://img.shields.io/badge/Room-2.8.4-0EA5E9?style=for-the-badge&logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)

**JDK · Android targets · libraries**

[![Build JDK](https://img.shields.io/badge/Build-JDK_26-FF6B00?style=for-the-badge&logo=openjdk&logoColor=white)](#-quick-start)
[![JVM target](https://img.shields.io/badge/JVM_target-21-437291?style=for-the-badge&logo=openjdk&logoColor=white)](app/build.gradle.kts)
[![Compile SDK](https://img.shields.io/badge/compileSdk-36.1-3DDC84?style=for-the-badge&logo=android&logoColor=white)](app/build.gradle.kts)
[![minSdk](https://img.shields.io/badge/minSdk-26-3DDC84?style=for-the-badge&logo=android&logoColor=white)](app/build.gradle.kts)
[![Navigation](https://img.shields.io/badge/Navigation-2.9.7-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/navigation)
[![Lifecycle](https://img.shields.io/badge/Lifecycle-2.10.0-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/lifecycle)
[![Coroutines](https://img.shields.io/badge/Coroutines-1.10.2-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://github.com/Kotlin/kotlinx.coroutines)

**Product · status**

[![App version](https://img.shields.io/badge/release-1.4.1-FF6B35?style=for-the-badge)](app/build.gradle.kts)
[![Platform](https://img.shields.io/badge/Android-16_%28API_36%29-3DDC84?style=for-the-badge&logo=android&logoColor=white)](app/build.gradle.kts)
[![Material 3](https://img.shields.io/badge/UI-Material_3-6750A4?style=for-the-badge)](https://m3.material.io/)
[![Status](https://img.shields.io/badge/Status-Phase_5_slice-FF6B35?style=for-the-badge)](DOCS/ROADMAP.md)

</div>

---

## ✨ What is Arteria?

**Arteria** is a feature-rich **idle RPG** for Android. Players manage skills, craft items, engage in combat, and progress through a game world — all while the game ticks forward even when closed. This repository is the **native Android track** and the source of truth for current implementation work.

### 🎮 Key Features

- **Idle Mechanics** — Offline progression, skill ticks, and resource generation
- **Skills UX** — Implemented skills open training (`SkillDetailScreen`); skills without registry actions show a **Coming Soon** dialog (`SkillComingSoonDialog`). **Herblore** uses herbs from **Harvesting** (bank `inputItems`); **Scavenging** adds salvage gathering tiers.
- **Account & Profile Persistence** — Room-based save/load with session management
- **🎬 Docking Station UI** — Beautiful character selection with **animated glitch effects**, timeline visualizations, and skill badge showcases
- **Jetpack Compose** — Modern declarative UI with Material Design 3
- **JDK 26 builds + Gradle 9.6 snapshot** — Maintainer workflow runs Gradle on **JDK 26**; bytecode stays **Java 21** until the matrix moves
- **Future GPU Island** — C++ / OpenGL ES 3 planned for advanced rendering (Phase 5+)

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| **Android Studio** | 2024.1+ | Bundled Gradle toolchain support required |
| **JDK (build)** | **26** (recommended) | Primary maintainer path; Windows helper: `build-with-jdk26.bat` |
| **JDK (daemon pin)** | **21** (ADOPTIUM) | Checked into `gradle/gradle-daemon-jvm.properties` + Foojay resolver for clean clones |
| **JVM bytecode** | **21** | `sourceCompatibility` / `targetCompatibility` in `:app` and `:core` |
| **Android SDK** | 36.1 (API 36 · Android 16) | Installed via Android Studio / `sdkmanager` |
| **Gradle** | **9.6.0** snapshot | Pinned in `gradle/wrapper/gradle-wrapper.properties` |

### Build & Run

```bash
# Clone the repo
git clone https://github.com/Otterdays/Arteria-V2-Gradle-Edition-Reloaded.git
cd Arteria-V2-Gradle-Edition-Reloaded

# Build APK (uses daemon JVM from gradle-daemon-jvm.properties, typically JDK 21)
./gradlew :app:assembleDebug

# Windows: same tasks but force JDK 26 for the Gradle JVM (edit JDK path inside the script if needed)
build-with-jdk26.bat :app:assembleDebug

# macOS / Linux: pass the same flag explicitly
# ./gradlew -Dorg.gradle.java.home=/path/to/jdk-26 :app:assembleDebug

# Install to device/emulator
./gradlew :app:installDebug

# Run unit tests
./gradlew :app:testDebugUnitTest

# Run instrumented tests (Room integration)
./gradlew :app:connectedAndroidTest
```

### First Run

1. **Launch emulator** (API 36 or later recommended)
2. **Run** `:app:assembleDebug` → `:app:installDebug`
3. **Tap** Arteria on the emulator home screen
4. **Create** a new account and watch the animated account selection screen
5. **Explore** the Docking Station with glitching CRT effects, timeline nodes, and energy flow particles

---

## Docking UI Notes

The account selection flow ships with deterministic glitch entry animations and ambient selected-card effects.
For full technical details, use `DOCS/ARCHITECTURE.md` and `CLAUDE.md` instead of duplicating animation specs here.

---

## 🏗️ Architecture

### Current State (Phases 0–1)

```
┌─────────────────────────────────────────────────────┐
│         Android OS (API 26–36)                       │
├─────────────────────────────────────────────────────┤
│  MainActivity.kt  (ComponentActivity)                │
│    ↓ (Compose)                                      │
│  ArteriaApp (NavHost)                               │
│    ├── 🎪 AccountSelectionScreen + Animation        │
│    ├── 📝 AccountCreationScreen                      │
│    ├── 🎮 GameScreen (3-tab hub: Skills/Bank/Combat; Coming Soon modal for stub skills)│
│    ├── ⚙️  SettingsScreen (overlay)                  │
│    └── 📜 ChangelogScreen (version history)         │
├─────────────────────────────────────────────────────┤
│  Room Database (Profile & Game Persistence)         │
│    ├── ProfileEntity, ProfileDao, ProfileDB         │
│    └── GameEntity, GameDao, GameDB                  │
├─────────────────────────────────────────────────────┤
│  :core Module (Android library, currently minimal)  │
│    ├── SkillId, XPTable, TickEngine                 │
│    └── MiningData, GameModels (domain types)        │
└─────────────────────────────────────────────────────┘
```

### Toolchain Snapshot

| Component | Version | Scope |
|-----------|---------|-------|
| **Gradle** | `9.6.0-20260331012943+0000` | Build orchestration (snapshot) |
| **AGP** | `9.1.0` | Android Gradle Plugin (stable) |
| **Kotlin Compose plugin** | `2.3.20` | Compose compiler plugin pin |
| **KSP** | `2.3.6` | Room and codegen hook |
| **Compose BOM** | `2026.03.01` | Material 3, icons, layout, animation |
| **Navigation** | `2.9.7` | Type-safe route encoding |
| **Room** | `2.8.4` | Reactive persistence with KSP codegen |
| **Lifecycle** | `2.10.0` | StateFlow, ViewModel lifecycle |
| **kotlinx-coroutines** | `1.10.2` | Test + structured concurrency helpers |
| **JDK (Gradle JVM)** | **26** (local) / **21** (pinned daemon) | Run Gradle on 26 when you want; repo still documents Foojay **21** for reproducible daemons |
| **JVM bytecode** | **21** | `compileOptions` / Kotlin jvmTarget alignment |

### Dependency Sweep (2026-03-31)

- No dependency bumps were applied in this sweep.
- Current pins remain intentionally stable for active feature work (`AGP 9.1.0`, Compose plugin `2.3.20`, KSP `2.3.6`, Compose BOM `2026.03.01`, Room `2.8.4`).
- Newer options observed in the ecosystem are primarily alpha/nightly lines; we keep those out until there is a concrete feature or compatibility need.
- Full decision log and next-review cadence live in `DOCS/SBOM.md`.

**📄 Full SBOM:** See [DOCS/SBOM.md](DOCS/SBOM.md) for complete dependency inventory, upgrade roadmap, and next-available versions.

### Future Architecture (Phases 2–6+)

Once engine logic and UI shells complete, an optional **GPU island** can be added:

```
C++ / OpenGL ES 3 layer (GPU island)
  ├─ EGL + GLES 3 context
  ├─ Shader system (GLSL)
  ├─ Model + Texture pipeline
  └─ Motion/key event handlers
     ↕ (Compose can composite over GL surface)
```

See **[DOCS/ARCHITECTURE.md](DOCS/ARCHITECTURE.md)** for detailed system design and rendering roadmap.

---

## 📂 Project Structure

```
Arteria-V2-Gradle-Edition-Reloaded/
├── README.md (this file)
├── settings.gradle.kts           # Gradle module declaration
├── build.gradle.kts              # Root config (plugins, versions)
├── gradle.properties             # Gradle daemon settings (optional org.gradle.java.home)
├── build-with-jdk26.bat          # Windows: run Gradle with JDK 26
│
├── gradle/
│   ├── wrapper/
│   │   └── gradle-wrapper.properties       # Pinned Gradle 9.6 nightly
│   └── gradle-daemon-jvm.properties        # Foojay-pinned daemon JDK 21 + ADOPTIUM
│
├── app/                           # Main application module (:app)
│   ├── build.gradle.kts
│   └── src/main/java/com/arteria/game/
│       ├── MainActivity.kt                 # Entry point
│       ├── ui/
│       │   ├── ArteriaApp.kt               # NavHost root + DB setup
│       │   ├── account/                    # Account select/create/session
│       │   ├── game/                       # Game hub + screens
│       │   ├── components/                 # DockingGlitch, DockingAccountCard, etc.
│       │   └── theme/                      # Material 3 + Cinzel typography
│       ├── navigation/
│       │   └── NavRoutes.kt                # Type-safe route encoding
│       ├── data/
│       │   ├── profile/                    # Room profile persistence
│       │   └── game/                       # Room game state persistence
│       └── core/                           # Domain logic (no Android imports)
│           ├── skill/                      # SkillId, XPTable
│           ├── engine/                     # TickEngine, offline simulation
│           └── model/                      # GameModels, MiningData
│
├── core/                          # Empty library module (:core)
│   └── build.gradle.kts           # Reserved for extracted engine code
│
├── DOCS/                          # Documentation hub
│   ├── SUMMARY.md                 # AI read order + design doc paths
│   ├── ARCHITECTURE.md            # System design & conventions
│   ├── SBOM.md                    # Software bill of materials
│   ├── ROADMAP.md                 # Phases 0–10 delivery plan
│   ├── SCRATCHPAD.md              # Live handoff + agent notes
│   └── ...
│
└── www/                           # Local landing page
    └── index.html                 # Dev hub with animated design
```

---

## 🛠️ Development Workflow

### Writing Code

1. **Respect boundaries**: UI composables don't call Room DAOs; they use ViewModels and StateFlow
2. **Kotlin async**: Use `viewModelScope` only; never `GlobalScope`
3. **Error handling**: Sealed `Result<T>` types; fail fast instead of silent failures
4. **Testing**: Write unit tests for domain logic (`:core`), instrumented tests for Room
5. **Git commits**: Conventional Commits format (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`)

### Build Verification

```bash
# Compile all modules
./gradlew clean :app:compileDebugKotlin :core:compileDebugKotlin

# Assemble & verify
./gradlew :app:assembleDebug

# Full test suite
./gradlew :app:testDebugUnitTest :app:connectedAndroidTest

# Code quality (optional)
./gradlew detektMain  # if Detekt plugin is wired
```

---

## 🎯 Current Status

| Phase | Status | Details |
|-------|--------|---------|
| **Phase 0** | ✅ DONE | Gradle 9.6 snapshot scaffold, AGP 9.1, JVM 21 target |
| **Phase 1** | ✅ DONE | Compose shell, navigation, account persistence, animation system |
| **Phase 2** | 🚧 IN PROGRESS | Engine port (TickEngine, XPTable, SkillId), unit tests |
| **Phase 3** | ✅ DONE | UI screens (Skills, Bank, Combat) wired in game shell |
| **Phase 4** | ✅ DONE | Room v2 migration + offline audit field; save/load + `OfflineReportDialog`; JVM + instrumented persistence tests — see [ROADMAP](DOCS/ROADMAP.md) |
| **Phase 5** | 🚧 IN PROGRESS | Mining + bank vertical slice playable, polish/testing pending |

**Next Immediate Goal:** Device smoke (account → game → mining → bank → switch account); extend `GameViewModel` tests for periodic save cadence; drive Phase 5 slice to **DONE** when smoke is clean.

---

## 📚 Documentation

- **[README](./README.md)** ← You are here
- **[SUMMARY.md](DOCS/SUMMARY.md)** — Documentation hub & design doc paths
- **[ARCHITECTURE.md](DOCS/ARCHITECTURE.md)** — System design, boundaries, conventions
- **[SBOM.md](DOCS/SBOM.md)** — Dependency inventory, upgrade roadmap
- **[ROADMAP.md](DOCS/ROADMAP.md)** — Delivery phases 0–10
- **[SCRATCHPAD.md](DOCS/SCRATCHPAD.md)** — Live handoff notes & agent credits

**Game Design Truth:** Main repo `../DOCU/` (monorepo context) or bundled copy at `DOCS/ARTERIA-V1-DOCS/DOCU/`

---

## 🤝 Contributing

1. **Read** [DOCS/SUMMARY.md](DOCS/SUMMARY.md) → [DOCS/ROADMAP.md](DOCS/ROADMAP.md) for context
2. **Pick a phase** or feature from [DOCS/ROADMAP.md](DOCS/ROADMAP.md) (Phases 2–10 are open)
3. **Create a branch** from `main` (no force-pushes unless coordinated)
4. **Follow conventions** in [DOCS/ARCHITECTURE.md](DOCS/ARCHITECTURE.md) (KISS/YAGNI/DRY/Fail Fast)
5. **Test locally**: `:app:testDebugUnitTest` and `:app:connectedAndroidTest` must pass
6. **Commit** with Conventional Commits format
7. **Open PR** with description of what/why; await review

**AI Agents:** See [CLAUDE.md](CLAUDE.md) for philosophy, workflow init order, and Kotlin/Gradle standards.

---

## 🐛 Troubleshooting

### Build fails with `JAVA_COMPILER missing`
Your machine is using a **JRE** instead of a **JDK**. Point Gradle at a full JDK — **21** matches the daemon pin; **26** is fine if you run builds with `build-with-jdk26.bat` or set `org.gradle.java.home` yourself:

```properties
org.gradle.java.home=C:/Program Files/Java/jdk-26
```

### Gradle wrapper stuck on nightly snapshot
Clear Gradle cache and re-download:
```bash
./gradlew clean --no-daemon
rm -rf ~/.gradle/wrapper/dists
./gradlew :app:assembleDebug
```

### Room migration errors on device
Delete app data: **Settings > Apps > Arteria > Storage > Clear Data**, then reinstall APK.

### Compose preview not working in IDE
- **Invalidate caches:** File > Invalidate Caches > Restart
- **Rebuild project:** Build > Clean Project → Build > Make Project
- **Resync Gradle:** File > Sync Now

---

## 📄 License

This project is part of the Arteria ecosystem. A repository license file is not yet published.

---

## 🙏 Credits

**Arteria V2** is maintained by the native Android team. Built with:
- **Kotlin** for safety and expressiveness
- **Jetpack Compose** for modern UI
- **Gradle 9.6 snapshot** line for early platform fixes
- **Room** for reliable persistence
- **Custom animation system** for delightful user experience

**Special thanks** to all contributors and the Kotlin/Android communities for amazing tooling.

---

<div align="center">

**🚀 Ready to build the future?** [Open DOCS/SUMMARY.md →](DOCS/SUMMARY.md)

**Questions?** Check [DOCS/REFERENCES.md](DOCS/REFERENCES.md) for external docs and links.

</div>
