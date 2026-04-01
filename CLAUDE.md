# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

## Philosophy & Agent Behavior

**KISS** · **YAGNI** · **DRY** (abstract after 3rd repeat) · **Fail Fast** (surface errors at the boundary)

- **Autonomy:** If a decision is reversible and low-risk, act. If irreversible or ambiguous, ask first.
- **Read before edit:** Always read a file before modifying it. Never assume structure or contents.
- **Multi-file changes:** Start from leaf dependencies and work toward the entry point (e.g., model → dao → repository → viewmodel → screen).
- **Scope discipline:** Do not rename, refactor, or modify anything outside the current task. Flag out-of-scope observations in `DOCS/SCRATCHPAD.md` under `Out-of-Scope Observations`.
- **Hallucination guard:** Never assume a method, API, or flag exists — verify against installed version. If unverifiable, leave a `NOTE: verify before shipping` comment.
- **No tests unless asked:** Do not run unit or instrumented tests in agent sessions unless explicitly requested (user preference, 2026-03-31).

---

## Workflow

**Session init order (read before coding):**
1. `DOCS/SUMMARY.md` — status overview
2. `DOCS/SBOM.md` — current dependency versions
3. `DOCS/SCRATCHPAD.md` — live blockers and last actions
4. `DOCS/ARTERIA-V1-DOCS/DOCU/STYLE_GUIDE.md` — conventions

**During work:** Update `SCRATCHPAD.md` as you go — not just at the end. Checkpoint after every 3–5 tool calls or after each subtask.

**Errors:** Log to `DOCS/debugs/debug_[timestamp].md`. Max 3 fix attempts. If still stuck, stop — report what was tried and what failed in SCRATCHPAD, then surface to the user.

**Handoff:** Always update `SCRATCHPAD.md` (Last Actions + Agent Credits + Next Action) and any relevant docs before ending a session.

**Policy alignment:** `.claude/cursor.rules` is the concise policy baseline; this file is the implementation guide.
If any instruction appears to conflict, follow the stricter safety rule and add an `[AMENDED YYYY-MM-DD]`
clarification to the touched doc.

---

## Build & Test Commands

```bash
# Build debug APK
./gradlew :app:assembleDebug

# Build and install to connected device/emulator
./gradlew :app:installDebug

# JVM unit tests (fast, no device needed)
./gradlew :app:testDebugUnitTest

# Single test class
./gradlew :app:testDebugUnitTest --tests com.arteria.game.data.game.GameRepositoryTest

# Device/emulator tests (Room, navigation)
./gradlew :app:connectedAndroidTest

# Pure Kotlin core module tests
./gradlew :core:test

# Clean build cache
./gradlew clean

# Full rebuild
./gradlew clean :app:assembleDebug

# Check Gradle/JVM version
./gradlew --version
```

**Windows gotchas:**
- `FileSystemException` on `.jar`: Close Android Studio, run `./gradlew --stop`, retry.
- `JAVA_COMPILER` missing: Machine has a JRE, not a JDK. Point `org.gradle.java.home` in `gradle.properties` to a full **JDK 21** install.
- `jlink.exe does not exist` under `.cursor\extensions\redhat.java\...`: **JAVA_HOME** is a JRE. Repo `gradlew.bat` strips `.cursor` / `redhat.java` paths, requires `bin\jlink.exe`, and scans `%ProgramFiles%\Java\jdk-*`, **Eclipse Adoptium\jdk-***, **Microsoft\jdk-***; it no longer falls back to `java` on PATH (that was still Cursor’s JRE). `build-apk-for-transfer.ps1` sets the same **JAVA_HOME** before Gradle. Run `gradlew --stop` after JDK changes.

---

## Architecture

### Navigation tree

```
MainActivity (ComponentActivity)
  └── ArteriaTheme
        └── ArteriaApp (NavHost)
              ├── account_select   → AccountSelectionScreen
              ├── account_create   → AccountCreationScreen
              └── game/{profileId} → GameScreen
                    ├── SkillsScreen        (tab 0)
                    ├── BankScreen          (tab 1)
                    ├── CombatScreen        (tab 2)
                    ├── TopAppBar           (account name + ⚙ icon)
                    └── SettingsScreen      (full-screen overlay, BackHandler dismissible)
```

**Route constants** (`NavRoutes.kt`):
- `account_select`, `account_create`, `game/{profileId}`
- Use `NavRoutes.gamePath(profileId)` — URI-encodes the ID safely.

### Module layout

| Module | Rule | Key paths |
|--------|------|-----------|
| `:app` | Android UI + persistence | `ui/`, `data/`, `navigation/`, `core/` (temporary home) |
| `:core` | **Zero Android imports** — pure Kotlin engine | future home of `TickEngine`, `XPTable`, `GameModels` |

