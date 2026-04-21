<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | **v1.5.0 doc + UX sync:** `SKILLS_EXPANSION_NATIVE.md` glossary §0 (4 skills), §5→§5b, §7.1/7.4 callouts; `ROADMAP` v1.5.0 row; `SkillsScreen` crossovers for Farming/Thieving/Woodworking/Tailoring + chip labels. |
| 2026-04-01 | Claude Haiku 4.5 | Anthropic Claude | **v1.5.0 — 4 new skills + Equipment/Companions integration:** `FarmingData`, `ThievingData`, `WoodworkingData`, `TailoringData` added to `:core`; all 4 registered in `SkillDataRegistry`; `GameState` + `TickEngine` wired with gear/companion XP multipliers; `GameDatabase` migration v2→v3 (5 new `game_meta` columns); `GameRepository` load/save maps equippedGear + activeCompanionId; `GameViewModel` gains `equip`, `unequip`, `equipCompanion`, `dismissCompanion` + `companions` StateFlow; `GameScreen` routes ⚔️/🐾 TopAppBar buttons to `EquipmentScreen`/`CompanionsScreen` overlays with full BackHandler; `versionCode` 7→8, `versionName` 1.4.5→1.5.0; README badge + `APP_CHANGELOG` top card updated. `:app:compileDebugKotlin` green. |
| 2026-04-01 | Cursor Agent | Composer | **v1.4.5 release hygiene:** `versionName` **1.4.5** / `versionCode` **7**; prepended `APP_CHANGELOG` (tablet Skills, PanelBackHeader, crossover chips, Chronicle note, doc parity); README shield; `ROADMAP` + `SKILLS_EXPANSION_NATIVE.md` amended with canonical app version line. |
| 2026-04-01 | Claude Haiku 4.5 | Anthropic Claude | **v1.4.3 Release:** ChangelogScreen polish (Tier 1+2+3): staggered entry animations, left accent stripe, glass highlight, improved spacing, tag emoji badges, interactive border animation, upgraded typography, accessibility. Added v1.4.2 QoL entry (Achievements, Bank search/sort, Haptics, Random Events, Equipment/Gear, Companions/Familiars, Prestige/Ascension from prior agents). Bumped `versionCode` 4→6, `versionName` 1.4.1→1.4.3. Updated README badge + `APP_CHANGELOG`. `:app:assembleDebug` green. |
| 2026-03-31 | Cursor Agent | Composer | **`SkillsScreen` crossover chips:** `FlowRow` + `SkillId.crossoverChipLabel()` (compact tags) + single-line ellipsis — fixes Logging card layout (three long targets in adaptive grid broke `Row` / text wrap). `:app:compileDebugKotlin` green. |
| 2026-04-01 | Cursor Agent | Composer | **`SKILLS_EXPANSION_NATIVE.md`:** V1 `SKILLS_ARCHITECTURE` / `skilling_guides` / `SYNERGIES` → native V2 playbook; `SUMMARY` read-order + index; `ARCHITECTURE` snapshot fixes (`:core` path, Hub 4-tab); SCRATCHPAD next-action pointer. |
| 2026-04-01 | opencode | qwen3.6-plus-free | **v1.5.0 QoL features (7/7):** Haptics on train start/stop + achievement unlocks; Bank search + 4-mode sort + withdraw presets; Achievements/Chronicle (18 achievements, 6 categories, tracking in GameViewModel, 🏆 icon in TopAppBar); Random Events (6 weighted events, dialog UI, tick-triggered); Equipment/Gear system (4 slots, 19 items, EquipmentScreen with equip/unequip); Companions/Familiars (11 companions, 5 rarities, passive bonuses, CompanionsScreen); Prestige/Ascension (6 perks, point system, PrestigeScreen with ascend flow). All core models + basic UI shipped. `:app:compileDebugKotlin` green. |
| 2026-04-21 | Antigravity | Claude Sonnet 4.6 (Thinking) | **Phase 5 Expansion & UI "Mad" Polish:** Implemented Forging, Jewelcrafting, Firemaking; Gem Rocks; CyberHUD (HoloGrid/Scanlines); Energy Pulse animations; ArteriaPalette high-polish tokens. Build stable. |
| 2026-04-21 | Antigravity | Claude Sonnet 4.6 (Thinking) | **Gameplay Vertical Expansion:** Implemented **Forging**, **Jewelcrafting**, and **Firemaking** skill data; updated **Mining** with Gem Rocks and raw gems. Bridged the economy loop from bars/gems to `EquipmentRegistry` items, making gear actually craftable. Updated `SkillDataRegistry` to wire all new loops. |
| 2026-04-01 | Antigravity | Claude Sonnet 4.6 (Thinking) | **Chronicle / Achievements overhaul:** `AchievementRarity` enum + `AnySkillLevel` condition in `Achievements.kt`; `AchievementRegistry` expanded 18 → 40 achievements (tiered depth per skill, item-collection milestones, correct `AnySkillLevel` for "Mastered"/"Halfway There"); `GameViewModel.checkAchievements` handles `AnySkillLevel` via `maxOfOrNull`; `ChronicleScreen` rebuilt — TopAppBar with back arrow, overall completion progress bar, animated category filter chips, per-card animated progress indicators, rarity badges (`COMMON`→`LEGENDARY`), unlock dates, locked/unlocked visual alpha hierarchy; `GameScreen` passes `onBack` to `ChronicleScreen`. `:app:compileDebugKotlin` green. |
| 2026-04-01 | Antigravity | Claude Sonnet 4.6 (Thinking) | **Skill roster expanded to 48:** Conceived and added 7 new experimental skills (`TRAPPING`, `SIPHONING`, `JEWELCRAFTING`, `TINKERING`, `ENCHANTING`, `DIVINATION`, `BARDING`) to `SkillId.kt`. Expanded `SKILLS_EXPANSION_NATIVE.md` glossary to include all un-implemented skills mapped to their intended "App Slices" / economic loops. |
| 2026-04-01 | Cursor Agent | Composer | **Phase 2 complete:** `:core` JVM Kotlin (`kotlin("jvm")` 2.3.20, toolchain 21); engine at `core/src/main/kotlin/com/arteria/game/core/`; `TickEngineTest` + `XPTableTest`; `app` → `implementation(project(":core"))`; root `org.jetbrains.kotlin.jvm` apply false; ROADMAP/SBOM/README/CLAUDE/SUMMARY updated; `:core:test` + `:app:testDebugUnitTest` green. |
| 2026-03-31 | Cursor Agent | Composer | **v1.4.1 docs + version modal sync:** `APP_CHANGELOG` top card (DOCS tag, release hygiene copy); `versionName` **1.4.1** / `versionCode` **4**; README release shield; `CLAUDE.md` Doc Update Rules + Output Checklist + Key Files (`ChangelogScreen`). Policy: SCRATCHPAD handoffs for shipped work should pair with What's New + version bump. |
| 2026-04-01 | Cursor Agent | Composer | **Settings plan (10+ options):** DataStore `UserPreferencesRepository`; `MainActivity` `ArteriaRoot` + `CompositionLocal`s; light/dark `ArteriaTheme`, `arteriaSpaceBackgroundBrush`, `DockingBackground`/`DockingGlitch` reduce-motion; `SettingsScreen` toggles + OSS/Credits + danger zone + DEBUG cap bypass; `GameRepository` reset/delete; `ProfileDao.deleteById`; `TickEngine.DEFAULT_MAX_OFFLINE_MS`; `UserPreferencesProvider` + `GameViewModel` wiring; `:app:testDebugUnitTest` green; `SBOM`/`ARCHITECTURE`/`master_settings` updated. |
| 2026-03-31 | Cursor Agent | Composer | **v1.4.0 release notes:** `APP_CHANGELOG` top entry (Hub, 4-tab bar, Herblore/Scavenging, settings parity, expanded skill roster note); `versionName` **1.4.0** / `versionCode` **3** in `app/build.gradle.kts`; README release badge **1.4.0**. |
| 2026-03-31 | Cursor Agent | Composer | **Skill roster expansion (V1 canon):** `SkillPillar.COSMIC`; 25 new `SkillId` entries (Farming, Thieving, Firemaking, Woodworking, Tailoring, Fletching, Construction, Alchemy, Ranged, Constitution, Sorcery, Slayer, Exploration, Astrology, Summoning, Cleansing, Barter, Research, Leadership, Resonance, Chaos Theory, Aether Weaving, Void Walking, Celestial Binding, Chronomancy); regrouped enum by pillar; `ArteriaPalette.CombatAccent` + `SkillsScreen` pillar colors; `ChangelogScreen` **v1.4.0**, `versionCode` 3; `CLAUDE.md` skill inventory. |
| 2026-03-31 | Claude Sonnet 4.6 | Anthropic Claude | **Final 5 checklist items (3c/3e/3f/3g/5f):** `SkillStarMap.kt` — pentagon pillar layout, fan-spread skill nodes, XP arc rings, crossover dashed edges, level labels, tap-to-navigate. `SkillsScreen.kt` — `skillCrossover` map + "→ Target" chips (3c); `PillarSectionHeader` live/avg/active summary (3e); `FilterSortBar` pillar chips + sort cycle + Map/List toggle (3f/3g). `HubScreen.kt` — `dismissedNudges` state, stable nudge keys, "×" dismiss buttons, 2-nudge cap (5f). Build green, checklist 30/30. |
| 2026-03-31 | Claude Opus 4.6 | Anthropic Claude | **Hub Screen (Command Center):** `HubScreen.kt` (items 1a–1h from checklist) — inline offline gains card replacing blocking dialog, active training panel with level ring + pulsing dot + XP-to-level estimate, quick stats row (total level / bank items / top skill), next milestone nudge with progress bar + unlock detection, smart suggestion engine (craft opportunity > idle skill), recent level-up mini-cards, empty-hub prompt. Updated `ArteriaBottomBar.kt` with Anchor icon (4 tabs: Hub/Skills/Bank/Combat), `quadraticBezierTo` → `quadraticTo` deprecation fix. Rewired `GameScreen.kt` tab indices, `recentLevelUps` tracking via `mutableStateListOf`. Fixed `OfflineReportDialog.kt` `MiningData` → `SkillDataRegistry`. |
| 2026-03-31 | Claude Opus 4.6 | Anthropic Claude | **Playability expansion + UI overhaul:** Added Logging, Fishing, Smithing, Cooking skill data with cross-economy `inputItems`; TickEngine bank consumption + auto-stop on material shortage; one-skill-at-a-time enforcement in `GameViewModel.startTraining`; custom `ArteriaBottomBar` (Canvas icons, XP ring, bank badge, glow effects); pillar-grouped `SkillsScreen` redesign with animated borders/arcs; `SkillDetailScreen` hero header with live training row + `LevelCircle`; `ActionCard` sweep fill progress. |
| 2026-03-31 | Cursor Agent | Composer | **README polish:** multi-row shields (KSP, Navigation, Lifecycle, Coroutines, compileSdk 36.1, JVM 21 vs JDK 26 build story), quick start + tree for `build-with-jdk26.bat`, prerequisites/toolchain/troubleshooting aligned with `DOCS/SBOM.md`. |
| 2026-04-01 | Cursor Agent | Composer | **Settings V1 parity slice:** `updateDisplayName` on profiles; `AccountSessionInfo`; `SettingsScreen` rename dialog, last played, `BuildConfig` version, tick/save cadence, test sound; `GameScreen`/`ArteriaApp` wiring; `FakeProfileRepository` updated; `ARCHITECTURE`/`SBOM`/`master_settings_suggestions_doc.md`; `:app:compileDebugKotlin` green. |
| 2026-04-01 | Cursor Agent | Composer | **Herblore + Scavenging:** `HerbloreData.kt` (8 potions, 1× `HarvestingData` herb each via `inputItems`), `ScavengingData.kt` (8 salvage tiers); `SkillDataRegistry`; docs `CLAUDE` / `ARCHITECTURE` / `README`. |
| 2026-03-31 | Cursor Agent | Composer | **Transfer script + JDK:** `gradlew.bat` strips `.cursor`/`redhat.java` `JAVA_HOME`, scans Eclipse Adoptium + Microsoft `jdk-*`, **removes PATH java fallback** (was still Cursor JRE). `build-apk-for-transfer.ps1` resolves same JDK + `gradlew --stop` before build. `CLAUDE.md` gotcha updated. |
| 2026-03-31 | Cursor Agent | Composer | **Transfer script + JDK:** `gradlew.bat` strips `.cursor`/`redhat.java` `JAVA_HOME`, scans Eclipse Adoptium + Microsoft `jdk-*`, removes PATH `java` fallback; `build-apk-for-transfer.ps1` resolves JDK + `gradlew --stop`. `CLAUDE.md` gotcha updated. |
| 2026-03-31 | Cursor Agent | Composer | **Windows / Cursor JDK:** `gradlew.bat` preamble drops `JAVA_HOME` when `bin\jlink.exe` missing (fixes AGP `JdkImageTransform` using Red Hat JRE); falls back to `%ProgramFiles%\Java\jdk-21.0.10` then `jdk-26` / `jdk-21`. `build-with-jdk26.bat` runs `--stop` then `--no-daemon`. `CLAUDE.md` Windows gotcha line. |
| 2026-03-31 | Cursor Agent | Composer | **In-app What's New v1.3.0:** `ChangelogScreen.kt` new top `APP_CHANGELOG` entry (six skills, Coming Soon dialog, DB v2/offline audit, transactional saves, background tick sim, custom bottom bar); `app/build.gradle.kts` `versionName` **1.3.0**, `versionCode` **2**. |
| 2026-04-01 | Cursor Agent | Composer | **Harvesting** skill content: `HarvestingData.kt` (8 gather nodes, items + XP aligned with `LoggingData`); wired in `SkillDataRegistry`; `CLAUDE.md` skill-add note; `:app:compileDebugKotlin` green. |
| 2026-03-31 | Cursor Agent | Composer | Docs: `ARCHITECTURE.md` (snapshot + Screen UI row), `CLAUDE.md` (skill flow, Key Files, Game State), `README.md` (feature + diagram line) for Coming Soon skill modal. |
| 2026-03-31 | Cursor Agent | Composer | Coming Soon modal for unimplemented skills: `SkillDataRegistry.isSkillImplemented`, `SkillComingSoonDialog.kt` (matches `OfflineReportDialog` pattern), `GameScreen` tap branch + `BackHandler`; `:app:compileDebugKotlin` green. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | `DOCS/ARTERIA-V1-DOCS/DOCU/STYLE_GUIDE.md`: preservation header, Part A (V2 Kotlin/Compose, A.1–A.10), Contents table, Part B label for legacy §1–§10; fixed `SUMMARY.md` relative link; aligned with `CLAUDE.md`. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Dependency update sweep completed: no-bump pass (stable pins retained); updated `DOCS/SBOM.md` and `README.md` with findings and rationale. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Began save-cadence hardening: added deterministic `GameViewModel` cadence tests (before/after interval), introduced test-only time/loop controls, and verified green `:app:testDebugUnitTest`. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Phase 4 **DONE**: `GameDatabase` v2 migration, `lastOfflineTickAppliedAt` persistence, `GameDatabaseMigrationTest` + JVM round-trip test; ROADMAP/README/SBOM/ARCHITECTURE updated. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | README Phase 4 status row + next-goal aligned with `DOCS/ROADMAP.md` Phase 4 amendment and scratchpad next actions. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Docs modernization pass: added doc canon alignment, README link/staleness fixes, SBOM modernization policy, architecture verification snapshot, scratchpad consolidation block, and rule cross-reference notes in `CLAUDE.md` + `.claude/cursor.rules`. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Enhanced `build-with-jdk26.bat` to create a timestamped named APK copy in `dist/` using `ArteriaV2-yyyyMMdd-HHmmss.apk` and verified output path printout. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Updated `build-with-jdk26.bat` to default to `:app:assembleDebug` when no args are passed and print the APK output directory + resolved APK path after successful build. |
| 2026-03-31 | Claude Haiku 4.5 | Anthropic Claude | Animation system polish pass: verified build (`:app:compileDebugKotlin` + `:app:assembleDebug` green), added terminal timeline node pulse animation to `AccountSelectionScreen.kt`, reviewed and documented complete animation architecture spanning `DockingGlitch.kt` (7-layer glitch engine + ambient overlays), `DockingAccountCard.kt` (entry orchestration, hex ghost text, ripple rings, energy flow, spring badge pop-in), and updated `SCRATCHPAD.md` + `ARCHITECTURE.md` per preservation rules. |
| 2026-03-31 | Claude Sonnet 4.6 | Anthropic Claude | Absorbed `.claude/cursor.rules` and rewrote `CLAUDE.md` to incorporate agent philosophy (KISS/YAGNI/DRY/Fail Fast), workflow init order, Kotlin async/error standards, git conventions, output checklist, accurate skill/component inventory, and DockingGlitch animation system documentation. Updated SCRATCHPAD + ARCHITECTURE. |
| 2026-03-31 | Claude Opus 4.6 | Anthropic Claude | Docking Station animation system: built `DockingGlitch.kt` (7-layer `GlitchMaterializeOverlay`, `AmbientScanOverlay`, `EntryAnimations`, `AmbientAnimations`, `GlitchLayout` precomputed per seed); upgraded `DockingAccountCard.kt` with timeline sidebar, ripple ring, energy flow particle, skill pillar badges with spring pop-in; updated `AccountSelectionScreen.kt` with timeline terminal node + "Enter timeline" CTA. |
| 2026-03-31 | Claude Haiku 4.5 | Anthropic Claude | Main menu UX refactor (Section 4 of menu redesign): removed Settings tab from bottom nav, added `TopAppBar` with account name + gear icon, wired settings as full-screen overlay with back navigation, updated `GameScreen.kt` and `SettingsScreen.kt` with BackHandler for Android back-press handling. |
| 2026-03-31 | Claude Haiku 4.5 | Anthropic Claude | In-app version history feature: created `ChangelogScreen.kt` with 4-version changelog (`APP_CHANGELOG` data + overlay UI, space gradient background, version cards with bullet lists); wired "What's New" button to Settings screen and small TextButton to account selection screen; both use BackHandler overlay pattern. Updated SCRATCHPAD/SBOM per cursor.rules. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | ROADMAP progress pass: checked off delivered Phase 1/3 items, marked Phase 4 partial + Phase 5 in-progress, and added a single immediate manual-smoke next point. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | SBOM format update: added Installed vs Next available columns for toolchain and dependencies. |
| 2026-03-31 | Cursor Agent | GPT-5.3 Codex (Cursor) | Full dependency modernization pass: Kotlin plugin `2.3.20`, KSP `2.3.6`, Compose BOM `2026.03.01`, Room `2.8.4`; no tests run by request. |
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

