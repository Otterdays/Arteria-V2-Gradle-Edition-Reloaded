<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | Settings backlog: DataStore `UserPreferencesRepository`, `MainActivity` `ArteriaRoot` (theme/system + reduce motion locals), light `ArteriaTheme` + space brush; `GameViewModel` + `UserPreferencesProvider`, offline cap/report prefs; `GameRepository` reset/delete; profile `deleteById`; OSS/Credits overlays; danger zone; verification snapshot amended. |
| 2026-04-01 | Cursor Agent | Composer | Settings V1 parity slice: profile rename (`ProfileDao.updateDisplayName`, `AccountViewModel`), `AccountSessionInfo` + `GameScreen`/`ArteriaApp` wiring; `SettingsScreen` About (`BuildConfig`), tick/save cadence copy, test sound; verification snapshot line amended. |
| 2026-04-01 | Cursor Agent | Composer | Content note: `HerbloreData` / `ScavengingData` + registry wiring; verification snapshot amended for harvest→herb craft chain. |
| 2026-03-31 | Cursor Agent | Composer | Doc sync: verification snapshot + Screen UI row for `SkillComingSoonDialog`, `SkillDataRegistry.isSkillImplemented`, `OfflineReportDialog` overlay pattern. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Phase 4 persistence: `GameDatabase` v2 + `MIGRATION_1_2`; `game_meta.lastOfflineTickAppliedAt`; verification note appended below. |
| 2026-03-31 | Claude Haiku 4.5 | Anthropic Claude | Updated Agent Credits + `Last updated` date. Animation system entry: `DockingGlitch.kt` (`GlitchMaterializeOverlay` 7-layer Canvas, `AmbientScanOverlay`, `EntryAnimations` orchestrator, `AmbientAnimations` infinite loops, `GlitchLayout` precomputed deterministic per seed); staggered per-card entry (index × 160ms), materialize/scan/glitch/jitter phases, badge spring pop-in; ambient system (infinite ripple/pulse/flow on selected). |
| 2026-03-31 | Claude Sonnet 4.6 | Anthropic Claude | Added UI Components layer to Responsibilities table documenting `DockingGlitch`, `DockingAccountCard`, `DockingBackground`, `DockingChrome` with their roles and animation capabilities. |
| 2026-03-31 | Claude Haiku 4.5 | Anthropic Claude | Main menu refactor: moved Settings from bottom nav tab to full-screen overlay (opened via `TopAppBar` gear icon); updated game screen navigation to 3 primary tabs (Skills, Bank, Combat). |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Complete architecture doc rebuild for clarity; split into Current Architecture, Boundaries, and Planned Next Architecture. |

*Future contributors: append a row here when you materially change this doc.*

---

# Arteria V2 Gradle Edition Reloaded — Architecture

> Last updated: 2026-04-01
> Status: Active native Android implementation (Kotlin + Compose + Room) with full Docking Station animation system
> Scope: This file documents the architecture that exists today and the planned next architecture.

## [AMENDED 2026-03-31] Verification Snapshot

- **Verified against live code:** `ui/ArteriaApp.kt`, `navigation/NavRoutes.kt`, `ui/game/GameScreen.kt`, `ui/game/SettingsScreen.kt`.
- **Active nav routes:** `account_select`, `account_create`, `game/{profileId}` (encoded by `NavRoutes.gamePath()`).
- **Game shell shape:** 3-tab content (`Skills`, `Bank`, `Combat`) + `TopAppBar` settings entry.
- **`[AMENDED 2026-03-31]:`** **Skills tab:** taps on skills with no actions in `SkillDataRegistry` show `SkillComingSoonDialog` (Compose `Dialog`, back-dismissible); implemented skills still push `SkillDetailScreen` via `GameScreen` `AnimatedContent`. Same overlay family as `OfflineReportDialog` (offline gains).
- **`[AMENDED 2026-04-01]:`** **Skill content:** `HerbloreData` brews potions using `HarvestingData` item ids in `SkillAction.inputItems` (consumed in `TickEngine` from bank); `ScavengingData` is pure gathering like `LoggingData`.
- **Settings flow:** full-screen overlay, back-dismissible, with nested `ChangelogScreen` overlay from “What's New”.
- **`[AMENDED 2026-04-01]:`** **Settings parity slice:** profile display name edit (Room `profiles.displayName`), last-played line, `BuildConfig` version string, informational tick/save cadence (`GameViewModel` constants), **Test sound** (`ToneGenerator`); rename + session refresh flow: `ArteriaApp` holds `AccountSessionInfo`, `GameScreen` passes `onRenameDisplayName` / `onRefreshAccountSession`.
- **`[AMENDED 2026-04-01]:`** **Settings expansion:** `user_preferences` DataStore (`ThemePreference`, motion, haptics, sound, offline report, soundscapes stub, DEBUG offline-cap bypass); `LocalUserPreferencesRepository`, `LocalArteriaDarkSpace`, `LocalReduceMotion`; `GameRepository.resetProgressForProfile` / `deleteAllGameDataForProfile`; `ProfileDao.deleteById`; nested **Open source notices** (`ARTERIA_OSS_NOTICES`) + **Credits** screens; danger dialogs (reset progress, delete profile).
- **Doc canon reference:** `DOCS/SUMMARY.md` section `Doc Canon (single source rules)`.
- **`[AMENDED 2026-03-31]:`** `GameDatabase` is **version 2** (`MIGRATION_1_2`); `game_meta` carries `lastOfflineTickAppliedAt` for offline catch-up audit. See `DOCS/SBOM.md` Android Targets table.