### Persistence — two Room databases

| Database | File | Tables |
|----------|------|--------|
| `arteria_profiles.db` | Who you are | `profiles` |
| `arteria_game.db` | What you've done | `skill_states`, `bank_items`, `game_meta` |

- `GameRepository.saveGameState()` is the **only safe write path** — it wraps everything in `withTransaction`.
- `AccountViewModel` owns profile state via `StateFlow<AccountUiState>`.
- `GameViewModel` owns game state, tick loop, and offline report.

### Skill system

**16 skills across 4 pillars** (defined in `SkillId.kt`):

| Pillar | Skills |
|--------|--------|
| GATHERING | Mining, Logging, Fishing, Harvesting, Scavenging |
| CRAFTING | Smithing, Cooking, Runecrafting, Herblore, Forging |
| COMBAT | Attack, Strength, Defence, Hitpoints |
| SUPPORT | Agility, Wizardry |

`SkillId.byPillar(pillar)` returns all skills for a given pillar. Each `SkillId` carries `displayName`, `pillar`, and `description`.

**Implemented vs Coming Soon:** `SkillDataRegistry.isSkillImplemented(skillId)` is true when `actionsForSkill` is non-empty. `GameScreen` shows `SkillComingSoonDialog` for unimplemented taps; `SkillDetailScreen` still has an empty-actions fallback if opened without actions.

### Core data models (`GameModels.kt`)

```kotlin
SkillAction(id, skillId, name, levelRequired, xpPerAction, actionTimeMs, resourceId?, resourceAmount, inputItems)
SkillState(skillId, xp, isTraining, currentActionId?, actionProgressMs)
GameState(profileId, skills: Map<SkillId, SkillState>, bank: Map<String, Int>, lastSaveTimestamp)
TickResult(state, xpGained, resourcesGained, levelUps)
LevelUp(skillId, oldLevel, newLevel)
```

`inputItems` is `Map<String, Int>` (bank deduction per completed action in `TickEngine`); empty for pure gathering.

Use `data class` for all models. Use `sealed class` for UI state variants.

---

## Coding Standards

### Limits

| Rule | Limit |
|------|-------|
| Line length | 100 chars |
| Function length | 50 lines |
| File length | 400 lines |

### Kotlin / Android specifics

- **MVVM strictly:** No business logic in Activities or Composables — delegate to ViewModels.
- **Coroutines:** Use `viewModelScope` or `lifecycleScope`. **Never `GlobalScope`.**
- **State:** Expose via `StateFlow` (cold, replay 1). Use `SharedFlow` for one-shot events (e.g., `levelUpEvents`).
- **Models:** `data class` for state, `sealed class` for UI state variants, `enum class` for fixed sets (SkillId, SkillPillar).
- **Sealed results over nulls:** Prefer `sealed class Result` / Kotlin's `Result<T>` over returning `null` when something can fail.

### Error handling

- Use `try/catch` (or `runCatching`) at coroutine boundaries and I/O operations.
- Never silently swallow exceptions — log or propagate.
- Fail fast at the system boundary; recover gracefully in the domain.
- Room and repository calls should propagate exceptions to the ViewModel, which maps them to UI state error fields.

### Async / concurrency

- Never block the main/UI thread.
- Offline tick simulation runs on `Dispatchers.Default` (already implemented in `GameViewModel`).
- Room queries use `Dispatchers.IO` automatically via Room's coroutine integration.

### Trace tags

Link code to documentation whenever a file implements documented behavior:

```kotlin
// [TRACE: DOCS/SCRATCHPAD.md — startup account/session persistence]
// [TRACE: DOCS/ARCHITECTURE.md — offline tick processing]
```

### Comments

Write **WHY**, never WHAT. Let types document structure.

| Prefix | Use |
|--------|-----|
| `TODO:` | Planned work not yet started |
| `FIXME:` | Known bug or hack |
| `NOTE:` | Non-obvious rationale or caveat |

### Git conventions (Conventional Commits)

```
feat(account): add profile avatar selection
fix(tick): clamp offline duration to 7 days
docs(arch): update navigation tree for settings overlay
refactor(game): extract XPTable to :core module
chore(deps): bump Compose BOM to 2026.03.01
test(repo): add GameRepository save/load round-trip tests
```

Branches: `feature/` · `fix/` · `chore/` · `docs/`

---

## Theme & Design System

### Color palette — `ArteriaPalette` only, no hardcoded hex

