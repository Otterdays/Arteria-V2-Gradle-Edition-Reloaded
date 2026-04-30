<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

# Agent Research Brief — Arteria V2 Clicker / Idle Game

> Purpose: give a fellow AI agent a detailed starting point for research, design critique,
> and improvement planning.
> Scope: native Android V2 (`Kotlin + Compose + Room + :core`), with special attention to
> the clicker-style **Resonance** subsystem.
> Generated: 2026-04-29.

## Research Grounding

This brief is grounded in:

- `DOCS/SUMMARY.md`
- `DOCS/ARCHITECTURE.md`
- `DOCS/SCRATCHPAD.md`
- `DOCS/ROADMAP.md`
- `DOCS/SBOM.md`
- `DOCS/SKILLS_EXPANSION_NATIVE.md`
- `DOCS/DESIGN_TRANSLATION_AUDIT.md`
- `DOCS/ARTERIA-V1-DOCS/DOCU/CLICKER_DESIGN.md`

Treat exact trainable-skill counts as **verify in code** via `SkillDataRegistry` and the
current `*Data.kt` files. Some planning docs are intentionally preserved with historical
entries, so newer `SCRATCHPAD.md` rows can supersede older roadmap/glossary counts.

---

## 1. Product Summary

**Arteria Gradle Edition V2** is a native Android idle / incremental RPG built with
Kotlin, Jetpack Compose, Navigation Compose, Room, Gradle, and a pure JVM `:core` module.
The current game is not a separate clicker product. It is one persistent character/save
where idle skilling, combat, bank economy, gear, companions, achievements, random events,
prestige UI, and a foreground clicker-like skill all feed each other.

The broader monorepo React Native / Expo app is reference material only. Current
implementation truth for this checkout is native Android.

**Design truth precedence:**

1. Root monorepo `../DOCU/` when available.
2. Bundled mirror `DOCS/ARTERIA-V1-DOCS/DOCU/`.
3. Current native docs and source code in this repo.

---

## 2. High-Level Game Shape

Arteria V2 is an idle RPG with a large skill roster and resource economy.

- Players create/select a profile.
- The app loads per-profile game state from Room.
- A tick loop advances the active training action.
- Completed actions award XP and resources, or consume inputs and craft outputs.
- Offline progress is simulated on return and shown in a report.
- Bank inventory connects gathering, crafting, gear, summoning, and combat.
- Meta-systems such as equipment, companions, achievements, random events, and prestige
  create long-term retention hooks.

The core gameplay promise is: **do something simple, watch numbers rise, unlock deeper
loops, then connect resources across systems.**

---

## 3. Core Loop

### Idle / Skill Loop

1. Choose a skill.
2. Pick an action unlocked by level.
3. Start training.
4. Tick loop advances progress.
5. Completed action gives XP and either:
   - adds resources to bank, or
   - consumes bank inputs and creates crafted outputs.
6. Level-ups unlock better actions.
7. Resources feed other skills, equipment, summoning, combat, and future systems.

### Persistence / Offline Loop

1. Game state is saved through `GameRepository.saveGameState()`.
2. On cold start, `GameViewModel` loads the state.
3. Offline tick simulation applies elapsed time, respecting caps/preferences.
4. `OfflineReportDialog` reports XP, resource gains, and level-ups when meaningful.

### Active Clicker Loop: Resonance

Resonance is the clicker subsystem. It is a **Support-pillar skill**, not a separate mode.

1. Player opens the Resonance tab/screen.
2. Player taps the orb to pulse.
3. Pulse grants Resonance XP and builds **Momentum**.
4. Momentum decays over time.
5. While Momentum is positive, other skills/combat run faster.
6. Level unlocks improve tap efficiency, heavy pulse behavior, passive feedback, and
   long-term stability.

The clicker fantasy is: **the player is the Anchor, pulsing cosmic weight to make the
world move faster.**

---

## 4. Resonance Design Details