## [AMENDED 2026-03-31] Consolidated Current State (active section)

Use this section as the live handoff source. Older repeated status/next-action blocks below are historical context.

### Active Status

- Native Android V2 stack is active (`Kotlin + Compose + Room + Gradle/AGP`).
- Core gameplay routing is live: `account_select` -> `account_create` -> `game/{profileId}`.
- Game shell is live with 4 tabs (`Hub`, `Skills`, `Bank`, `Combat`) and settings/changelog overlays. **`[AMENDED 2026-03-31]:`** Hub is default landing; older “3 tabs” mentions superseded.
- **`[AMENDED 2026-03-31]:`** Tapping a skill with no actions in `SkillDataRegistry` shows `SkillComingSoonDialog` instead of `SkillDetailScreen` (implemented skills unchanged).
- **`[AMENDED 2026-04-01]:`** **Harvesting** is playable: `HarvestingData` registered in `SkillDataRegistry` (gathering pillar).
- **`[AMENDED 2026-04-01]:`** **Settings** includes V1-style **display name edit** (persisted on `profiles`), **last played** line, **About** version from `BuildConfig`, **tick/save cadence** copy, and **Test sound**; see `master_settings_suggestions_doc.md` checklist updates.
- **`[AMENDED 2026-04-01]:`** **Settings expansion (DataStore):** theme **Dark / Follow system**, reduce motion, haptics, sound master, offline report toggle, offline cap copy, idle soundscapes pref (no audio yet), notifications/economy stubs, open-source list + credits, **reset progress** + **delete profile**, DEBUG offline-cap bypass; `GameViewModel.reloadAfterReset()`.
- **`[AMENDED 2026-04-21]:`** **Phase 5 Economy & UI Polish:** Implemented Forging, Jewelcrafting, Firemaking. Added Gem Rocks to Mining. Created **CyberHUD** system (Holo grids, scanlines) and **Energy Pulse** animations for training.
- **`[AMENDED 2026-03-31]:`** **`SkillId` roster = 48 skills, 5 pillars** (`COSMIC` added).
- **`[AMENDED 2026-03-31]:`** **Release hygiene:** When documenting shipped work here (`SCRATCHPAD`), also prepend `ChangelogScreen.kt` `APP_CHANGELOG`, bump `app/build.gradle.kts` `versionCode`/`versionName`, and README badge so **What's New** and **About** stay accurate (`CLAUDE.md` lists the checklist).
- **`[AMENDED 2026-04-01]:`** **Phase 2 (engine port) DONE** — idle math + skill data live in Gradle **`:core`** (JVM); run **`./gradlew :core:test`** for engine unit tests; `:app` has no duplicate `com.arteria.game.core` sources.
- Toolchain snapshot remains Gradle nightly `9.6.0-20260331012943+0000`, AGP `9.1.0`, JDK `21`.
- **`[AMENDED 2026-03-31]:`** Maintainer builds often run the **Gradle JVM on JDK 26** (`build-with-jdk26.bat` or `org.gradle.java.home`); Foojay daemon pin in-repo stays **JDK 21**; `:app`/`:core` bytecode target stays **Java 21** (see `README.md` + `DOCS/SBOM.md`).
- `[AMENDED 2026-03-31]:` Any older mention of `play/{profileId}` is superseded by `game/{profileId}`.
- **`[AMENDED 2026-03-31]:` Phase 4 (persistence + offline) is DONE** per `DOCS/ROADMAP.md` — `GameDatabase` v2, migration test + cold-start round-trip instrumented test, JVM meta round-trip. Optional manual device smoke remains a QA habit, not a phase gate.