| Token group | Tokens |
|-------------|--------|
| Background | `BgDeepSpaceTop`, `BgDeepSpaceMid`, `BgDeepSpaceBottom`, `BgApp`, `BgCard`, `BgCardHover`, `BgInput` |
| Text | `TextPrimary`, `TextSecondary`, `TextMuted` |
| Accent | `AccentPrimary`, `AccentHover`, `AccentWeb`, `Gold`, `GoldDim` |
| Surfaces | `Border`, `Divider` |
| Special | `LuminarEnd` (blue), `VoidAccent` (purple), `BalancedEnd` (green) — used for pillar gradients |

### Typography — `MaterialTheme.typography.*` only

- **Cinzel** (bundled variable font): `displaySmall`, `headlineMedium`, `titleLarge`, `titleMedium`, `labelSmall`
- **Sans-serif**: `bodyLarge`, `bodyMedium`, `bodySmall`

Never set `fontFamily` in component code — it's wired in `ArteriaTheme.kt`.

### UI Component library (`ui/components/`)

| Component | File | Purpose |
|-----------|------|---------|
| `DockingBackground` | `DockingBackground.kt` | Animated space backdrop (stars, nebula, gradient breath) — account/settings screens |
| `DockingAccountCard` | `DockingAccountCard.kt` | Account card with full glitch materialization + ambient selected-state animations + timeline sidebar |
| `DockingGlitch` | `DockingGlitch.kt` | Animation system: `GlitchLayout` (precomputed per-card), `EntryAnimations`, `AmbientAnimations`, `GlitchMaterializeOverlay`, `AmbientScanOverlay` |
| `DockingChrome` | `DockingChrome.kt` | Title block, new account slot, lore footer |

**DockingAccountCard animation layers** (entry sequence per card, staggered by `entryIndex × 160ms`):
1. Alpha materialize (550ms)
2. CRT scan sweep top→bottom (480ms, +40ms delay)
3. Glitch intensity 1→0: RGB aberration, block artifacts, noise grain, horizontal jitter (650ms)
4. Hex ghost text overlays real name during glitch
5. Accent stripe flashes wide then settles
6. Stabilization flicker near glitch end
7. Skill pillar badges spring pop-in with overshoot (after 420ms)

**Ambient (selected card, infinite):** slow scan drift, border pulse, timeline node breathe + ripple ring, energy flow particle on connector line.

**Gradient sets** (`dockingGradientForIndex(index)`): cycles through blue (Luminar), purple (Void), green (Balanced) by `index % 3`.

---

## Current Screen Structure

**Account Selection (`AccountSelectionScreen.kt`):**
- `DockingBackground` fills the screen
- `LazyColumn` of `DockingAccountCard` items with timeline sidebar + terminal node
- "Enter timeline" CTA button — enabled only when an account is selected
- `DockingLoreFooter` at bottom

**Game Screen (`GameScreen.kt`):**
- 3-tab bottom nav: Skills · Bank · Combat
- `TopAppBar` (transparent, Cinzel account name, gear icon → `showSettings = true`)
- Settings rendered as conditional full-screen composable with `BackHandler`
- Unimplemented skill tap → `SkillComingSoonDialog`; implemented → `SkillDetailScreen` (`AnimatedContent` push)
- Space gradient brush on container background

**Settings (`SettingsScreen.kt`):**
- Full-screen overlay with space gradient background
- Back arrow + "SETTINGS" label in top row
- Profile card (name, game mode) + Game info card + "Switch Account" button

---

## Game State & Offline Processing

- **TickEngine** (`core/engine/TickEngine.kt`): Simulates accumulated offline ticks from `lastSaveTimestamp` to now.
- **GameViewModel** calls tick simulation on startup, emits `offlineReport`, fires `levelUpEvents` as `SharedFlow`.
- **Offline report dialog** (`OfflineReportDialog.kt`): Shows only when `xpGained` or `resourcesGained` is non-empty.
- **Coming Soon dialog** (`SkillComingSoonDialog.kt`): Shown when the user taps a skill with no registry actions (`SkillDataRegistry.isSkillImplemented` is false).
- **Tick loop in GameViewModel:** `enableTickLoop` constructor param — set `false` in tests to prevent `runTest` hangs.

---

## Doc Update Rules

> These come from `.claude/cursor.rules` — they apply to all doc edits.

- **Never delete existing content.** Append, amend with `[AMENDED YYYY-MM-DD]:`, or annotate.
- **Every doc in `DOCS/` must keep this header:**
  ```
  <!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->
  ```
- **Update order:** New entries go to the **top** of their section (most-recent-first).
- **Status docs** (`SCRATCHPAD.md`, `SBOM.md`) — update proactively as part of normal work.
- **Content docs** (`ARCHITECTURE.md`, `README.md`, `STYLE_GUIDE.md`) — only amend when explicitly instructed.
- **SCRATCHPAD.md:** Compact at 500 lines. Always update Agent Credits + Last Actions + Next Action at session end.
- **SBOM.md:** Update immediately when any package is added or removed.

