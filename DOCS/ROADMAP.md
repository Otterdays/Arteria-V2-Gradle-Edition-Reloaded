<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Phase 4 marked `[DONE]`: `GameDatabase` v2 migration + tests; `lastOfflineTickAppliedAt` pipeline. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Progress checkpoint pass: checked off shipped items across Phases 1/3/4 and set next concrete target for Phase 5 vertical slice hardening. |
| 2026-03-22 | Cursor Agent (Composer) | Cursor | Initial ROADMAP Phases 0–6 + source references. |
| 2026-03-30 | Cursor Agent | Composer | V2 identity block, canonical paths, Phases 7–10 (features / QoL / UI / ship). |

*Future contributors: append a row when you materially change this doc.*

---

# ROADMAP — Arteria Gradle Edition V2

Phased plan for the **native** Android line. Game content and tone remain defined in monorepo root **`../DOCU/`** (`MASTER_DESIGN_DOC.md`, `TRUTH_DOCTRINE.md`, and related docs). A read-only mirror lives under **`DOCS/ARTERIA-V1-DOCS/DOCU/`**; if they diverge, **root `DOCU/` wins**.

**Rules for agents:** Mark phases done in this file with `[DONE YYYY-MM-DD]:` notes — do not delete prior text. Add new phases at the top if the plan forks.

---

## [AMENDED 2026-03-30] Product identity, Gradle root, AI alignment

- **Folder / Gradle root:** `Arteria-Gradle-Edition-V2/` (contains `settings.gradle.kts`, `:app`, `:core`). Older mentions of `Artera-Gradle-Edition/`, `Arteria-Gradle-Edition-v1.2/`, or scaffold-only `GameActivity` paths are **legacy names** — use **this folder** unless comparing history.
- **Handoff hub:** Start from **`DOCS/SUMMARY.md`**, then this file and **`SCRATCHPAD.md`**.
- **Phases 0–6:** Spine — scaffold, engine port, UI shell, persistence, content, release engineering.
- **Phases 7–10:** Ongoing **features, QoL, and UI** — can run **in parallel** with later spine phases once enough UI shell exists; pull specifics from `../DOCU/IMPROVEMENTS.md`, `CURRENT_IMPROVEMENTS.md`, `UI_REVISION_CRAFTING_v2.md`, `THEMING.md`, `zhip-ai-styling.md`.

---

## Phase 0 — Scaffold and documentation [DONE 2026-03-22]

- Initial native template + Gradle 9.6 nightly wrapper, AGP 9.1.0, API 36 targets (see `SBOM.md`). `[AMENDED 2026-03-30]:` Current tree is **`Arteria-Gradle-Edition-V2/`** (Compose shell, `com.arteria.game`); early `GameActivity` + C++/GLES notes in archived path names are **superseded** by `ARCHITECTURE.md` “current state” table unless C++ is reintroduced.
- Docs: `ARCHITECTURE.md`, `SBOM.md`, continuity docs in this folder.

**Reference:** `gradle/wrapper/gradle-wrapper.properties`, `app/build.gradle.kts`, `settings.gradle.kts`.

---

## Phase 1 — Foundation (Compose shell + package identity)

**Goal:** Runnable app with Jetpack Compose entry, clear package name, and a **pure Kotlin** `core` module (no Android imports in `core`).

- Add Compose BOM + dependencies; enable Compose in `app/build.gradle.kts`.
- Introduce navigation scaffold (e.g. Navigation Compose) with placeholder routes: Skills, Bank, Combat, Settings.
- Rename application id / namespace from `com.example.arteriav15_gradleedition` toward `com.arteria.game` (or chosen final id) — update manifest, CMake native lib name + `loadLibrary` if lib name changes.
- Create Gradle module or source set: `core/` — empty engine package + first unit test (JUnit runs on JVM).

**Consult (main repo, read-only patterns):**

- `apps/mobile/app/_layout.tsx` — tab structure intent (not code copy).
- `DOCU/ARCHITECTURE.md` — monorepo concepts; V2 native app is a separate project.

