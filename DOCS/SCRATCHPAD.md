<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Zen-Dhawan recovery completion pass: wired game route/screens/data engine, fixed review findings (offline processing threading, transaction save, enum safety), and added unit tests for TickEngine + GameRepository with green `:app:testDebugUnitTest`. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | SCRATCHPAD/SUMMARY/SBOM status refresh: JDK 21 build path, Room profiles, transfer APK naming; superseded stale “build blocked” handoff lines. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Renamed transfer APK output to `Arteria-V2-Gradle-Edition-Reloaded-*` in `build-apk-for-transfer.ps1`. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Upgraded all declared app/core dependency coordinates to SBOM latest-known versions for user-led full modernization test pass. |
| 2026-03-30 | Claude Haiku 4.5 | Anthropic Claude | Professional README.md with banner, quick-start, architecture, project structure, docs roadmap, contributing guidelines, troubleshooting, and links to main Arteria project. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Implemented persistent startup account/profile flow using Room + AccountViewModel, switched play nav to `profileId`, added tests, and updated SBOM. |
| 2026-03-22 | Cursor Agent (Composer) | Cursor | Initial SCRATCHPAD — agent handoff template. |
| 2026-03-22 | Cursor Agent (Composer) | Cursor | Last Actions renumbered; Next Action points at `Arteria-Gradle-Edition-v1.2/`; out-of-scope minSdk note amended. |
| 2026-03-22 | Cursor Agent (Composer) | Cursor | Cinzel bundled + docs (SBOM, REFERENCES, ARCHITECTURE); SCRATCHPAD status. |
| 2026-03-22 | Cursor Agent (Composer) | Cursor | Animated `DockingBackground` (twinkle, nebula, gradient breath); KDoc “way forward”. |
| 2026-03-30 | Cursor Agent | Composer | Renamed handoff to **V2**; docs hub path `Arteria-Gradle-Edition-V2/DOCS/`; pointer to `SUMMARY.md` + ROADMAP Phases 7–10. |

*Future contributors: append a row when you materially change this doc.*

---

# SCRATCHPAD — Arteria Gradle Edition V2

Active state for **native Android** track. Game design truth stays in monorepo root **`../DOCU/`** (e.g. `MASTER_DESIGN_DOC.md`, `TRUTH_DOCTRINE.md`); bundled mirror: `DOCS/ARTERIA-V1-DOCS/DOCU/`. This folder (`Arteria-Gradle-Edition-V2/`) is **planning + continuity** for the Gradle 9.6 / Kotlin / Compose product line.

**`[AMENDED 2026-03-30]:`** Gradle project root is **`Arteria-Gradle-Edition-V2/`** (not `Arteria-Gradle-Edition-v1.2/`). New agents: read **[DOCS/SUMMARY.md](SUMMARY.md)** first.

**`[AMENDED 2026-03-30]:`** This repo may be checked out as **`Arteria-V2-Gradle-Edition-Reloaded`** on disk — same Gradle project (`settings.gradle.kts`, `:app`, `:core`).

**`[AMENDED 2026-03-30]:`** Expo / React Native references in this file and other docs are historical V1 notes unless explicitly marked as active V2 work.

---

## Current Status

