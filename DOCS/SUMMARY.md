<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-29 | Cursor Agent | GPT-5.2 | **Chronicle unlock UX:** `AchievementUnlockBanner` (queued top toast, rarity border, tap → Chronicle) + `AchievementDecor` (palette-only tints for banner + Chronicle); themed level-up snackbars; Hub strip copy fix (“Recent level-ups”); **`DOCS/ARCHITECTURE.md`** verification line; **`v1.10.2` (15)** What’s New + README. |
| 2026-04-21 | Antigravity | Claude Sonnet 4.6 (Thinking) | **Forging/Jewelcrafting/Firemaking Expansion:** Implemented 3 new crafting skills bridging the gear gap; updated Mining gems; Phase 5 near-complete. |
| 2026-04-01 | Claude Haiku 4.5 | Anthropic Claude | **v1.5.0 doc sync:** ROADMAP Phase 5/7/9 done-markers; "Immediate Next Point" rewritten; SCRATCHPAD Last Actions + Next Action aligned; SUMMARY credits row. |
| 2026-04-01 | Cursor Agent | Composer | Added **[SKILLS_EXPANSION_NATIVE.md](SKILLS_EXPANSION_NATIVE.md)** (V1 DOCU → V2 skill playbook); doc index + read-order row. |
| 2026-03-30 | Cursor Agent | GPT-5.3 Codex (Cursor) | Amended hub: local checkout folder name `Arteria-V2-Gradle-Edition-Reloaded` vs doc `Arteria-Gradle-Edition-V2/` alias. |
| 2026-03-30 | Cursor Agent | Composer | Initial `DOCS/SUMMARY.md` — V2 hub, canonical paths, AI workflow for features/QoL/UI. |

*Future contributors: append a row when you materially change this doc.*

---

# SUMMARY — Arteria Gradle Edition V2

**What this is:** Native Android track (Kotlin, Jetpack Compose, Gradle 9.x line). This folder is the active V2 implementation and product track.

**Gradle project root:** `Arteria-Gradle-Edition-V2/` (contains `settings.gradle.kts`, `:app`, `:core`). Open this directory in Android Studio.

**`[AMENDED 2026-03-30]:`** The same project may live in a folder named **`Arteria-V2-Gradle-Edition-Reloaded`** — treat it as the same Gradle root if those files are present.
**`[AMENDED 2026-03-31]:`** For this repository checkout, prefer **`Arteria-V2-Gradle-Edition-Reloaded/`** as canonical local path naming.

**`[AMENDED 2026-03-30]:`** Any Expo / React Native mentions in this repo are **legacy V1 context only**. Current implementation stack is native Android: **Kotlin + Jetpack Compose + Room + Gradle 9.x + AGP 9.1**.

**`[AMENDED 2026-03-30]:`** Do not add Expo/React Native dependencies or workflow steps to this project.

## [AMENDED 2026-03-31] Doc Canon (single source rules)

- **Canonical local root:** `Arteria-V2-Gradle-Edition-Reloaded/` (this checkout).
- **Legacy alias in older docs:** `Arteria-Gradle-Edition-V2/` (same project if `settings.gradle.kts`, `:app`, `:core` exist).
- **Stack truth for this repo:** native Android only (`Kotlin + Compose + Room + Gradle/AGP`).
- **Design truth precedence:** monorepo `../DOCU/` first, bundled `DOCS/ARTERIA-V1-DOCS/DOCU/` second.
- **Operational doc precedence:** `SBOM.md` (versions), `ARCHITECTURE.md` (runtime shape), `ROADMAP.md` (phase status), `SCRATCHPAD.md` (live handoff).

---

## Source of truth (read order for any AI / new contributor)

| Order | Doc | Why |
|-------|-----|-----|
| 1 | [SCRATCHPAD.md](SCRATCHPAD.md) | Current phase, blockers, last actions, **next action**. |
| 2 | [ROADMAP.md](ROADMAP.md) | Phased delivery including **platform (0–6)** and **features / QoL / UI (7+)**. |
| 3 | [ARCHITECTURE.md](ARCHITECTURE.md) | Stack decisions, RN vs native split, module layout. |
| 4 | [MIGRATION_SPEC.md](MIGRATION_SPEC.md) | How to translate RN + TypeScript engine patterns → Kotlin / Compose. |
| 5 | [SBOM.md](SBOM.md) | Toolchain and dependency versions — verify before changing Gradle or SDKs. |
| 6 | [SKILLS_EXPANSION_NATIVE.md](SKILLS_EXPANSION_NATIVE.md) | **Skill roster vs trainables:** V1 `DOCU` references (workbench UI, skilling guides, synergies design-only) + native checklist to ship `*Data.kt` + registry. **`[AMENDED 2026-04-01]:`** |