---

## 1) At A Glance

| Item | Value |
|------|-------|
| Repository root | `Arteria-V2-Gradle-Edition-Reloaded/` |
| App package | `com.arteria.game` |
| Core package | `com.arteria.game.core` |
| UI stack | Jetpack Compose + Navigation Compose |
| Persistence | Room (`arteria_profiles.db`, `arteria_game.db`) |
| Modules | `:app` (active), `:core` (present, minimal) |
| Toolchain | Gradle 9.6 nightly, AGP 9.1, JDK 21 |
| Current gameplay state | Account/profile flow live + in-app game hub (bottom nav: Skills/Bank/Combat; settings via `TopAppBar` overlay) |

---

## 2) Current Runtime Architecture (Implemented)

```text
Android OS
  -> MainActivity (ComponentActivity)
    -> setContent { ArteriaTheme { ArteriaApp() } }
      -> NavHost (Navigation Compose)
        -> account_select
        -> account_create
        -> game/{profileId}
      -> AccountViewModel (StateFlow UI state)
        -> ProfileRepository (interface)
          -> RoomProfileRepository (implementation)
            -> ProfileDatabase (Room)
              -> ProfileDao
                -> SQLite file: arteria_profiles.db
      -> GameViewModel (StateFlow tick state)
        -> GameRepository
          -> GameDatabase (Room)
            -> GameDao
              -> SQLite file: arteria_game.db
```

### Responsibilities

| Layer | Main files | Responsibility |
|------|------------|----------------|
| Entry/UI host | `app/src/main/java/com/arteria/game/MainActivity.kt` | Application entry; Compose host setup |
| Navigation/UI composition | `app/src/main/java/com/arteria/game/ui/ArteriaApp.kt` | Builds `NavHost`, wires VM to screens, route transitions |
| App entry / theme locals | `app/src/main/java/com/arteria/game/MainActivity.kt` | `ArteriaRoot`: collects DataStore prefs, resolves light/dark + reduce motion, provides `LocalUserPreferencesRepository` / `LocalArteriaDarkSpace` / `LocalReduceMotion`, wraps `ArteriaTheme` |
| User preferences (DataStore) | `app/src/main/java/com/arteria/game/data/preferences/*` | `UserPreferences`, `UserPreferencesRepository` (`user_preferences` file), `UserPreferencesProvider` adapter for `GameViewModel` |
| Screen UI | `app/src/main/java/com/arteria/game/ui/account/*`, `app/src/main/java/com/arteria/game/ui/game/*` | Account flow (select/create); game hub with bottom nav (Skills/Bank/Combat tabs), `TopAppBar` (account name + settings gear), `SettingsScreen` overlay (full-screen, back-dismissible), `OfflineReportDialog` / `SkillComingSoonDialog` (modal dialogs over hub), `SkillDetailScreen` (in-tab push for implemented skills) |
| UI Components | `app/src/main/java/com/arteria/game/ui/components/*` | `DockingBackground` (animated space backdrop), `DockingAccountCard` (glitch materialization + timeline sidebar + ambient selected-state), `DockingGlitch` (7-layer Canvas animation system: `GlitchMaterializeOverlay`, `AmbientScanOverlay`, `EntryAnimations`, `AmbientAnimations`, `GlitchLayout`), `DockingChrome` (title block, lore footer, new account slot) |
| State orchestration | `app/src/main/java/com/arteria/game/ui/account/AccountViewModel.kt` | Validates input, handles user actions, updates UI state, executes repository operations; **`[AMENDED 2026-04-01]:`** `resolveSession`, `updateDisplayName` for in-game settings rename |
| Navigation contracts | `app/src/main/java/com/arteria/game/navigation/NavRoutes.kt` | Route constants and encoded route-builder API |
| Persistence boundary | `app/src/main/java/com/arteria/game/data/profile/ProfileRepository.kt` | App-facing persistence contract |
| Persistence implementation | `app/src/main/java/com/arteria/game/data/profile/RoomProfileRepository.kt` | Room-backed implementation of profile operations |
| Database schema/access | `app/src/main/java/com/arteria/game/data/profile/ProfileEntity.kt`, `ProfileDao.kt`, `ProfileDatabase.kt` | Entity model, queries, and Room database definition |
| Game data persistence | `app/src/main/java/com/arteria/game/data/game/*` | Room-backed game state persistence per profile |
| Gameplay domain/tick | `app/src/main/java/com/arteria/game/core/*` | Skill model, XP table, action data, and tick/offline simulation engine |
| Reusable module | `core/build.gradle.kts` | Reserved for extracted engine/domain code; currently minimal |

