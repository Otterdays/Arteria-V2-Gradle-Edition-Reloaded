<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
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

---

## Current Status

- **Phase:** Phase 0 (Structure + Gradle 9.6 wrapper + Nightly URL) is **DONE [2026-03-22]**. Phase 1 **partial** — see [ROADMAP.md](ROADMAP.md) (`[IN PROGRESS 2026-03-22]` checklist). Phases **7–10** (features / QoL / UI / ship) are **backlog** for parallel AI work — same file.
- **Runnable app:** `:app` Compose **Docking Station** styling (palette from `apps/mobile/constants/theme.ts`), **Cinzel** display type (`res/font/cinzel.ttf`, OFL), **animated space backdrop** (`DockingBackground`: twinkling stars, drifting aurora blobs, breathing gradient) on menu-related screens. NavHost `ui/ArteriaApp.kt`. `package com.arteria.game`, `minSdk 26`, `compileSdk 36.1`, `targetSdk 36`. **Daemon JVM:** JDK **21** / **Adoptium** via `gradle/gradle-daemon-jvm.properties`. `:core` empty. `:app:assembleDebug` green [2026-03-22].
- **Docs hub:** **`Arteria-Gradle-Edition-V2/DOCS/`** — start at **`SUMMARY.md`**, then **`ROADMAP.md`**, **`ARCHITECTURE.md`**, **`MIGRATION_SPEC.md`**, **`SBOM.md`**.
- **Main shipping app:** Still **`../apps/mobile/`** (Expo / RN, Gradle 8.x). Unaffected.

---

## Last Actions (most recent first)

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

---

## Next Action (for the next agent)

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