---

## Game design & UI intent (features, QoL, polish)

**Canonical (monorepo):** `../DOCU/` relative to this folder — especially:

- `MASTER_DESIGN_DOC.md`, `TRUTH_DOCTRINE.md`, `ROADMAP.md` (shipping app roadmap — cross-check for feature parity targets)
- `IMPROVEMENTS.md`, `CURRENT_IMPROVEMENTS.md` — prioritized UX and feature ideas
- `UI_REVISION_CRAFTING_v2.md`, `THEMING.md`, `zhip-ai-styling.md`, `STYLE_GUIDE.md` — UI/QoL alignment

**Bundled copy (offline / historical):** [ARTERIA-V1-DOCS/DOCU/](ARTERIA-V1-DOCS/DOCU/) — same filenames. If **root `DOCU/`** and the copy disagree, **root wins**.

**Detailed content audit:** [DESIGN_TRANSLATION_AUDIT.md](DESIGN_TRANSLATION_AUDIT.md).

**`[AMENDED 2026-04-29]:`** **Achievements vs hub copy:** Chronicle **trophy** unlocks toast at the game shell top (`AchievementUnlockBanner`, queued bursts); tint math is shared via `AchievementDecor` with `ChronicleScreen`. The Hub ticker is **recent level-ups** (skill level gains), not Chronicle entries — see `ARCHITECTURE.md` verification snapshot.

---

## Aligning multiple AIs on the same work

1. **Claim a slice:** Add or update a line in `SCRATCHPAD.md` (Last Actions + Next Action) so parallel sessions do not collide.
2. **Track delivery:** Mark items in `ROADMAP.md` with `[DONE YYYY-MM-DD]:` or `[IN PROGRESS …]:` per preservation rules — never delete old text.
3. **Design parity:** Before UI work, open the matching RN screen under monorepo `apps/mobile/app/` and the relevant `DOCU/` spec.
4. **After dependency changes:** Update `SBOM.md` the same session.

---

## Mission Checklist (Zen-Dhawan Recovery)

- [x] Recover zen-dhawan Kotlin files from `@.claude/worktrees/zen-dhawan`.
- [x] Place recovered files into canonical app paths (`app/src/main/java/com/arteria/game/{core,data/ui}`).
- [x] Wire nav handoff from account flow into `game/{profileId}` route.
- [x] Add game-state Room stack (`arteria_game.db`) and tick/offline loop wiring.
- [x] Run build verification (`:app:assembleDebug`) and unit test verification (`:app:testDebugUnitTest`).
- [x] Add/refresh initial tests for `TickEngine` and `GameRepository`; fix `AccountViewModelTest`.
- [ ] Run manual device smoke test (account create/select -> enter game -> switch account).
- [ ] Add/refresh `GameViewModel` behavior tests (offline load, training start/stop, save cadence).
- [x] **`[DONE 2026-04-01]:`** Domain engine moved into Gradle **`:core`** JVM module (`core/src/main/kotlin/com/arteria/game/core/`); `:app` uses `implementation(project(":core"))`.

---

## Documentation index (this `DOCS/` folder)

| Doc | Role |
|-----|------|
| **SKILLS_EXPANSION_NATIVE.md** | V1-informed playbook for expanding **trainable** skills on native V2 (`SkillId` vs `SkillDataRegistry`, `TickEngine`, checklist). **`[AMENDED 2026-04-01]:`** |
| **SUMMARY.md** | This hub — paths, reading order, AI alignment. |
| **SCRATCHPAD.md** | Live handoff. |
| **ROADMAP.md** | Full phased plan. |
| **ARCHITECTURE.md** | System structure. |
| **MIGRATION_SPEC.md** | TS / RN → Kotlin mapping. |
| **SBOM.md** | Versions and bill of materials. |
| **REFERENCES.md** | External official links. |
| **DESIGN_TRANSLATION_AUDIT.md** | File-level port checklist from V1. |
| **Gradle_Tool_Chain_Migration.md** | Daemon JVM / toolchain notes. |
| **gradle_website_links.md** | Legacy pointer; prefer REFERENCES. |

---

## Quick link — root README

Monorepo overview and RN commands: [../README.md](../README.md).