---

## Common Development Tasks

### Adding a new skill screen
1. Create `ui/game/NewSkillScreen.kt`
2. Reference it in `GameScreen.kt`'s `AnimatedContent` `when` block
3. Add a `NavRoutes` entry only if it needs its own route (sub-tabs don't)
4. Amend `DOCS/ARCHITECTURE.md` if navigation structure changed

### Adding a new skill to the game
1. Add entry to `SkillId` enum with `displayName`, `pillar`, `description`
2. Add action data in `core/data/` (gathering: `MiningData.kt` / `HarvestingData.kt` / `ScavengingData.kt`; bank inputs → output: `CookingData.kt` / `HerbloreData.kt` with `inputItems`)
3. Add `SkillState` initialization in `GameRepository` load path
4. Add `SkillStateEntity` rows will be created automatically on first save

### Updating game state schema
1. Edit `GameEntities.kt` — add field(s)
2. Bump `version` in `GameDatabase.kt`
3. Add `Migration(old, new)` object and register it in `GameDatabase.kt`
4. Update `GameRepository.kt` to map the new field
5. Update `GameViewModel.kt` to expose it
6. Test with `./gradlew :app:connectedAndroidTest`

### Adding a UI component
1. Create in `ui/components/`
2. Use only `ArteriaPalette` tokens — no hardcoded hex
3. Use `MaterialTheme.typography.*` — no inline font overrides
4. Keep under 400 lines; split into sub-files if the animation system is complex (see `DockingGlitch.kt` pattern)

---

## Output Checklist

Before considering any task complete:

- [ ] Runnable without modification
- [ ] No placeholder `TODO` left in delivered code
- [ ] No hardcoded hex colors — `ArteriaPalette` only
- [ ] No `GlobalScope` — `viewModelScope` / `lifecycleScope` only
- [ ] Error handling at coroutine boundaries and I/O
- [ ] No silent exception swallowing
- [ ] Docs updated: `SCRATCHPAD.md` Agent Credits + Last Actions
- [ ] `SBOM.md` updated if any dependency changed
- [ ] Nothing deleted from any doc
- [ ] Commit message follows Conventional Commits format

---

## Key Files

| File | Purpose |
|------|---------|
| `ui/ArteriaApp.kt` | NavHost root; wires DBs, VMs, route transitions |
| `ui/game/GameScreen.kt` | 3-tab hub + TopAppBar + settings overlay |
| `ui/game/GameViewModel.kt` | Tick loop, offline processing, level-up events |
| `ui/account/AccountViewModel.kt` | Profile CRUD + selection state |
| `ui/account/AccountSelectionScreen.kt` | Docking station account chooser |
| `ui/components/DockingAccountCard.kt` | Animated account card + timeline sidebar |
| `ui/components/DockingGlitch.kt` | Glitch/materialization animation system |
| `navigation/NavRoutes.kt` | Route constants + `gamePath()` helper |
| `ui/theme/ArteriaTheme.kt` | Material 3 theme, `ArteriaPalette`, Cinzel typography |
| `core/model/GameModels.kt` | Domain data classes (SkillState, GameState, TickResult…) |
| `core/skill/SkillId.kt` | All 16 skills + 4 pillars enum |
| `core/engine/TickEngine.kt` | Offline tick simulation |
| `core/data/SkillDataRegistry.kt` | Skill actions/items; `isSkillImplemented`, `actionsForSkill` |
| `core/data/HerbloreData.kt` | Potions from `HarvestingData` inputs (`inputItems`) |
| `core/data/ScavengingData.kt` | Salvage gathering tiers |
| `ui/game/SkillComingSoonDialog.kt` | Modal when a skill has no registry actions yet |
| `data/game/GameRepository.kt` | Safe game state load/save (transactional) |
| `data/profile/ProfileRepository.kt` | Profile persistence interface |
| `DOCS/SCRATCHPAD.md` | **Read this first** — live session state |
| `DOCS/SBOM.md` | Canonical dependency versions |
| `.claude/cursor.rules` | Agent behavior rules (source of this doc's philosophy) |

---

## Further Reading

- **Game design truth:** `DOCS/ARTERIA-V1-DOCS/DOCU/MASTER_DESIGN_DOC.md` (read-only)
- **TS → Kotlin port guide:** `DOCS/MIGRATION_SPEC.md`
- **UI/UX audit notes:** `DOCS/ARTERIA-V1-DOCS/DOCU/debugs/ui-audit-2026-03-08.md`
- **Roadmap:** `DOCS/ROADMAP.md` (Phases 0–10)