### Active Blockers

- No current blockers for docs maintenance.
- Build environment remains sensitive to incorrect local JDK/JRE setup on contributor machines.

### Active Next Actions (single list)

1. **Combat Baseline**: Wire Attack/Strength loop (Melee training).
2. **Dungeon Core**: Simple enemy list + combat ticker logic.
3. **XP/hr Display**: Live efficiency tracking in SkillDetailScreen.
4. **Holo-Icons**: Replace generic icons with themed SVG paths (VectorJuice).
5. **Verify equipment loop**: mine gems → cut gems (Jewelcrafting) → use bars/gems to craft accessories/gear (Forging/Jewelcrafting) → equip in `EquipmentScreen`.

### Supersession Note

`[AMENDED 2026-03-31]:` For concise handoff, treat this consolidated block as authoritative when older sections conflict.

---

## Current Status

- **Phase:** Phase 0 (Structure + Gradle 9.6 wrapper + Nightly URL) is **DONE [2026-03-22]**. Phase 1 **partial** — see [ROADMAP.md](ROADMAP.md) (`[IN PROGRESS 2026-03-22]` checklist). Phases **7–10** (features / QoL / UI / ship) are **backlog** for parallel AI work — same file.
- **Runnable app:** `:app` Compose **Docking Station** styling (palette from `apps/mobile/constants/theme.ts`), **Cinzel** display type (`res/font/cinzel.ttf`, OFL), **animated space backdrop** (`DockingBackground`: twinkling stars, drifting aurora blobs, breathing gradient) on menu-related screens. NavHost `ui/ArteriaApp.kt`. `package com.arteria.game`, `minSdk 26`, `compileSdk 36.1`, `targetSdk 36`. **Daemon JVM:** JDK **21** / **Adoptium** via `gradle/gradle-daemon-jvm.properties`. `:core` empty. `:app:assembleDebug` green [2026-03-22].
- **`[AMENDED 2026-03-30]:`** **Startup profiles:** Room-backed account list + create/select + session placeholder; `data/profile/*`, `AccountViewModel`, `play/{profileId}`. **Build:** Use a full **JDK 21** (`javac` available); repo may set `org.gradle.java.home` in `gradle.properties`. **`build-apk-for-transfer`** copies APK to `dist/` as `Arteria-V2-Gradle-Edition-Reloaded-<variant>-<timestamp>.apk`.
- **Docs hub:** **`Arteria-Gradle-Edition-V2/DOCS/`** — start at **`SUMMARY.md`**, then **`ROADMAP.md`**, **`ARCHITECTURE.md`**, **`MIGRATION_SPEC.md`**, **`SBOM.md`**.
- **Main shipping app:** Still **`../apps/mobile/`** (Expo / RN, Gradle 8.x). Unaffected.

