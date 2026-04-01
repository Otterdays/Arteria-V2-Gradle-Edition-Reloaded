<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | Initial **SKILLS_EXPANSION_NATIVE.md**: V1 design sources → V2 native playbook; roster vs implementation; vertical-slice steps; synergy/UI deferrals; cross-links to bundled V1 DOCU. |
| 2026-04-01 | Cascade | SWE-1.5 | Added **Section 7**: Prioritized skill expansion recommendations; skill split proposals (Firemaking → Bonfire Craft, Runecrafting → Glyph Inscription); implementation phases; synergy opportunities. |

*Future contributors: append a row when you materially change this doc.*

---

---

# Skills expansion — Native V2 playbook (V1-informed)

> **Purpose:** Give contributors a single place to understand how **skill roster expansion** works in **Gradle V2 (Kotlin / `:core` / Room)** and how to use **bundled V1 docs** for *content and UX intent* without copying RN/Expo implementation detail literally.  
> **Audience:** Implementers, designers, and AI agents extending trainable skills.  
> **Last updated:** 2026-04-01

---

## 0) Skill Glossary — Current Status

### ✅ Implemented (Trainable)
These skills have active data in `:core` and are fully playable in the current build.
- **Mining** (Gathering): Base ores and gems.
- **Logging** (Gathering): Wood types for crafting.
- **Fishing** (Gathering): Raw fish for cooking.
- **Harvesting** (Gathering): Herbs and botanical reagents.
- **Scavenging** (Gathering): Salvaged parts and scrap.
- **Smithing** (Crafting): Smelting ores into bars.
- **Cooking** (Crafting): Preparing food from fish/crops.
- **Herblore** (Crafting): Brewing potions from harvested reagents.

### ⏳ To-Add (In Roster / Planned Splits)
These exist in the `SkillId` enum but lack data, or are identified for **2-out-of-1 splits** to create deep feature loops.

| Pillar | Skill | Expansion Path / "App" Slice |
| :--- | :--- | :--- |
| **Gathering** | **Farming** | Split into **Agriculture** (Crops) and **Husbandry** (Animals). |
| **Gathering** | **Thieving** | Expand into **Pickpocketing** (Fast) and **Infiltration** (High-stakes). |
| **Crafting** | **Tailoring** | Split into **Weaving** (Refining fibers) and **Leatherworking** (Gear). |
| **Crafting** | **Construction** | Split into **Masonry** (Structures) and **Carpentry** (Furniture/Utility). |
| **Crafting** | **Forging** | Extension of Smithing; focused on high-tier equipment crafting. |
| **Support** | **Exploration** | Split into **Cartography** (Map nodes) and **Archaeology** (Rare relics). |
| **Support** | **Barter** | Trade routes, NPC favors, and economy management. |
| **Cosmic** | **All Cosmic** | Late-game "Prestige" layer; requires multi-skill mastery. |

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

---

## 7) Prioritized Skill Expansion Recommendations

**`[AMENDED 2026-04-01]:`** Based on current implementation gap (8/41 skills) and existing economy edges, here are prioritized expansions that maximize cross-skill synergy and reuse existing resources.

### 7.1 Current State Analysis

**Implemented Skills (8):** Mining, Logging, Fishing, Harvesting, Scavenging, Smithing, Cooking, Herblore  
**Available Resource Outputs:** Ores, Logs, Fish, Herbs, Crops, Scrap, Bars, Cooked Food  
**Critical Gap:** No consumer skills for Logging output, limited utility skills, no risk/reward gathering

### 7.2 Skill Split Proposals (2-for-1)

Consider splitting complex skills into focused specializations:

| Current Skill | Split Into | Rationale |
|---------------|------------|-----------|
| **Firemaking** | Firemaking (1-50) + Bonfire Craft (50+) | Basic burning vs community/multi-log combinations |
| **Runecrafting** | Runecrafting (1-40) + Glyph Inscription (40+) | Basic runes vs complex magical matrices |

