<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | Initial **SKILLS_EXPANSION_NATIVE.md**: V1 design sources → V2 native playbook; roster vs implementation; vertical-slice steps; synergy/UI deferrals; cross-links to bundled V1 DOCU. |

*Future contributors: append a row when you materially change this doc.*

---

# Skills expansion — Native V2 playbook (V1-informed)

> **Purpose:** Give contributors a single place to understand how **skill roster expansion** works in **Gradle V2 (Kotlin / `:core` / Room)** and how to use **bundled V1 docs** for *content and UX intent* without copying RN/Expo implementation detail literally.  
> **Audience:** Implementers, designers, and AI agents extending trainable skills.  
> **Last updated:** 2026-04-01

---

## 1) How native V2 models skills today (facts)

| Layer | Role |
|-------|------|
| **`SkillId` / `SkillPillar`** (`core/.../skill/SkillId.kt`) | Full **roster** (~41 skills, 5 pillars). Enum order and display copy are canonical for UI lists. |
| **`*Data.kt` + `SkillDataRegistry`** (`core/.../data/`) | **Trainable surface:** only skills with non-empty `actionsForSkill` are “implemented.” |
| **`SkillAction` + `TickEngine`** (`core/.../model/`, `core/.../engine/`) | One action at a time per skill loop; `inputItems` consume bank on completion; `resourceId` adds to bank. |
| **`GameRepository` + Room** (`app/.../data/game/`) | Persists **every** `SkillId` key in `GameState.skills` (missing keys merged on load). |
| **UI** (`SkillComingSoonDialog` vs `SkillDetailScreen`) | Unimplemented enum entries still appear in the grid; taps show **Coming Soon** until registry has actions. |

**Implication:** Adding an enum entry is **cheap**; shipping a skill is **data + registry + persistence sanity + detail screen**. Large roster with few implementations is intentional.

---

## 2) What V1 documentation is for (read order)

Bundled under **`DOCS/ARTERIA-V1-DOCS/DOCU/`** (mirror of monorepo `DOCU/`; if they conflict, monorepo root wins per `SUMMARY.md`).

| V1 doc | Use for native V2 |
|--------|-------------------|
| **[SKILLS_ARCHITECTURE.md](ARTERIA-V1-DOCS/DOCU/SKILLS_ARCHITECTURE.md)** | **UX patterns** (e.g. Woodworking “workbench”: hero header, category rail, recipe cards, sticky CTA). **Design splits:** Farming = cultivated vs Harvesting = wild; growth timers vs instant nodes. Treat RN file paths as **historical**; port *behavior* to Compose. |
| **`skilling_guides/`** (`mining_reference.md`, `logging_reference.md`, `smithing_reference.md`, `*_master_item_list.md`) | **Content density:** level bands, item names, tier ideas — translate into `ItemDef` + `SkillAction` rows (ids snake_case). |
| **[SYNERGIES.md](ARTERIA-V1-DOCS/DOCU/SYNERGIES.md)** | **Future systems layer** — pair/chain/cross-pillar bonuses. V2 **does not** need this to add skills; track as Phase 7+ / design-only until `TickEngine` or a dedicated modifier layer exists. |
| **[MASTER_DESIGN_DOC.md](ARTERIA-V1-DOCS/DOCU/MASTER_DESIGN_DOC.md)** | Tone, progression philosophy, pillar balance — **truth** for *why*; not step-by-step Kotlin. |
| **[UI_REVISION_CRAFTING_v2.md](ARTERIA-V1-DOCS/DOCU/UI_REVISION_CRAFTING_v2.md)** | Crafting presentation and clarity goals when upgrading `SkillDetailScreen` beyond list+cards. |

---

## 3) Native V2 “our own” expansion policy (recommended)

**KISS / vertical slice**

1. **Prefer new tiers on existing skills** (more `SkillAction` rows) before adding a new `SkillId`, unless the fantasy is clearly a separate progression (e.g. **Smithing** = bars, **Forging** = equipment — both already exist as enums; only Smithing has data today).

2. **Reuse economy edges:** next skills that consume **existing bank ids** land faster (e.g. **Firemaking**: logs → outputs; **Forging**: bars → gear).

3. **One skill = one `*Data.kt` object** (or split by file if >400 lines per `STYLE_GUIDE` / `CLAUDE.md` limits) + **registry** `putAll` + **`when` branch** in `actionsForSkill`.

4. **Do not block on synergies, mastery, or multi-phase growth** until the idle core is stable; V1 `SYNERGIES.md` explicitly calls the old stub non-mechanical — same discipline on V2.

5. **UI parity is incremental:** start with current `SkillDetailScreen` pattern; adopt V1 “workbench” layout per skill when that skill justifies the complexity (Woodworking-class skills).

---

## 4) Checklist — ship a new trainable skill (implementation)

1. Confirm **`SkillId` exists** (add enum entry only if missing — order with pillar group).
2. Add **`YourSkillData.kt`** in `core/src/main/kotlin/com/arteria/game/core/data/`: `items`, `actions`, `actionRegistry` (follow `MiningData` / `CookingData` patterns).
3. Update **`SkillDataRegistry`**: `putAll` in `actionRegistry` + `itemRegistry`; extend **`actionsForSkill`** `when`.
4. **`GameRepository`** load path: ensure new skill gets default `SkillState` if not present (usually “merge all enum keys” already does this — verify when adding **new** enum values).
5. **`./gradlew :core:test`** if you add tick-sensitive math; **`./gradlew :app:testDebugUnitTest`** if persistence or VM behavior changes.
6. **Player-facing:** if the release is user-visible, follow `CLAUDE.md` / `SCRATCHPAD`: `ChangelogScreen` `APP_CHANGELOG`, `versionCode` / `versionName`, README badge.

---

## 5) Recent product expansion context (handoff)

**`[AMENDED 2026-04-01]:`** Scratchpad credits mention **v1.5.0 QoL** work (achievements, random events, equipment, companions, prestige, bank UX, haptics). Those systems extend **meta / gear / narrative** layers in `:app`. This doc stays focused on **skill trainables** in `:core` + registry; cross-feature hooks (e.g. achievements on level-up) should be documented in **`ARCHITECTURE.md`** / **`SCRATCHPAD.md`** as those modules stabilize.

---

## 6) Cross-references (V2 repo)

| Topic | Where |
|-------|--------|
| Phase / spine plan | `DOCS/ROADMAP.md` (Phase 5 content port) |
| TS → Kotlin engine mapping | `DOCS/MIGRATION_SPEC.md` |
| File-level V1 port checklist | `DOCS/DESIGN_TRANSLATION_AUDIT.md` |
| Runtime UI + nav | `DOCS/ARCHITECTURE.md` |
| Agent handoff | `DOCS/SCRATCHPAD.md` |