---

## Last Actions (most recent first)

**2026-04-01:** **What's New / version UI (Cursor):** `ChangelogScreen` opens on **current build only** (matches `BuildConfig.VERSION_NAME`); header `vX.Y.Z (build N)` + **Earlier releases** / **Current release only**; **1.5.0** changelog bullets expanded. Docking Station badge uses `BuildConfig` (removed hardcoded version). `:app:compileDebugKotlin` green.

**2026-04-01:** **v1.5.0 — ROADMAP + SCRATCHPAD + SUMMARY doc pass (Haiku):** ROADMAP Phase 5 amended (12 trainable skills, equipment/companions note); Phase 7 items checked off (haptics ✓, reduce-motion ✓, bank search/sort/presets ✓, WYWA ✓); Phase 9 items checked off (companions ✓, equipment ✓, chronicle/achievements ✓, random events ✓, prestige partial); "Immediate Next Point" rewritten for v1.5.0 standing; SUMMARY + SCRATCHPAD Last Actions + Next Action aligned.

**2026-04-01:** **v1.5.0 — docs + layout pass (Cursor):** `SKILLS_EXPANSION_NATIVE.md` — §0 adds four trainable skills; duplicate **§5** renamed **§5b**; §7 intro/§7.1 counts and Thieving/Farming roadmap callouts amended for **12** trainable + Room v3; dual intro version lines (1.4.5 historical + **1.5.0** canonical). `ROADMAP` Agent Credits row for **1.5.0**. `SkillsScreen` `skillCrossover` extended for Farming / Thieving / Woodworking / Tailoring.