From `CLICKER_DESIGN.md`, Resonance should remain a **synergy layer**, not the replacement
core loop.

### Current / Intended Variables

- `momentum`: 0-100 active-play haste meter.
- `anchorEnergy`: resource for heavier pulse actions.
- Resonance XP: normal skill XP/leveling through tap actions and passive hooks.

### Momentum

Momentum is the core clicker output.

- Taps increase Momentum.
- Momentum decays in foreground time.
- Momentum should not accrue offline.
- Haste formula intent: full Momentum gives roughly **1.5x** speed.
- A level 99-style capstone can create a Momentum floor for permanent mild haste.

### Unlock Ladder

Design unlocks:

- Level 20: **Multi-Pulse** — multi-finger tapping.
- Level 40: **Resonant Echo** — more Momentum and XP per tap.
- Level 60: **Soul Cranking** — heavy pulse using Anchor Energy.
- Level 80: **Kinetic Feedback** — successful non-Resonance ticks can add Momentum.
- Level 99: **Perfect Stability** — Momentum floor.

### Why This Matters

Resonance gives active players something tactile to do while preserving idle identity.
The key design risk is fatigue: if Momentum becomes mandatory, the game stops feeling idle.
The key opportunity is delight: if pulsing feels premium but optional, the game gains a
strong differentiator.

---

## 5. Architecture Summary

```text
MainActivity
  -> ArteriaRoot / theme and preference locals
  -> ArteriaApp / NavHost
    -> account_select
    -> account_create
    -> game/{profileId}

AccountViewModel
  -> ProfileRepository
  -> RoomProfileRepository
  -> ProfileDatabase / arteria_profiles.db

GameViewModel
  -> GameRepository
  -> GameDatabase / arteria_game.db
  -> :core TickEngine / XPTable / skill data
```

### Boundaries

- Composables do not call DAOs directly.
- ViewModels own UI state and side effects.
- Room is behind repositories.
- `:core` owns engine/domain math and should stay Android-free.
- Routes are centralized in `NavRoutes`.

---

## 6. Data And Systems Inventory

| Area | Current Role |
|------|--------------|
| Profiles | Account selection, creation, active profile, display name, last played |
| Skills | XP, level, active action, progress, implemented/coming-soon routing |
| Bank | Resource inventory, search/sort/presets, crafting inputs/outputs |
| TickEngine | Action progress, XP, resources, input consumption, offline simulation |
| GameViewModel | Tick loop, start/stop training, saves, offline report, events |
| Equipment | Gear slots, modifiers, paper doll style UI, combat stat aggregation |
| Companions | Passive bonuses and future assignment potential |
| Combat | Encounter v1, Barn Rat starter loop, loot, logs, flee/death states |
| Resonance | Clicker Momentum, haste, pulse counters, active-play boosts |
| Achievements | Chronicle, categories, progress, unlock notifications |
| Random Events | Tick-triggered modal events |
| Prestige | Ascension UI exists; numeric perk hooks need confirmation/completion |
| Settings | DataStore prefs, theme, motion, haptics, sound, reset/delete profile |

---

## 7. Skill And Content Model

Skills live in `SkillId` and are implemented when `SkillDataRegistry` has action data.
If a skill lacks actions, tapping it should show `SkillComingSoonDialog`.

Typical action model:

- `id`
- `skillId`
- `name`
- `levelRequired`
- `xpPerAction`
- `actionTimeMs`
- optional `resourceId`
- optional `resourceAmount`
- optional `inputItems`

The strongest current design pattern is **resource chaining**:

- Mining -> ores/gems.
- Smithing -> bars.
- Forging/Jewelcrafting -> gear/accessories.
- Fishing -> raw fish.
- Cooking -> food.
- Harvesting -> herbs.
- Herblore -> potions.
- Combat -> charms/shards/drops.
- Summoning -> pouches.
- Resonance -> haste for all of the above.

---

## 8. Existing Strengths