---

## 3) Current Behavior Flows

### Startup

1. `MainActivity` launches Compose content.
2. `ArteriaApp` creates Room database and repository.
3. `AccountViewModel` starts observing profiles from repository.
4. UI state is exposed via `StateFlow` and consumed with lifecycle-aware collection.

### Account Creation

1. User enters a display name on account creation screen.
2. `AccountViewModel` validates (length + allowed characters).
3. Repository persists profile through Room.
4. UI state updates selected account and clears errors.
5. Navigation returns to account selection.

### Continue Session

1. User selects an account and taps continue.
2. `AccountViewModel` marks selected profile as active and updates last-played timestamp.
3. App navigates to `game/{profileId}` using URL-safe encoded id.
4. `GameViewModel` loads game state from `arteria_game.db`, processes offline progress, and starts the active tick loop.

---

## 4) Architecture Boundaries

### Boundary rules used by this codebase

- UI composables do not talk directly to Room DAOs.
- ViewModel depends on `ProfileRepository`, not concrete Room types.
- Route generation is centralized in `NavRoutes` to avoid string duplication.
- Persistence and navigation side effects happen in ViewModel/coroutines, not in composable rendering paths.

### Why this matters

- Keeps UI easier to iterate quickly without persistence coupling.
- Makes data layer replaceable (Room today, another source later).
- Makes route contracts explicit and safer (`profileId` encoding).

---

## 5) Build And Runtime Constraints

| Constraint | Current rule |
|-----------|---------------|
| JVM | JDK 21 required for Gradle daemon and compile targets |
| Android targets | `compileSdk` 36.1, `targetSdk` 36, `minSdk` 26 |
| Native engine | No active NDK/CMake/OpenGL wiring in current modules |
| Package stack | Native Android only in this repository; no Expo/RN dependencies in module builds |

---

## 6) Planned Next Architecture (Not Yet Implemented)

### Phase 1 completion (near-term)

- Harden the new game hub path (`game/{profileId}`) with tests around route/decode and profile switching.
- Expand beyond current hub stubs by adding first playable combat/skill interactions.
- Keep current boundary pattern: composables -> ViewModel -> repository contracts.

### Phase 2+ domain extraction

- Move reusable game/domain logic into `:core`.
- Keep `:core` free of Android framework APIs where possible.
- Use clear adapter seams between Compose/UI state and engine/domain state.

### Optional Phase 5+ GPU island

- C++/OpenGL component may be added for high-performance rendering.
- This will be additive architecture, not a replacement for existing UI/navigation shell.
- If activated, update this document and `DOCS/SBOM.md` in the same change.

---

## 7) Minimal Source Map

```text
app/src/main/java/com/arteria/game/
  MainActivity.kt
  navigation/NavRoutes.kt
  ui/ArteriaApp.kt
  ui/account/*
  ui/game/*
  ui/components/*
  ui/theme/*
  data/profile/*
  data/game/*
  core/*

core/
  build.gradle.kts  (module exists; domain expansion target)
```

---

## 8) Conventions That Drive Architecture

- Keep async and I/O work in coroutine boundaries (ViewModel/repository).
- Fail fast at boundaries and surface user-visible errors through UI state.
- Prefer typed state over implicit UI branching.
- Keep architecture docs aligned with code reality; no speculative sections without explicit "planned" labeling.

---

## 9) Related Docs

- `DOCS/SUMMARY.md` -> orientation and read order
- `DOCS/SCRATCHPAD.md` -> live status and next actions
- `DOCS/ROADMAP.md` -> delivery phases
- `DOCS/MIGRATION_SPEC.md` -> logic-port guidance
- `DOCS/SBOM.md` -> exact dependencies and toolchain versions