**2026-04-01:** **v1.4.5 — version modalities:** `app/build.gradle.kts` `versionName` **1.4.5** / `versionCode` **7**; `ChangelogScreen` `APP_CHANGELOG` new top card; README release badge **1.4.5**; `ROADMAP` Agent Credits row; `SKILLS_EXPANSION_NATIVE.md` amended with shipped-app version cross-reference.

**2026-04-01:** **Reusable panel back header:** Added `ui/components/PanelBackHeader.kt` (shared back-button header chrome) and applied it to `ChangelogScreen`, `OpenSourceNoticesScreen`, and `CreditsScreen` to standardize "return path" UX across overlays/panels. `:app:compileDebugKotlin` green.

**2026-04-01:** **`SkillsScreen` tablet responsiveness:** Replaced `GridCells.Adaptive(160.dp)` with width-aware fixed columns (`2/3/4/5` at `700/1000/1400dp` breakpoints) and centered content via `widthIn(max = 1280.dp)` so tablets no longer render tiny, over-dense cards. `:app:compileDebugKotlin` green.

**2026-04-01:** **`gradle.properties`:** Removed `android.disallowKotlinSourceSets=false` (experimental; default `true`). Clears AGP sync warning in Android Studio; `:app:compileDebugKotlin` still green.

**2026-04-01:** **Weapon/Tool Mastery system + Martial Arts (Docs + Code):** Designed full **Weapon & Tool Mastery** sub-progression system in `SKILLS_EXPANSION_NATIVE.md` §8 — 11 weapon categories (Sword, Dagger, Axe, Mace, Spear, Bow, Crossbow, Staff, Unarmed, Shield, Thrown) + 8 tool categories (Pickaxe, Hatchet, Fishing Rod, etc.) with passive speed/resource bonuses at mastery milestones (levels 1–50). Includes backend model proposal (`MasteryCategory` enum, `MasteryState` data class, `SkillAction.masteryCategory` optional field, Room `mastery_states` table). Added `MARTIAL_ARTS` to Combat pillar in `SkillId.kt` (unarmed combat, chi strikes, no-gear entry). Roster now **49 skills**. `:app:compileDebugKotlin` green.

**2026-03-31:** **`SkillsScreen` — Logging crossover layout:** Replaced crossover `Row` with `FlowRow` (`@OptIn(ExperimentalLayoutApi)`); added `crossoverChipLabel()` short names (e.g. Woodwork / Fire / Fletch); chips use `maxLines = 1`, `TextOverflow.Ellipsis`, `softWrap = false`; `heightIn(min)` on flow so the strip stays predictable. Resolves tall card + broken FIREMAKING / vertical FLETCHING in 2-up adaptive grid.