**Exit criteria:** `./gradlew :app:assembleDebug` succeeds; Compose shows a stub home screen; `core` compiles with zero `android.*` imports.

`[IN PROGRESS 2026-03-22]:` Partial Phase 1 complete:
- [x] Compose BOM + dependencies in `app/build.gradle.kts` (BOM 2024.06.00, Material3, Navigation Compose)
- [x] Package renamed to `com.arteria.game` — `applicationId`, `namespace`, Kotlin source path, manifest
- [x] `:core` Gradle module created (empty library, `compileSdk 36.1`, `minSdk 26`)
- [x] `./gradlew :app:assembleDebug` green; app confirmed running on API 36.1 AVD
- [x] Navigation scaffold + in-app tabs shipped (`Skills`, `Bank`, `Combat`, `Settings`) in `ui/game/GameScreen.kt`

`[DONE 2026-03-31]:` Phase 1 exit criteria are now met in current tree.

---

## Phase 2 — Engine port (headless Kotlin)

**Goal:** Port idle **math and rules** from TypeScript engine into `core` — no UI.

- Tick / delta processing: mirror `TickSystem.ts` behavior.
- XP progression: port `XPTable.ts` + tests.
- Types: port `types.ts` skill IDs, player shape subsets needed for ticks.
- Narrative helpers: port relevant pieces of `narrative.ts` if gating is in-engine.

**Primary sources (main repo):**

- `packages/engine/src/TickSystem.ts`
- `packages/engine/src/XPTable.ts`
- `packages/engine/src/types.ts`
- `packages/engine/src/GameEngine.ts`
- `packages/engine/src/utils/narrative.ts`
- Tests: `packages/engine/src/__tests__/TickSystem.test.ts`, `XPTable.test.ts`

**Exit criteria:** JVM unit tests in V2 `:core` cover XP + tick invariants comparable to engine tests.

---

## Phase 3 — UI shell (Compose)

**Goal:** Screens that **display** engine-driven state (read-only or stub actions).

- Skills grid / pillars (visual parity with design intent, not pixel-perfect clone).
- Bank, Combat, Settings placeholders wired to `ViewModel` + `StateFlow`.
- Theme: start from `DOCU/THEMING.md` / `DOCU/zhip-ai-styling.md` tokens (manual Compose colors first).

**Primary sources (main repo, UI reference only):**

- `apps/mobile/app/(tabs)/` — tab layout patterns.
- `apps/mobile/constants/theme.ts` — palette numbers.
- `apps/mobile/store/gameSlice.ts` — field names for future parity (port gradually in Phase 4–5).

**Exit criteria:** Navigate between main tabs; state hoisted in ViewModels (even if mock data).

`[DONE 2026-03-31]:` `GameScreen` bottom navigation switches tabs (`Skills`, `Bank`, `Combat`, `Settings`) and state is hoisted through `GameViewModel` (`StateFlow`), with account flow handoff from `ArteriaApp`.

---

## Phase 4 — Persistence + offline

**Goal:** Save/load player state; deterministic migrations; WYWA-style report data structures.

- Choose storage: **DataStore** (protobuf/json) and/or **MMKV** NDK — document choice in `SBOM.md` when added.
- Port migration mindset from `apps/mobile/store/` (migrations in `gameSlice` / persistence layer).
- Offline window: port cap logic and report DTOs from `useGameLoop.ts` / WYWA components conceptually.

**Primary sources:**

- `apps/mobile/hooks/useGameLoop.ts` — tick loop, offline processing, combat ticks.
- `apps/mobile/hooks/usePersistence.ts` (if present) / `store/persistence.ts`
- `apps/mobile/components/WhileYouWereAway.tsx` — UX contract for report fields.

**Exit criteria:** Cold start restores state; instrumented or unit tests for migration round-trip.

`[AMENDED 2026-03-31]:` Persistence/offline is partially delivered via Room (`arteria_profiles.db`, `arteria_game.db`) with save/load and offline catch-up report (`OfflineReportDialog`). Migration round-trip coverage and manual smoke remain open.