- Native stack has a clean separation between UI, persistence, and core game math.
- Room persistence and migrations are already real, not placeholder-only.
- Offline catch-up exists, which is essential for idle retention.
- The Docking Station account flow gives the project a distinct identity.
- Resonance is a smart clicker integration because it boosts the idle RPG instead of
  fragmenting into a separate game.
- Recent work has expanded economy chains so gear can be crafted instead of only existing
  as static UI.
- Settings, accessibility-adjacent toggles, haptics, soundscape, and reduce motion are
  already considered.

---

## 9. Current Gaps / Risks

### Gameplay

- Combat v1 exists, but Attack/Strength/Defence loops and deeper dungeons are still key
  progression gaps.
- XP/hr and efficiency feedback are called out as next UX priorities.
- Prestige appears partially implemented: UI is present, but numeric perk effects need
  verification and likely completion.
- Crafting queue / multi-step active crafting is still a future opportunity.
- Resonance balance needs careful tuning so active clicking is beneficial but not required.

### UX

- A11y pass remains open: TalkBack labels, focus order, contrast checks.
- Loading/empty/error states should be audited route-by-route.
- Rare drop / level-up banners should become non-blocking and consistent.
- Icons are still a known polish target.

### Release / Quality

- R8/minify is enabled, but keep-rules, signing, AAB readiness, Play Console metadata, and
  data safety are still release-hardening work.
- Compile checks are common, but broader `GameViewModel` behavior tests and integration
  coverage should be expanded.
- Manual smoke tests remain important for full economy chains.

### Documentation

- Some docs preserve historical rows and can look stale at first glance.
- `DESIGN_TRANSLATION_AUDIT.md` is useful for V1 source location mapping, but its port
  status table should not be treated as current V2 truth without code verification.

---

## 10. Improvement Plan For Another Agent

### Priority 1 — Verify The Live Build Truth

Research tasks:

- Inspect `SkillDataRegistry` and all `*Data.kt` files.
- Count implemented trainable skills from code.
- Compare `SCRATCHPAD.md` claims against source.
- Verify current `GameDatabase` version and migrations.
- Verify current app version in `app/build.gradle.kts`.

Output:

- A one-page “live truth” matrix: skill -> implemented -> source file -> economy inputs/outputs.

### Priority 2 — Improve Resonance Feel

Research tasks:

- Review `ResonanceScreen.kt`.
- Confirm Momentum math in `TickEngine` / `GameViewModel`.
- Check if the UI clearly shows:
  - current Momentum,
  - speed bonus,
  - XP per tap,
  - Momentum per tap,
  - decay behavior,
  - heavy pulse cost/value.

Recommended improvements:

- Add clearer “current haste” copy.
- Add XP/hr or time-saved estimate while Momentum is active.
- Add reduce-motion-sensitive orb feedback.
- Add daily/session recap stats for active pulsing.
- Consider “soft cap” or diminishing returns if tapping becomes too dominant.

### Priority 3 — Close Combat Baseline

Research tasks:

- Inspect `CombatEngine`, encounter data, `CombatScreen`, and equipment stats.
- Identify how Attack/Strength/Defence/Hitpoints should receive XP.
- Confirm Barn Rat starter drops and summoning material flow.

Recommended improvements:

- Add first complete melee loop.
- Add readable enemy state and fight speed display.
- Show equipped weapon/armor summary in combat.
- Ensure combat has a clear stop/flee/recover loop.

### Priority 4 — Economy Closure

Research tasks:

- Trace mining -> smithing -> forging -> equipment.
- Trace mining gems -> jewelcrafting -> accessories -> equip.
- Trace combat drops -> summoning.
- Trace harvesting -> herblore -> potions if potions are usable or just banked.

Recommended improvements:

- Add missing use-cases for crafted outputs.
- Add “next useful craft” suggestions in Hub.
- Add item tooltips: source, used in, rarity, and next unlock.
- Add balance notes for resource bottlenecks.

### Priority 5 — Player Feedback