- **Phase:** Phase 0 (Structure + Gradle 9.6 wrapper + Nightly URL) is **DONE [2026-03-22]**. Phase 1 **partial** — see [ROADMAP.md](ROADMAP.md) (`[IN PROGRESS 2026-03-22]` checklist). Phases **7–10** (features / QoL / UI / ship) are **backlog** for parallel AI work — same file.
- **Runnable app:** `:app` Compose **Docking Station** styling (palette from `apps/mobile/constants/theme.ts`), **Cinzel** display type (`res/font/cinzel.ttf`, OFL), **animated space backdrop** (`DockingBackground`: twinkling stars, drifting aurora blobs, breathing gradient) on menu-related screens. NavHost `ui/ArteriaApp.kt`. `package com.arteria.game`, `minSdk 26`, `compileSdk 36.1`, `targetSdk 36`. **Daemon JVM:** JDK **21** / **Adoptium** via `gradle/gradle-daemon-jvm.properties`. `:core` empty. `:app:assembleDebug` green [2026-03-22].
- **`[AMENDED 2026-03-30]:`** **Startup profiles:** Room-backed account list + create/select + session placeholder; `data/profile/*`, `AccountViewModel`, `play/{profileId}`. **Build:** Use a full **JDK 21** (`javac` available); repo may set `org.gradle.java.home` in `gradle.properties`. **`build-apk-for-transfer`** copies APK to `dist/` as `Arteria-V2-Gradle-Edition-Reloaded-<variant>-<timestamp>.apk`.
- **Docs hub:** **`Arteria-Gradle-Edition-V2/DOCS/`** — start at **`SUMMARY.md`**, then **`ROADMAP.md`**, **`ARCHITECTURE.md`**, **`MIGRATION_SPEC.md`**, **`SBOM.md`**.
- **Main shipping app:** Still **`../apps/mobile/`** (Expo / RN, Gradle 8.x). Unaffected.

---

## Last Actions (most recent first)

**2026-03-31:** **GameViewModel test stability:** Added optional constructor param `enableTickLoop` (default `true`; `ViewModelProvider.Factory` unchanged). `GameViewModelTest` passes `enableTickLoop = false` so the tick loop’s `delay`/`while` does not run under `runTest`; removed reflection-based `dispose()`. **Note:** On Windows, Gradle may fail with `FileSystemException` on `app/build/.../classes.jar` if another process (IDE, daemon) holds the file — close or retry before `:app:testDebugUnitTest` / `:app:clean`.

**2026-03-31:** **Zen-Dhawan recovery verification green:** `:app:assembleDebug` and `:app:testDebugUnitTest` both pass after integration. Added tests:
- `app/src/test/java/com/arteria/game/core/engine/TickEngineTest.kt`
- `app/src/test/java/com/arteria/game/data/game/GameRepositoryTest.kt`
- Updated `AccountViewModelTest` to stable coroutine test dispatcher usage.
Also fixed reviewer findings:
- Moved offline tick processing off main thread in `GameViewModel` (`Dispatchers.Default` for offline simulation).
- Made `GameRepository.saveGameState` atomic via injected transaction runner wired to Room `withTransaction` in `ArteriaApp`.
- Hardened skill enum decode during load (`runCatching { SkillId.valueOf(...) }`).

**2026-03-30:** **Zen-Dhawan recovery mission (in progress):** Located recovered gameplay stack in `@.claude/worktrees/zen-dhawan` and started canonical merge into app tree:
- Added `app/src/main/java/com/arteria/game/core/{model,skill,data,engine}/*` (`GameModels`, `SkillId`, `XPTable`, `MiningData`, `TickEngine`).
- Added `app/src/main/java/com/arteria/game/data/game/*` (`GameEntities`, `GameDao`, `GameDatabase`, `GameRepository`) with `arteria_game.db`.
- Added `app/src/main/java/com/arteria/game/ui/game/*` (`GameScreen`, `GameViewModel`, `SkillsScreen`, `SkillDetailScreen`, `BankScreen`, `CombatScreen`, `SettingsScreen`, `OfflineReportDialog`).
- Updated `ArteriaApp` + `NavRoutes` to route account continue -> `game/{profileId}` with encoded profile id.
- Updated `app/build.gradle.kts` and `SBOM.md` for `androidx.compose.material:material-icons-extended`.
- **`[AMENDED 2026-03-31]:`** Compose icons dependency is **`material-icons-core`** (BOM), not `material-icons-extended` — align SBOM line when editing that file.
- **Open verification task:** complete successful `:app:assembleDebug` and test pass after full merge.

**2026-03-30:** **Architecture doc fully rebuilt:** Rewrote `DOCS/ARCHITECTURE.md` into a clean, code-accurate architecture spec with explicit sections for current runtime architecture, responsibilities, behavior flows, boundaries, constraints, and planned next architecture. Removed mixed historical/planned ambiguity from primary sections.