### 7.3 Phase 1: Low-Hanging Fruit (Immediate Priority)

**Skills that consume existing resources with minimal new systems:**

#### 7.3.1 Fletching (Crafting Pillar)
- **Why:** Perfect consumer of Logging output, feeds Ranged combat
- **Economy Flow:** Logs → Arrow shafts → Complete arrows/bows
- **Cross-skill inputs:** Logs (Logging) + Feathers (Fishing) + Arrowheads (Smithing)
- **Implementation:** Simple `inputItems` recipes, no new mechanics
- **Synergy potential:** "Double Duty" with Woodworking, "Arsenal" with Smithing

#### 7.3.2 Firemaking (Crafting Pillar)
- **Why:** Consumes Logging surplus, provides utility buffs
- **Economy Flow:** Logs → Heat/Light/Cooking bonuses
- **Cross-skill inputs:** Logs only (simple implementation)
- **Implementation:** Basic burning actions with timed benefits
- **Synergy potential:** "Flame & Feast" with Cooking (already designed in V1 docs)

#### 7.3.3 Agility (Support Pillar)
- **Why:** No items required, pure XP progression, different gameplay style
- **Economy Flow:** Time investment → Movement speed/energy restoration
- **Cross-skill inputs:** None (self-contained progression)
- **Implementation:** Course-based actions with success rates and timers
- **Synergy potential:** "Steady Hands" type bonuses for gathering skills

### 7.4 Phase 2: Economic Expansion (Medium Priority)

**Skills that add new mechanics but reuse existing frameworks:**

#### 7.4.1 Thieving (Gathering Pillar)
- **Why:** Alternative gold source, adds risk/reward excitement
- **Economy Flow:** NPC/Stall targets → Gold + item drops
- **Cross-skill inputs:** None (creates new gold flow)
- **Implementation:** Success rate mechanics, failure stun, loot tables
- **Synergy potential:** Risk reduction combos with Support skills

#### 7.4.2 Farming (Gathering Pillar)
- **Why:** Alternative to Harvesting, introduces time-based progression
- **Economy Flow:** Seeds → Growth timers → Harvested crops
- **Cross-skill inputs:** Seeds (purchased/thieved), tools (Smithing)
- **Implementation:** Growth timer system, patch management
- **Synergy potential:** "Herb Garden" with Herblore (already designed)

### 7.5 Phase 3: Complex Systems (Advanced Priority)

#### 7.5.1 Construction (Crafting Pillar)
- **Why:** Major resource sink, consumes multiple skill outputs
- **Economy Flow:** Planks + Nails + Cloth → Furniture/Rooms
- **Cross-skill inputs:** Woodworking (planks), Smithing (nails), Tailoring (cloth)
- **Implementation:** Complex recipes, housing system integration
- **Synergy potential:** Chain synergies with all production skills

### 7.6 Implementation Strategy

**Phase 1 Criteria:**
- Consumes existing bank items
- Simple `inputItems` → `resourceId` flow
- No new core mechanics required
- High cross-skill synergy value

**Phase 2 Criteria:**
- Introduces 1-2 new mechanics (success rates, timers)
- Moderate cross-skill integration
- Expands gameplay variety

**Phase 3 Criteria:**
- Multiple system dependencies
- Major resource sink potential
- Long-term player retention value

### 7.7 Synergy Integration Notes

From **`SYNERGIES.md`**, these synergies become available with recommended skills:

| New Skill | Existing Synergies Activated |
|-----------|------------------------------|
| Fletching | Double Duty (with Woodworking) |
| Firemaking | Flame & Feast (with Cooking) |
| Agility | Steady Hands (with Mining), Iron Will (with Smithing) |
| Thieving | New risk/reward synergies with Support skills |
| Farming | Herb Garden (with Herblore) |
| Construction | Lumberjack chain (Logging + Woodworking + Construction) |

**Recommendation:** Start with **Fletching** and **Firemaking** as they perfectly demonstrate the "reuse economy edges" principle and activate existing designed synergies.