**`[DONE 2026-03-31]:`** Phase 4 exit criteria met for the native Room stack: `GameDatabase` **v2** with `MIGRATION_1_2` (`game_meta.lastOfflineTickAppliedAt`); `GameRepository` + `GameViewModel` persist offline audit timestamp after catch-up; JVM test `saveGameState_roundTripsLastOfflineTickAppliedAt`; instrumented `GameDatabaseMigrationTest` (v1→v2 migration + save/close/reopen round-trip). Optional future: DataStore/MMKV called out in original bullets remains **not** required for exit — add to `SBOM.md` if introduced.

---

## Phase 5 — Content port

**Goal:** Data-driven skills, nodes, recipes, enemies — backed by Kotlin definitions or assets.

- Port tables from `packages/engine/src/data/*.ts` and `apps/mobile/constants/*.ts` as needed (mining, logging, items, combat).
- Wire `useGameLoop`-equivalent orchestration in a **single** Android-side loop (coroutine + clock).

**Primary sources:**

- `packages/engine/src/data/mining.ts`, `logging.ts`, `fishing.ts`, `quests.ts`, `story.ts`, etc.
- `apps/mobile/constants/items.ts`, skill screens under `apps/mobile/app/skills/`.

**Exit criteria:** At least one vertical slice (e.g. Mining + Bank) playable end-to-end offline.

`[IN PROGRESS 2026-03-31]:` Mining + Bank loop is present (`TickEngine`, `MiningData`, `GameViewModel`, `BankScreen`) and account->game routing is wired. Remaining to call this fully done: manual smoke checklist and explicit balancing/UX pass for the slice.

---

## [AMENDED 2026-03-31] Immediate Next Point (single focus)

- [ ] Run **manual smoke pass** (no automated tests required): account create/select -> enter game -> start Mining action -> confirm XP/resource gain -> bank reflects resources -> return to accounts.
- [ ] If smoke is clean, mark Phase 5 vertical slice **DONE** and open the next slice candidate (Logging or Combat baseline).

---

## Phase 6 — Polish and release engineering

**Goal:** Production hygiene.

- Enable R8/minify for release; keep rules for any reflection/native JNI.
- Sound (e.g. Oboe or Android audio), haptics, accessibility labels on Compose.
- Play listing: signing config, versionCode policy, privacy/data safety notes.
- Optional: retain C++/OpenGL subview for effects — compose over GL per `ARCHITECTURE.md`.

**Primary sources:**

- `apps/mobile/android/app/proguard-rules.pro` — patterns for native/RN (adapt, do not copy blindly).
- Root `2_Build_APK_Local.bat` — inspiration for a V2-specific script when stable.

**Exit criteria:** Release build produces uploadable AAB; smoke checklist documented in `SCRATCHPAD.md`.

---

## Phase 7 — UX parity, accessibility, and QoL (Compose)

**Goal:** Player-facing polish comparable to RN app conventions — without blocking engine completeness.

- [ ] **A11y:** TalkBack labels on all primary actions; focus order matches visual order; color contrast checks against `THEMING.md` tokens.
- [ ] **Motion:** User-toggleable reduced motion (respect `android.settings.ACCESSIBILITY` / Compose `ReduceMotion`); document behavior in `SCRATCHPAD.md` when implemented.
- [ ] **Feedback:** Haptic mapping table (RN `expo-haptics` → Android `HapticFeedback` / `Vibrator`) for level-up, rare drop, errors.
- [ ] **Navigation:** Loading / empty / error states on every major route; deep link stubs for Settings and Bank where applicable.
- [ ] **Bank (when data exists):** Search, sort modes, withdraw presets (X / All), stack tooltips — parity with `apps/mobile` bank UX intent.
- [ ] **Combat (when wired):** Readable enemy state, fight speed indicator, equipment strip mirroring RN combat screen intent.
- [ ] **WYWA / offline report:** Field parity with `WhileYouWereAway` contract (time away, XP gained, resources, combat summary when engine supports).
- [ ] **Profile hub:** Account / progress snapshot / entitlements surface aligned with RN Profile (v0.6.x) when persistence exists.
- [ ] **Exploration:** Expedition cards — level lock clarity, discovery item tooltips, mastery hooks per `WORLD_EXPLORATION.md`.
- [ ] **Crafting queue:** When engine supports queued actions, multi-step progress UI and cancel/skip affordances.