Recommended improvements:

- XP/hr on `SkillDetailScreen`.
- “Time to next level” estimate.
- Bank item source/usage hints.
- Non-blocking banners for level-ups and rare drops.
- Chronicle entries for meaningful milestones.
- Resonance session recap: taps, max Momentum, time saved.

### Priority 6 — Ship Readiness

Recommended improvements:

- Add a documented smoke checklist.
- Add focused `GameViewModel` tests for:
  - offline catch-up,
  - training start/stop,
  - insufficient input handling,
  - combat blocks skilling,
  - Resonance decay/haste.
- Review release keep rules.
- Prepare AAB/signing/data safety checklist.

---

## 11. Research Questions

Use these as prompts for deeper agent research:

1. Which skills are currently implemented in code, and which are only roster entries?
2. Does Resonance Momentum affect combat and skilling consistently?
3. Is Momentum fun without being mandatory?
4. Which crafted items have no meaningful sink yet?
5. Which systems generate resources faster than they consume them?
6. Are new players guided from first skill -> first craft -> first gear -> first combat?
7. Is the account/profile flow ready for normal users, or still dev-centric?
8. Are offline rewards legible and trustworthy?
9. Which docs are stale enough to mislead future agents?
10. What one vertical slice would most improve player retention this week?

---

## 12. Suggested Source Files To Inspect

- `core/src/main/kotlin/com/arteria/game/core/data/SkillDataRegistry.kt`
- `core/src/main/kotlin/com/arteria/game/core/engine/TickEngine.kt`
- `core/src/main/kotlin/com/arteria/game/core/model/GameModels.kt`
- `core/src/main/kotlin/com/arteria/game/core/skill/SkillId.kt`
- `app/src/main/java/com/arteria/game/ui/game/GameViewModel.kt`
- `app/src/main/java/com/arteria/game/ui/game/GameScreen.kt`
- `app/src/main/java/com/arteria/game/ui/game/SkillDetailScreen.kt`
- `app/src/main/java/com/arteria/game/ui/game/ResonanceScreen.kt`
- `app/src/main/java/com/arteria/game/ui/game/CombatScreen.kt`
- `app/src/main/java/com/arteria/game/data/game/GameRepository.kt`
- `app/src/main/java/com/arteria/game/data/game/GameDatabase.kt`

---

## 13. Suggested Verification Commands

Run only if the session/user allows verification:

```bash
./gradlew :app:compileDebugKotlin
./gradlew :core:test
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

---

## 14. One-Line Pitch

**Arteria V2 is a native Android idle RPG where a large skill economy, Room-backed
offline progression, gear/companions/combat, and the active Resonance clicker all share
one save and one progression loop; the highest-leverage improvements are combat depth,
economy closure, Resonance feedback/balance, XP efficiency UI, accessibility, and release
hardening.**

---

## 1. Product summary

**Arteria Gradle Edition V2** is a **native Android** idle / incremental game: **Kotlin**, **Jetpack Compose**, **Navigation Compose**, **Room** (two SQLite DBs), **Gradle ~9.6 / AGP ~9.x**, **JDK 21**. Game rules and skill tables live in a **JVM `:core` module** with **no Android imports**; the app is UI + persistence + ViewModels.

It is **not** the Expo/React Native app in the wider monorepo; RN is **reference only** for parity (`MIGRATION_SPEC.md`, `DESIGN_TRANSLATION_AUDIT.md`).

**Design truth** (lore, economy intent, long roadmap): monorepo `../DOCU/` first; bundled `DOCS/ARTERIA-V1-DOCS/DOCU/` second.

---

## 2. Core gameplay loop (idle “clicker” + skills)

- **Accounts**: Room `arteria_profiles.db` — profiles, active selection, display name, last played.
- **Game state**: Room `arteria_game.db` — per-profile skills, bank, `game_meta` (offline audit, equipment, companions, combat/resonance-related fields per migrations described in docs).
- **Progression**: `SkillAction`-style nodes — level gate, XP per action, duration (`actionTimeMs`), optional **gathered** `resourceId` / amount, optional **consumed** `inputItems` from bank (`TickEngine`).
- **Training**: **One active skill action at a time** (enforced in `GameViewModel`); tick loop advances `actionProgressMs`, completes actions, updates XP/bank, can auto-stop on missing inputs.
- **Offline**: On load, **offline catch-up** runs (capped duration; user prefs / debug bypass documented in architecture/settings work); **`OfflineReportDialog`** summarizes gains.
- **Hub**: Command-center tab — training summary, nudges, offline card, stats (per architecture / scratchpad).

**“Clicker” slice**: **Resonance** is explicitly a **foreground interaction / momentum** layer (pulse, haste, kinetic feedback) integrated with ticks — not purely passive like some gathering lines.

---

## 3. Pillars and content model

- **`SkillId`**: Large roster (**48 skills / 5 pillars** in docs — Gathering, Crafting, Combat, Support, Cosmic).
- **Implemented vs placeholder**: `SkillDataRegistry.isSkillImplemented` / `actionsForSkill` — empty → **`SkillComingSoonDialog`** on tap.
- **Expansion pattern** (`SKILLS_EXPANSION_NATIVE.md`): add `*Data.kt` in `:core`, register in `SkillDataRegistry`, ensure `GameRepository` initializes state on load; DB migrations when schema changes.

---

## 4. Major systems (inventory for research)

| Area | Role (from docs) |
|------|------------------|
| **TickEngine** (`:core`) | Time-based action completion, XP, bank mutations, offline simulation, gear/companion modifiers where wired |
| **GameViewModel** | Tick loop, training start/stop, saves, offline report, level-up `SharedFlow`, preference-driven behavior |
| **GameRepository** | **Transactional** `saveGameState()` as primary safe write path |
| **Bank** | Search, sort modes, withdraw presets (Phase 7 marked done) |
| **Equipment** | Slots expanded in recent work (docs mention **7 slots**, paper doll, combat stat aggregation) |
| **Combat** | Encounter model (e.g. Barn Rat / Sunny Meadow Barn), `CombatEngine`, combat log, skilling blocked during combat; further melee skill loops **roadmapped** |
| **Achievements / Chronicle** | Large achievement set, categories, UI filter/progress |
| **Random events** | Weighted tick-triggered dialogs |
| **Prestige / Ascension** | UI + flow; **perk multipliers in engine** noted as partial |
| **Summoning** | Pouch actions, combat drops for charms/shards; smoke called out in scratchpad |
| **Settings** | DataStore prefs (theme, motion, haptics, sound, offline report, soundscapes, danger zone reset/delete), OSS/credits, rename profile |
| **Shell UX** | Docking station account flow (`DockingGlitch`, cards, backdrop), game tabs: **Hub, Skills, Bank, Combat** (+ Resonance route/tab per recent entries), settings overlay |

---

## 5. Technical architecture (short)

```
MainActivity → ArteriaTheme / prefs locals → ArteriaApp (NavHost)
  account_select | account_create | game/{profileId}