**2026-03-30:** **SBOM rebuild + docs audit:** Replaced `DOCS/SBOM.md` with a clean, current bill of materials generated from live Gradle declarations (`settings.gradle.kts`, root/app/core `build.gradle.kts`, wrapper/toolchain files). Audit notes: `DOCS/ARCHITECTURE.md` still contains historical/planned sections mixed with current-state data and should get a focused cleanup pass when content-doc edits are in scope.

**2026-03-30:** **Handoff / docs sync:** Status above reflects **working** `build-apk-for-transfer` + `:app:assembleDebug` when the machine uses JDK 21 (not a JRE-only path). Prior notes in this file about `JAVA_COMPILER` / JRE Adoptium failures are **historical** — fix is JDK + correct `JAVA_HOME` / optional `org.gradle.java.home`. Ready to continue Phase 1 / gameplay placeholders per [ROADMAP.md](ROADMAP.md).

**2026-03-30:** **Transfer APK filename:** `build-apk-for-transfer.ps1` now copies to `dist/` as `Arteria-V2-Gradle-Edition-Reloaded-<debug|release>-<timestamp>.apk` (was `Arteria-v1.2-...`).

**2026-03-30:** **Bulk dependency modernization:** Updated `app/build.gradle.kts` and `core/build.gradle.kts` to SBOM latest-known versions (core-ktx, lifecycle*, activity-compose, compose BOM, navigation-compose, room*, coroutines-test, AndroidX test libs). Build attempt still blocked by local Java toolchain resolution selecting `Eclipse Adoptium jre-21...` (`JAVA_COMPILER` missing) despite `org.gradle.java.home` entry.

**2026-03-30:** **Basic username/session persistence shipped (Room):** Added `data/profile` Room stack (`ProfileEntity`, `ProfileDao`, `ProfileDatabase`, `ProfileRepository`, `RoomProfileRepository`), new `AccountViewModel` state/actions (load/select/create/continue), wired persistence in `ui/ArteriaApp.kt`, switched route payload to `profileId` in `navigation/NavRoutes.kt`, and upgraded account UI to surface validation/DB errors. Added tests: `app/src/test/.../AccountViewModelTest.kt` and `app/src/androidTest/.../RoomProfileRepositoryTest.kt`. Build verification currently blocked on machine JDK setup (`JAVA_COMPILER` capability missing from configured JRE path).
**2026-03-30:** **Professional README.md shipped:** Created comprehensive `README.md` (404 lines) with ASCII art header, engaging introduction, quick-start section, architecture overview (current Compose shell + future GPU island), detailed project structure tree, documentation roadmap with reading order, development workflow (UI/frontend, engine/backend, Gradle), contributing guidelines, build verification checklist, troubleshooting guide, license/credits, and links to main Arteria project. Integrated with existing DOCS/ hub and SUMMARY.md. Serves as primary entry point for new developers and AI agents.

1. **2026-03-30:** **Basic username/session persistence shipped (Room):** Added `data/profile` Room stack (`ProfileEntity`, `ProfileDao`, `ProfileDatabase`, `ProfileRepository`, `RoomProfileRepository`), new `AccountViewModel` state/actions (load/select/create/continue), wired persistence in `ui/ArteriaApp.kt`, switched route payload to `profileId` in `navigation/NavRoutes.kt`, and upgraded account UI to surface validation/DB errors. Added tests: `app/src/test/.../AccountViewModelTest.kt` and `app/src/androidTest/.../RoomProfileRepositoryTest.kt`. Build verification currently blocked on machine JDK setup (`JAVA_COMPILER` capability missing from configured JRE path).