**Primary references:** `../DOCU/IMPROVEMENTS.md`, `../DOCU/CURRENT_IMPROVEMENTS.md`, `apps/mobile/components/WhileYouWereAway.tsx`, bank/combat screens under `apps/mobile/app/`.

---

## Phase 8 — Visual, thematic, and UI-system alignment

**Goal:** Compose look-and-feel matches Arteria art direction and RN reference implementation.

- [ ] **Tokens:** Single source of Compose `Color` / typography matching `apps/mobile/constants/theme.ts` + `DOCU/THEMING.md` + `zhip-ai-styling.md`.
- [ ] **Tabs / pillars:** Skills grid and tab bar semantics aligned with `apps/mobile/app/(tabs)/` structure.
- [ ] **Crafting flagship:** Radial Mastery paradigm per `DOCU/UI_REVISION_CRAFTING_v2.md` (wheel, tier nodes, center orb, detail dock).
- [ ] **Docking / account flow:** Continue parity with `character-select.tsx` — glass cards, starfield, typography (Cinzel for display type).
- [ ] **Toasts / banners:** Level-up, rare drops, mastery unlocks — non-blocking surfaces consistent with RN patterns.
- [ ] **Icons:** Replace placeholder glyphs with vector assets or shared icon set decision (document in `SBOM.md` or `REFERENCES.md`).

**Exit criteria:** Design review checklist in `SCRATCHPAD.md` signed off for “default theme” pass.

---

## Phase 9 — Feature verticals (content-driven)

**Goal:** Ship playable slices that mirror high-value RN features from `MASTER_DESIGN_DOC.md` and `DOCU/ROADMAP.md`.

- [ ] **Skills:** Vertical slices in dependency order (e.g. Mining + Bank first, then Logging, Fishing…) per Phase 5 — plus training hints and mastery UI.
- [ ] **Dungeons / instances:** UI shells for Delves vs Expeditions timers and rewards (logic can stub) per GDD combat sections.
- [ ] **Companions:** Roster and role labels per `DOCU/COMPANIONS.md`.
- [ ] **Economy surfaces:** Lumina shop, patron / entitlements placeholders tied to future persistence keys.
- [ ] **Chronicle / achievements:** Read-only milestone list when state exists.
- [ ] **Random events:** Lightweight modal or banner pattern for absurdity events when engine hooks land.
- [ ] **Skill pets:** Collection / bestiary stub tied to drop tables when ported.

---

## Phase 10 — Performance, quality gates, and store readiness

**Goal:** Comparable or better cold start and binary discipline vs RN release; Play-ready artifacts.

- [ ] **Startup:** Cold start benchmark documented (target: within agreed % of RN `assembleRelease` baseline on same device class).
- [ ] **R8 / ProGuard:** Rules for reflection, serialization, JNI — reviewed when persistence format stabilizes.
- [ ] **Testing:** JVM tests for `core` invariants; minimal Compose screenshot or semantics tests for regressions on critical flows.
- [ ] **Play Console:** Data safety, signing, versionCode policy, internal testing track checklist in `SCRATCHPAD.md`.

---

## Cross-links

- **Doc hub / AI start here:** [SUMMARY.md](SUMMARY.md)

- **How to translate RN code:** [MIGRATION_SPEC.md](MIGRATION_SPEC.md)
- **Stack truth:** [ARCHITECTURE.md](ARCHITECTURE.md)
- **Dependency versions:** [SBOM.md](SBOM.md)
- **External docs:** [REFERENCES.md](REFERENCES.md)