**2026-03-31:** **v1.4.1 — docs, version modal, policy:**
- `ChangelogScreen.kt` — new top `APP_CHANGELOG` entry (tag DOCS): About / What's New alignment, contributor note linking `SCRATCHPAD` + version bump + `CLAUDE.md`.
- `app/build.gradle.kts` — `versionName` **1.4.1**, `versionCode` **4**.
- `README.md` — release shield **1.4.1**.
- `CLAUDE.md` — Doc Update Rules (What's New + version), Output Checklist line, Key Files row for `ChangelogScreen.kt`.
- `SCRATCHPAD.md` — Active Status release hygiene bullet + this Last Actions block + Agent Credits.

**2026-03-31:** **v1.4.0 — changelog + version bump:**
- `ChangelogScreen.kt` — `APP_CHANGELOG` first card documents Hub command center, fourth tab + Anchor, Herblore/Scavenging, settings parity, expanded skill roster / Coming Soon.
- `app/build.gradle.kts` — `versionName` **1.4.0**, `versionCode` **3** (already aligned).
- `README.md` — release shield **1.4.0**.
- `DOCS/ROADMAP.md` — `[AMENDED]` under Phase 5 for v1.4.0 scope pointer.

**2026-03-31:** **Hub Screen / Command Center (Claude Opus 4.6):**
- `HubScreen.kt` — new landing tab covering checklist items 1a–1h: inline offline gains card (dismissible, not blocking dialog), active training panel (level ring, pulsing dot, action name, cycle progress bar, time-to-level estimate), quick stats row (total level, bank items, top skill), next milestone nudge card (closest level-up with progress bar + unlock detection), smart suggestion card (craftable opportunity from bank > idle skill prompt), recent level-up mini-cards, empty-hub prompt with "Go to Skills" shortcut.
- `ArteriaBottomBar.kt` — added `AnchorIcon` (Canvas: ring + shank + crossbar + curved flukes); 4 tabs now (Hub/Skills/Bank/Combat); fixed `quadraticBezierTo` → `quadraticTo` deprecation.
- `GameScreen.kt` — tab indices shifted (+1 for Skills/Bank/Combat); added `recentLevelUps: mutableStateListOf` populated from `levelUpEvents`; wired `HubScreen` as tab 0 with offline report, skill tap, and cross-tab navigation callbacks.
- `OfflineReportDialog.kt` — replaced stale `MiningData.items` reference with `SkillDataRegistry.itemName()`.
- Build: `:app:compileDebugKotlin` green, zero warnings.

**2026-03-31:** **Playability expansion + alive UI overhaul (Claude Opus 4.6):**
- **4 new skill data files:** `LoggingData.kt` (8 actions), `FishingData.kt` (8), `SmithingData.kt` (7, ore→bar via `inputItems`), `CookingData.kt` (8, raw fish→cooked via `inputItems`).
- **`SkillDataRegistry.kt`:** Central registry merging all skill data; `actionsForSkill()`, `isSkillImplemented()`, `itemName()`.
- **`SkillAction.inputItems`:** New `Map<String, Int>` field on `GameModels.kt` — crafting actions declare bank item costs.
- **`TickEngine.kt`:** Bank consumption before action completion; `ranOutOfMaterials` auto-stop when bank insufficient.
- **`GameViewModel.kt`:** One-at-a-time training enforcement — `startTraining()` stops all other active skills first.
- **`ArteriaBottomBar.kt`:** Custom 3-tab nav with Canvas-drawn icons (pickaxe, vault, crossed swords), XP progress ring on Skills tab when training, gold badge on Bank when crafting affordable, radial glow on selected tab, gradient background blending into space backdrop.
- **`SkillsScreen.kt`:** Pillar-grouped `LazyVerticalGrid` with colored section headers, animated border colors (training/unimplemented/default), `LevelBadge` with Canvas XP arc, dimmed unimplemented cards with "SOON" label.
- **`SkillDetailScreen.kt`:** `SkillHeroHeader` replacing TopAppBar (horizontal gradient, live training row with dot+name+progress bar, `LevelCircle` with animated XP arc), `ActionCard` with sweep fill progress and pillar-colored Train button, input items display.
- **`GameScreen.kt`:** `AnimatedContent` push/pop transitions for skill detail, `derivedStateOf` for training progress/bank opportunity, wired `ArteriaBottomBar` + `SkillComingSoonDialog`.

**2026-03-31:** **build-apk-for-transfer + jlink:** `gradlew.bat` no longer falls back to `java` on PATH when no JDK found (PATH was still Red Hat JRE). Added Eclipse Adoptium + Microsoft `jdk-*` scan; strip `JAVA_HOME` if path contains `.cursor` or `redhat.java`. `build-apk-for-transfer.ps1` resolves JDK the same way, sets `JAVA_HOME`/`Path`, runs `gradlew --stop` before assemble.

**2026-03-31:** **Skill roster → 41 / 5 pillars:** `SkillId.kt` + `SkillPillar.COSMIC`; `SkillsScreen` / `SkillDetailScreen` pillar tint via `pillarColor`; `CombatAccent` in `ArteriaPalette`; `ChangelogScreen` v1.4.0 + `app/build.gradle.kts` version bump; `CLAUDE.md` tables. **Build unblock:** `MainActivity` `LocalContext` + `getValue`; `UserPreferences.asProvider` + `flow.first`; `SkillStarMap` `nativeCanvas` import (shared `skillCrossover` from `SkillsScreen`); `SkillCard` accent vs map shadowing; `ArteriaApp` wires `GameScreen` reset/delete profile callbacks. `:app:compileDebugKotlin` green.

**2026-03-31:** **Gradle jlink / Cursor JRE:** Root `gradlew.bat` now clears `JAVA_HOME` when `%JAVA_HOME%\bin\jlink.exe` is absent, then picks `%ProgramFiles%\Java\jdk-21.0.10` / `jdk-26` / `jdk-21` if present. `build-with-jdk26.bat` runs `gradlew --stop` then builds with `--no-daemon` so AGP `JdkImageTransform` does not keep using a Red Hat JRE path.

**2026-04-01:** **Herblore + Scavenging:** `HerbloreData.kt` (brews from `flax_bundle` … `star_herb`); `ScavengingData.kt` (junk heap → echo dump, logging-aligned XP/times). Registry + `CLAUDE.md` (`SkillAction` / skill-add task / Key Files), `ARCHITECTURE.md`, `README.md` Skills UX.

**2026-03-31:** **What's New / version bump:** Shipped **v1.3.0** in `ChangelogScreen` (`APP_CHANGELOG` first card) summarizing post–v1.2.0 work: `SkillDataRegistry` skills (Mining through Cooking), `SkillComingSoonDialog`, Game DB v2 + `lastOfflineTickAppliedAt`, transactional `saveGameState`, `Dispatchers.Default` offline ticks, `ArteriaBottomBar`. Android `versionName` → `1.3.0`, `versionCode` → `2`.

**2026-04-01:** **Harvesting skill data:** Added `core/data/HarvestingData.kt` (flax → star herb, levels 1–95, XP/time curve matches logging); merged into `SkillDataRegistry` action/item registries and `actionsForSkill(HARVESTING)`. `CLAUDE.md` “Adding a new skill” references `HarvestingData` as a gathering template.

**2026-03-31:** **Doc pass (Coming Soon skills):** Updated `DOCS/ARCHITECTURE.md` verification snapshot + Screen UI responsibilities; `CLAUDE.md` skill system, Game Screen bullets, Game State, Key Files; `README.md` Key Features + architecture ASCII for `GameScreen`.

**2026-03-31:** **Coming Soon skill modal:** Added `SkillDataRegistry.isSkillImplemented(skillId)`; new `SkillComingSoonDialog` (`Dialog` + `Surface`, `ArteriaPalette`, skill name + enum `description`, "Got it"); `GameScreen` branches `onSkillClick`, `comingSoonSkillId` state, `BackHandler` dismiss. `SkillDetailScreen` empty-actions path kept as fallback.

**2026-03-31:** **STYLE_GUIDE dual-track refresh:** `DOCS/ARTERIA-V1-DOCS/DOCU/STYLE_GUIDE.md` now opens with standard preservation header, a **Contents** table (A.1–A.10 + legacy §1–§10), **Part A** for active Gradle/Kotlin/Compose rules (trace, limits, comments, naming, MVVM/coroutines, Compose touch/theming, Gradle build, errors, feature UI), and **Part B** framing for unchanged legacy RN/Expo sections. `SUMMARY.md` link corrected to `../../SUMMARY.md` from this doc path.

**2026-03-31:** **Dependency sweep (no-bump):** Audited root/app/core Gradle declarations against current SBOM policy. No coordinate changes applied; retained stable pins (`AGP 9.1.0`, Compose plugin `2.3.20`, KSP `2.3.6`, Compose BOM `2026.03.01`, Room `2.8.4`) because available newer lines are predominantly alpha/nightly and not required for active Phase 5 work. Updated `DOCS/SBOM.md` and `README.md` with this decision and next-review cadence.

**2026-03-31:** **Save cadence tests started + green:** Added `GameViewModelTest.tickLoop_doesNotSaveBeforeSaveInterval` and `tickLoop_savesAfterSaveInterval`. Added test-only controls in `GameViewModel` (`setNowProviderForTests`, `setMaxTickIterationsForTests`) to make cadence checks deterministic under virtual time. Ran `:app:testDebugUnitTest` -> **BUILD SUCCESSFUL**.

**2026-03-31:** **Phase 4 closed:** Shipped `GameDatabase` **v2** with `MIGRATION_1_2` (`game_meta.lastOfflineTickAppliedAt`); `GameState`/`GameRepository`/`GameViewModel` persist audit timestamp after offline catch-up; `GameDatabaseMigrationTest` (migration + save/close/reopen); `GameRepositoryTest.saveGameState_roundTripsLastOfflineTickAppliedAt`. `:app:testDebugUnitTest` green. Docs: `ROADMAP` `[DONE]`, `README`, `SBOM`, `ARCHITECTURE` amended.

**2026-03-31:** **README Phase 4 row:** Clarified partial completion vs `DOCS/ROADMAP.md` (Room save/load + `OfflineReportDialog` done; migration round-trip tests + device smoke still open). Updated **Next Immediate Goal** to match consolidated next actions (smoke + `GameViewModel` persistence tests).

**2026-03-31:** **Docs modernization + consistency sweep completed (user-requested content-doc update):** Implemented the attached plan across docs without touching the plan file. Added `Doc Canon` section in `SUMMARY.md`; refreshed `README.md` with canonical project block and fixed stale AI-agent link to root `CLAUDE.md`; added SBOM modernization profile (`Installed`, update channels, review cadence); appended architecture verification snapshot against live route/screen files; added consolidated current-state block to this scratchpad; aligned rule precedence between `CLAUDE.md` and `.claude/cursor.rules`; and resolved stale route references via amendment notes.

**2026-03-31:** **Timestamped named APK output shipped:** Updated `build-with-jdk26.bat` to copy `app-debug.apk` into `dist/` as `ArteriaV2-yyyyMMdd-HHmmss.apk` after successful build and print the final named file path. Verified run produced `dist/ArteriaV2-20260331-123434.apk`.

**2026-03-31:** **APK path visibility fix in JDK 26 build helper:** Updated `build-with-jdk26.bat` so empty args now default to `:app:assembleDebug` (instead of Gradle `help` behavior). On successful completion, the script now always prints:
- output directory: `app/build/outputs/apk/debug`
- expected APK path: `app/build/outputs/apk/debug/app-debug.apk`
Verified by running `build-with-jdk26.bat` with no args; build completed and printed the resolved path.

**2026-03-31:** **Animation system build verified + docs updated:** `:app:compileDebugKotlin` and `:app:assembleDebug` both pass. Added terminal timeline node pulse animation (2.4s period, 0.28→0.55 alpha) to `AccountSelectionScreen.kt`. Fixed `DockingAccountCard.kt` `drawBehind` scale reference (applied to radius not transform). Documented full animation architecture: `DockingGlitch.kt` (7-layer glitch engine with precomputed layouts, entry orchestrator via `Animatable`, ambient infinite transitions with IDLE sentinel); `DockingAccountCard.kt` (timeline sidebar with ripple ring + energy flow, hex ghost text overlay, skill badge spring pop-in, pulsing border/tint wash on selected); `AccountSelectionScreen.kt` (terminal node pulse). Updated `SCRATCHPAD.md` agent credits.

**2026-03-31:** **CLAUDE.md overhaul + cursor.rules integration:** Absorbed `.claude/cursor.rules` agent philosophy into `CLAUDE.md`. Added: workflow init order (SUMMARY→SBOM→SCRATCHPAD→STYLE_GUIDE), agent behavior rules (read-before-edit, leaf→entry multi-file ordering, scope discipline), Kotlin async standards (viewModelScope only, no GlobalScope), error handling (sealed results, no silent failures), git Conventional Commits format, output checklist. Also documented the full `DockingGlitch` animation system in component library table and updated accurate skill inventory (16 skills, 4 pillars) from live `SkillId.kt`. **`[AMENDED 2026-03-31]:`** Superseded by **41 skills / 5 pillars** — see current `SkillId.kt`.

**2026-03-31:** **Docking Station animation system shipped:** Created `DockingGlitch.kt` — full glitch materialization engine with 7 Canvas layers (phosphor veil, CRT scan sweep, RGB chromatic aberration, block displacement, noise grain, interference scanlines, stabilization flicker), `AmbientScanOverlay` for selected cards, `GlitchLayout` precomputed deterministically per seed, `EntryAnimations` orchestrator (staggered per `entryIndex × 160ms`), `AmbientAnimations` with `IDLE` sentinel. Upgraded `DockingAccountCard.kt` with timeline sidebar (node + ripple ring + energy flow particle on connector), hex ghost text overlay during glitch, skill pillar badges with spring pop-in, pulsing border + tint wash on selected cards. Updated `AccountSelectionScreen.kt` with timeline terminal node (pulsing `LuminarEnd` dot) and "Enter timeline" CTA.

**2026-03-31:** **Main menu UX refactor — Settings overlay + TopAppBar:** Removed `SETTINGS` from bottom navigation `GameTab` enum (now 3 tabs: Skills, Bank, Combat). Added `TopAppBar` with account display name (Cinzel, truncated) + gear settings icon. Tapping gear opens `SettingsScreen` as full-screen overlay with back arrow button + space gradient background. Wired `BackHandler` to close settings on Android back-press. Updated `GameScreen.kt` (line 64+, Scaffold structure) and `SettingsScreen.kt` (new `onBack` parameter). Aligns with menu redesign Section 4 checklist (4a–4e).

**2026-03-31:** **In-app version history:** Created `ChangelogScreen.kt` composable with `APP_CHANGELOG` data structure (4 entries: v0.1.0 FOUNDATION, v1.0.0 LAUNCH, v1.1.0 UI refactor, v1.2.0 Docking animations). Full-screen overlay with space gradient background, back arrow header, version cards with colored tag badges (UI/ENGINE/FIX/LAUNCH), dates, and bullet-point change lists. Wired “What's New” OutlinedButton from `SettingsScreen` and small TextButton from `AccountSelectionScreen` using BackHandler overlay pattern. Matches existing architecture & design system (ArteriaPalette, Cinzel typography, Surface cards).

**2026-03-31:** **ROADMAP check-off alignment:** Audited current app state and updated `DOCS/ROADMAP.md` with `[DONE 2026-03-31]` for Phase 1 and Phase 3, `[AMENDED]` partial completion note for Phase 4, and `[IN PROGRESS]` status for Phase 5 (Mining + Bank slice). Added a single “Immediate Next Point” checklist to drive manual smoke completion.

**2026-03-31:** **SBOM schema upgrade:** `DOCS/SBOM.md` now tracks dependency state with explicit `Installed` and `Next available` columns for Build Toolchain + `:app` + `:core` tables to make upgrade planning visible at a glance.

**2026-03-31:** **Kotlin + dependency max-update pass (no-test run):** Updated root plugin versions to **Kotlin Compose `2.3.20`** and **KSP `2.3.6`**; updated app deps to **Compose BOM `2026.03.01`** and **Room `2.8.4`** (`runtime`, `ktx`, `compiler`, `room-testing`). Kept AGP at `9.1.0` (latest stable line), Navigation `2.9.7`, Lifecycle `2.10.0`, Activity Compose `1.13.0`, Core KTX `1.18.0` as already current stable. No tests executed per user instruction.

**2026-03-31:** **Gradle wrapper + JDK 26 helper:** Updated `gradle/wrapper/gradle-wrapper.properties` to **`9.6.0-20260331012943+0000`** (via `./gradlew wrapper --gradle-distribution-url=...`). Gradle’s CLI rejected the screenshot string `9.6.0-202603311012943+0000` (invalid / 404 on `services.gradle.org`); used the published 2026-03-31 snapshot instead. Added repo-root **`build-with-jdk26.bat`**: runs `gradlew.bat` with `-Dorg.gradle.java.home=C:\Program Files\Java\jdk-26` so it overrides `gradle.properties`’s JDK 21. **User preference [2026-03-31]:** do not run unit/instrumented **tests** in agent chats unless explicitly requested.

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

**`[AMENDED 2026-04-01]:`** **v1.5.0 standing** — 12 skills live, equipment + companions integrated, docs aligned. Next priorities:
1. **Manual smoke — new skills:** Train Farming/Thieving (verify XP + bank item gain), train Woodworking/Tailoring (verify log/flax consumed from bank), equip a tool and confirm XP boost, summon companion and confirm passive applies during tick.
2. **Prestige multipliers:** `PrestigeScreen` UI is complete but perk boosts are not wired into `TickEngine` — hook `PrestigePerks` into XP/resource multiplier path (same pattern as `gearXpMultiplier`).
3. **Combat baseline:** Wire the first idle combat loop — `Attack`/`Strength` XP per enemy, `Hitpoints` XP, drops to bank. Use existing `TickEngine` action pattern; add `CombatData.kt` in `:core`.
4. **Companion DB persistence:** `activeCompanionId` persists via `game_meta`, but `ownedCompanions` (unlocked roster) is currently derived from the full registry (all companions available). If gated unlocking is desired, add a `companion_states` Room table (migration v4).

**`[AMENDED 2026-03-31]:`** Animation polish is shipped. Immediate priorities:
1. **Device smoke test:** Install APK, navigate account select → create → continue → game screen. Verify account name + gear icon visible in TopAppBar, settings overlay opens/closes cleanly, back gesture/button work.
2. **Timeline + glitch visual verification:** Watch the Docking Station list populate — check staggered CRT scan-ins with glitch artifacts, skill badges pop in with overshoot spring motion, terminal node pulses steadily, energy particles flow on selected card's connector line.
3. **Optional refinement:** Tweak animation timings if they feel too slow/fast (see `rememberEntryAnimations` and `rememberAmbientAnimations` phase delays and durations).

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