2. **2026-03-30:** **Docs / roadmap alignment (V2):** Added `DOCS/SUMMARY.md` (AI hub, design-doc paths, multi-agent workflow). `ROADMAP.md` — identity block + **Phases 7–10** (UX/QoL, visual alignment, feature verticals, performance & Play). Normalized **Gradle root** naming to **`Arteria-Gradle-Edition-V2/`** across `ARCHITECTURE.md`, `REFERENCES.md`, `SBOM`, `MIGRATION_SPEC`, `ROADMAP`, `SCRATCHPAD`. Root `README.md` + `DOCU/SUMMARY.md` link to V2 doc hub.
2. **2026-03-22:** **Animated main-menu sky:** `DockingBackground` — dual `InfiniteTransition` loops (26s cosmic / 5.2s twinkle), **78** stars with per-star phase/speed, **3** soft radial **nebula** blobs (accent blue / purple / luminar) drifting slowly, **lerp**-shifted vertical gradient “breath.” KDoc lists roadmap: reduced motion, parallax, shader fog, battery hooks. Used on account select / create / session screens.
3. **2026-03-22:** **Cinzel font:** Bundled variable `res/font/cinzel.ttf` (Google Fonts OFL, parity with Expo `Cinzel_400Regular` / `Cinzel_700Bold`). `ArteriaTheme.kt` uses Cinzel for display / titles / pretag; body remains sans-serif. Docs: `SBOM.md` **Bundled font assets**, `REFERENCES.md` typography section, `ARCHITECTURE.md` §4 rows. `:app:compileDebugKotlin` OK.
4. **2026-03-22:** **Docking Station UI:** Palette + starfield + gradient account cards inspired by `apps/mobile/app/character-select.tsx` and `constants/theme.ts`.
5. **2026-03-22:** **Account UI (stub):** NavHost — account selection, creation (Standard mode), session placeholder. Files: `ui/ArteriaApp.kt`, `ui/account/*`, `navigation/NavRoutes.kt`.
6. **2026-03-22:** **Handoff wrap-up:** Next Action targets **`Arteria-Gradle-Edition-V2/`**; SBOM single source; title *Software* Bill of Materials.
7. **2026-03-22:** **SBOM cleanup:** Single source of truth tables.
8. **2026-03-22:** **Doc accuracy pass:** `ARCHITECTURE.md`, `SCRATCHPAD.md`, `ROADMAP.md` Phase 1 `[IN PROGRESS]`.
9. **2026-03-22:** **SBOM audit:** Canonical dependency inventory.
10. **2026-03-22:** **JVM 21** app bytecode + daemon.
11. **2026-03-22:** **Gradle Daemon toolchain** JDK 21 / ADOPTIUM.
12. **2026-03-22:** **Android 16** compileSdk 36.1 / targetSdk 36.
13. **2026-03-22:** AGP 9.1 **built-in Kotlin** migration; launcher icon fix.
14. **2026-03-22:** `build-apk-for-transfer` scripts.
15. **2026-03-22:** Agent continuity suite (ROADMAP, MIGRATION_SPEC, REFERENCES, SCRATCHPAD).
16. **2026-03-22:** React Native vs Gradle 9.x documented; `whitepaper.md` superseded for execution.
17. **2026-03-22:** Established **Gradle Edition V2** **future direction**: Kotlin + Compose, C++/OpenGL GPU island.

---

## Blockers

- **None** for documentation / planning.
- **Build risk:** Gradle 9.6 **nightly** can change between snapshots — always pin wrapper URL (see `SBOM.md`).
- **`[AMENDED 2026-03-30]:`** If Gradle reports `JAVA_COMPILER` missing, the machine is using a **JRE** or wrong `JAVA_HOME` — point to a **JDK 21** install (see `gradle.properties` `org.gradle.java.home` if present).

---

## Next Action (for the next agent)

**`[AMENDED 2026-03-31]:`** Manual app smoke test + polish checklist:
1. Install APK on emulator/device and verify flow: account create/select -> enter game -> train Mining -> see bank item growth -> switch account.
2. Add `GameViewModel` tests for offline report display behavior and periodic save behavior.
3. Decide if `app/src/main/java/com/arteria/game/core/*` should migrate into `:core` module now or after gameplay vertical stabilizes.
4. Remove or archive now-orphaned `PlayPlaceholderScreen.kt` once manual smoke confirms no fallback needed.