AccountViewModel → ProfileRepository → Room (profiles)
GameViewModel → GameRepository → Room (skills, bank, meta) + :core TickEngine/XPTable
```

**Boundaries**: Composables → ViewModels → repositories; no composable → DAO. Routes via `NavRoutes.gamePath(profileId)`.

---

## 6. Doc map for a researcher (read order)

1. **`DOCS/SCRATCHPAD.md`** — live state, **next actions**, recent feature landings  
2. **`DOCS/ROADMAP.md`** — Phases 0–10 spine + parity backlog  
3. **`DOCS/ARCHITECTURE.md`** — runtime diagram, verification snapshot, file responsibilities  
4. **`DOCS/SKILLS_EXPANSION_NATIVE.md`** — how to add skills; glossary + future splits  
5. **`DOCS/MIGRATION_SPEC.md`** — TS/RN → Kotlin mental model  
6. **`DOCS/SBOM.md`** — versions, Android targets  
7. **`DOCS/DESIGN_TRANSLATION_AUDIT.md`** — V1 file index; **port status table is stale** vs current app — still useful for **where V1 content lived** (engine vs constants vs UI)  
8. **External design** — `../DOCU/MASTER_DESIGN_DOC.md`, `IMPROVEMENTS.md`, `UI_REVISION_CRAFTING_v2.md`, etc. (per `SUMMARY.md`)

---

## 7. Documented gaps and risks

- **Phase 5 “near-complete”** but **balancing / production-chain smoke** still called out (mine → cut → forge → equip).  
- **Combat**: encounter v1 exists; **Attack/Strength idle combat training** and **dungeon/instance** shells still open in roadmap / scratchpad.  
- **Prestige**: UI shipped; **engine-side perk multipliers** partial.  
- **Phase 6**: R8 on; **keep-rules review, AAB/signing, Play metadata** still backlog.  
- **Phase 7–10**: A11y pass, loading/error states, deep links, crafting queue UI when engine supports queues, **XP/hr** on skill detail, **vector icons** vs placeholders, cold-start benchmarks, more tests.  
- **SUMMARY mission checklist**: manual device smoke; richer **`GameViewModel`** tests still listed among historical checklist items — confirm against latest scratchpad.  
- **`DESIGN_TRANSLATION_AUDIT.md`**: good for **V1 source paths**; do not treat the ⬜ table as current V2 truth.

---

## 8. How it can be improved (themes, prioritized from docs)

**A. Gameplay depth**

- Close the **combat vertical**: melee stats loop, clearer enemy UI, fight speed, equipment strip (ROADMAP Phase 7/9).  
- **Dungeons / instances** (timers, rewards) as structured retention.  
- **Prestige**: finish **numeric hooks in `TickEngine`** so ascension is felt, not only UI.  
- **Economy surfaces**: shop / patron placeholders when design is ready.

**B. Idle / clicker feel**

- **`SkillDetailScreen` XP/hr** (explicit next action in scratchpad).  
- **Crafting queue** + cancel/skip when engine supports it.  
- **Resonance**: tune so it complements idle (avoid fatigue vs reward).

**C. Content and parity**

- Use **`SKILLS_EXPANSION_NATIVE.md`** splits (e.g. farming/thieving depth, runecrafting/essence) to avoid shallow “one row per skill.”  
- Align **`DESIGN_TRANSLATION_AUDIT`** with reality: refresh port status or add a “V2 implemented” column sourced from registry.

**D. UX / accessibility**

- TalkBack, focus order, contrast (Phase 7).  
- **Navigation**: loading/empty/error on major routes; deep links to Bank/Settings.  
- **Toasts/banners** for level-ups and rare drops (Phase 8).

**E. Visual / IP**

- **Theming token single source** (Compose vs `theme.ts` / DOCU).  
- **Radial mastery / crafting flagship** per `UI_REVISION_CRAFTING_v2.md`.  
- Replace generic icons (**Holo-Icons** in scratchpad).

**F. Quality and ship**

- **`GameViewModel` / integration tests** beyond compile-only.  
- **Startup benchmark** vs RN baseline (Phase 10).  
- **Play Console** checklist: data safety, signing, version policy.

---

## 9. Suggested verification commands (for the other agent)

- `./gradlew :app:assembleDebug`  
- `./gradlew :core:test`  
- `./gradlew :app:testDebugUnitTest` (if policy allows tests)

---

## 10. One-line pitch for another model

**Arteria V2** is a **native Android idle skill game** with a **large multi-pillar skill tree**, **Room persistence**, a **JVM tick engine**, **offline catch-up**, and growing **meta-systems** (gear, companions, achievements, events, combat encounters, resonance); **docs + roadmap** prioritize **combat depth, economy closure, XP feedback, accessibility, and store-ready release engineering**.