**`[AMENDED 2026-03-30]:`** Continue Zen-Dhawan recovery checklist:
1. Finish wiring by replacing old placeholder references (`PlayPlaceholder`) if any remain.
2. Run `:app:assembleDebug`, `:app:testDebugUnitTest`, and targeted device smoke test for account -> game route.
3. Add tests for `TickEngine` tick/offline behavior and `GameRepository` load/save correctness.
4. If stable, update docs (`ARCHITECTURE`, `SUMMARY`, `SBOM`) with final “Phase 1 complete” status.

**`[AMENDED 2026-03-30]:`** Continue product work: [ROADMAP.md](ROADMAP.md) Phase 1 navigation placeholders (Skills, Bank, Combat, Settings) and/or gameplay wiring beyond session placeholder. Run `:app:testDebugUnitTest` and instrumented tests when touching persistence or navigation.
**[AMENDED 2026-03-30]:** README published; next focus: **Phase 1 completion** (navigation scaffold with placeholder routes: Skills, Bank, Combat, Settings) or **Phase 2 engine port** (TickSystem, XPTable logic from monorepo). **Setup verification:** Confirm local JDK 21 is full JDK (not JRE), then run `:app:assembleDebug` + `:app:testDebugUnitTest` + device install to validate Room persistence end-to-end.

1. **New developer checklist:** Read [README.md](../README.md) first (just created), then [SUMMARY.md](SUMMARY.md), then [ROADMAP.md](ROADMAP.md) Phase 1 checklist + **Phases 7–10** (UX/QoL priorities).
2. Read [MIGRATION_SPEC.md](MIGRATION_SPEC.md) for TS → Kotlin mapping and source file pointers.
3. Open **`Arteria-Gradle-Edition-V2/`** in Android Studio (project root with `settings.gradle.kts`, `:app`, `:core`).
4. **Finish Phase 1:** Navigation Compose scaffold with placeholder routes (Skills, Bank, Combat, Settings) per ROADMAP; optionally add first JVM unit test under `:core` with zero `android.*` imports.
5. **Truth for versions/deps:** [SBOM.md](SBOM.md) — single source of truth.

---

## Out-of-Scope Observations

- **Package name:** **Resolved (2026-03-22):** Current id is **com.arteria.game** (applicationId, namespace, Kotlin paths). Older scaffold used **com.example.arteriav15_gradleedition** (obsolete). When C++ is added, coordinate **System.loadLibrary** with the CMake target name.
- **minSdk:** `[AMENDED 2026-03-22]:` Repo **`minSdk` is 26** (not 36). If product policy changes supported OS floor, update `app`/`core` Gradle, `SBOM.md` Android SDK table, and note in `ROADMAP.md`.
- **`whitepaper.md`:** Grok draft; high-level native vision only. **ROADMAP + ARCHITECTURE + MIGRATION_SPEC** override for exact tooling versions and folder layout.

---

## Documentation Index (this folder)

| Doc | Role |
|-----|------|
| `../README.md` | **Primary entry point** — introduction, quick-start, architecture, project structure, development workflow, contributing |
| `SUMMARY.md` | Doc hub — paths, AI read order, features/QoL pointers |
| `ARCHITECTURE.md` | Stack, RN vs Gradle 9, render loop, conventions |
| `SBOM.md` | Single source: toolchain, SDK targets, dependency inventory, update policy |
| `ROADMAP.md` | Phased delivery 0–10 |
| `MIGRATION_SPEC.md` | How to port from RN monorepo |
| `DESIGN_TRANSLATION_AUDIT.md` | Detailed checklist of Split data / skills / content files from V1 |
| `REFERENCES.md` | External links (Gradle, AGP, Compose, GameActivity, etc.) |
| `SCRATCHPAD.md` | This file — live handoff |
| `whitepaper.md` | Vision / rhetoric — do not treat as runbook |
| `gradle_website_links.md` | Legacy one-liner; see `REFERENCES.md` |
