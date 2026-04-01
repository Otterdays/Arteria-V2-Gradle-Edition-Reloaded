<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

## Agent Credits

| Date | Agent | Model / Tooling | Contribution |
|------|-------|-----------------|--------------|
| 2026-04-01 | Cursor Agent | Composer | **Version cross-ref:** Amended intro with shipped app **1.4.5** parity note (`build.gradle.kts`, `APP_CHANGELOG`, README badge). |
| 2026-04-01 | Cursor Agent | Composer | Initial **SKILLS_EXPANSION_NATIVE.md**: V1 design sources → V2 native playbook; roster vs implementation; vertical-slice steps; synergy/UI deferrals; cross-links to bundled V1 DOCU. |
| 2026-04-01 | Cascade | SWE-1.5 | Added **Section 7**: Prioritized skill expansion recommendations; skill split proposals (Firemaking → Bonfire Craft, Runecrafting → Glyph Inscription); implementation phases; synergy opportunities. |
| 2026-04-01 | opencode | qwen3.6-plus-free | **Section 9**: New skill recommendations (Gemcutting, Salvaging, Jewelcrafting); economy loop analysis; checklist status audit; game mode integration notes; v1.5.0 feature hooks. |

*Future contributors: append a row when you materially change this doc.*

---

---

# Skills expansion — Native V2 playbook (V1-informed)

> **Purpose:** Give contributors a single place to understand how **skill roster expansion** works in **Gradle V2 (Kotlin / `:core` / Room)** and how to use **bundled V1 docs** for *content and UX intent* without copying RN/Expo implementation detail literally.  
> **Audience:** Implementers, designers, and AI agents extending trainable skills.  
> **Last updated:** 2026-04-01

**`[AMENDED 2026-04-01]:`** **Shipped app version** for player-facing parity is **1.4.5** — keep in sync with `app/build.gradle.kts` (`versionName` / `versionCode`), the top entry in `ChangelogScreen.kt` → `APP_CHANGELOG`, and the README release badge.

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
| **Gathering** | **Trapping** | Set snares. Standalone passive loops yielding game, pelts, and rare meats. |
| **Gathering** | **Siphoning** | Extract magical essences and planar residue. Feeds Enchanting and Cosmic loops. |
| **Crafting** | **Runecrafting** | Crafting runes from essence. Could split to **Essence Mining** (Gathering) + **Runecrafting**. |
| **Crafting** | **Forging** | Capstone metalworking. Uses bars from **Smithing** to craft weapons/armor. |
| **Crafting** | **Firemaking** | Utility loop. Enhances camping/offline buffs. Provides ashes for **Alchemy**. |
| **Crafting** | **Woodworking** | Refines logs into staves, bows, and shields. Feeds **Fletching** and **Magic**. |
| **Crafting** | **Tailoring** | Split into **Weaving** (Refining fibers) and **Leatherworking** (Gear). |
| **Crafting** | **Fletching** | Ranged gear creation. Arrows, bolts, darts. Depends on **Woodworking** & **Smithing**. |
| **Crafting** | **Construction** | Split into **Masonry** (Structures) and **Carpentry** (Furniture/Utility). |
| **Crafting** | **Alchemy** | Extension of Herblore. Transmutations and volatile battle concoctions. |
| **Crafting** | **Jewelcrafting**| Cuts gems (from Mining) & pairs with bars to make magical accessories. High value sink. |
| **Crafting** | **Tinkering** | Uses salvage to build automated resource-collectors, bombs, and clockwork gadgets. |
| **Crafting** | **Enchanting** | Imbues gear with magical stats. Consumes runes and siphoned essences. |
| **Combat** | **Melee (Attack/Str/Def)** | Core combat loop. Requires gear from **Forging**. |
| **Combat** | **Hitpoints / Constitution**| Vitality and health regen modifiers. |
| **Combat** | **Ranged** | Ranged combat. Requires ammo from **Fletching**. |
| **Combat** | **Sorcery** | Spellcasting loop. Requires runes from **Runecrafting**. |
| **Combat** | **Slayer** | Bounty loop. Talk to NPC → get target → fight → earn unique drops & craft Slayer gear. |
| **Combat** | **Martial Arts** | Unarmed combat. No-gear entry point; feeds `UNARMED` mastery. Chi Wraps from Tailoring. |
| **Support** | **Agility** | Global action speed modifiers. Obstacle courses (mini-game slice). |
| **Support** | **Wizardry** | Arcane research. Unlocks spells for **Sorcery** and recipes for **Alchemy**. |
| **Support** | **Exploration** | Split into **Cartography** (Map nodes) and **Archaeology** (Rare relics). |
| **Support** | **Astrology** | Passive buffs via star sign studying. Slow, low-interaction background skill. |
| **Support** | **Summoning** | Pet/Familiar loop. Requires charms (Combat drops) + items to make pouches. |
| **Support** | **Cleansing** | Item purification. Removing curses from corrupted loot. |
| **Support** | **Barter** | Trade routes, NPC favors, merchant contracts, and economy management. |
| **Support** | **Research** | Talent tree points. Passive knowledge accumulation over time. |
| **Support** | **Leadership** | Companion management. Sending idle NPCs on missions (Expedition slice). |
| **Support** | **Resonance** | Global aura / tempo management. Haste buffs based on continuous interaction. |
| **Support** | **Divination** | Predict random events, track rare spawns, and significantly boost luck-based drops. |
| **Support** | **Barding** | Perform music using crafted instruments to provide extensive offline party-buffs. |
| **Cosmic** | **Chaos Theory** | Endgame RNG manipulation and drop-rate boosters. |
| **Cosmic** | **Aether Weaving** | Tier 8+ legendary gear creation, overriding normal crafting caps. |
| **Cosmic** | **Void Walking** | Portal unlocking, instanced raid tier access. |
| **Cosmic** | **Celestial Binding** | Spirit contracts for permanent offline automation. |
| **Cosmic** | **Chronomancy** | Time skip economy, offline tick acceleration. |

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

## 4) Expansion Strategy: The "2-out-of-1" Split
To maintain **KISS** codebases, we break complex "Mega-Skills" into focused loops. This allows each skill to stay under file-length limits and creates clear economy interlocks.

### 🛡️ Example: Tailoring → Weaving + Leatherwork
- **Level 1 (Direct Implementation):** Take fibers (from Harvesting) or wool (from Husbandry) and process them in **Weaving** (Refining-class).
- **Level 2 (Economy Bridge):** Use the woven bolts in **Tailoring** (Crafting-class) to make bags or armor.
- **Why:** Keeps refining logic (timers, materials) separate from crafting logic (blueprints, equipment tiers).

### 🐄 Example: Farming → Agriculture + Husbandry
- **Agriculture**: High-volume/fast cycles for food/herbs.
- **Husbandry**: High-value/slow "App" slice. Raising animals on timers to harvest hides/wool.
- **Why:** Husbandry acts as the cornerstone to the **Leather/Tailoring** loop, while Agriculture sustains **Cooking/Alchemy**.

---

## 5) Checklist — ship a new trainable skill (implementation)

1. Confirm **`SkillId` exists** (add enum entry only if missing — order with pillar group).
2. Add **`YourSkillData.kt`** in `core/src/main/kotlin/com/arteria/game/core/data/`: `items`, `actions`, `actionRegistry` (follow `MiningData` / `CookingData` patterns).
3. Update **`SkillDataRegistry`**: `putAll` in `actionRegistry` + `itemRegistry`; extend **`actionsForSkill`** `when`.
4. **`GameRepository`** load path: ensure new skill gets default `SkillState` if not present (usually “merge all enum keys” already does this — verify when adding **new** enum values).
5. **`./gradlew :core:test`** if you add tick-sensitive math; **`./gradlew :app:testDebugUnitTest`** if persistence or VM behavior changes.
6. **Player-facing:** if the release is user-visible, follow `CLAUDE.md` / `SCRATCHPAD`: `ChangelogScreen` `APP_CHANGELOG`, `versionCode` / `versionName`, README badge.

---

## 5) Recent product expansion context (handoff)

**`[AMENDED 2026-04-01]:`** v1.5.0 shipped: achievements, random events, equipment system (4 slots, 19 items), companions/familiars (11 companions, 5 rarities), prestige/ascension (6 perks), bank search/sort/withdraw, haptics. Game modes added to account creation (Standard, Ironclad 🔒, Void-Touched 🔒). Equipment and Companion systems create new economy edges — skills should now consider gear inputs and companion bonuses. Random events provide a hook for skill-specific event triggers.

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

---

## 8) Weapon & Tool Mastery — Sub-Progression System

**`[ADDED 2026-04-01]:`** A parallel XP layer that ticks **alongside** main skills. Using a specific weapon or tool type repeatedly levels up your **mastery** for that category, granting passive bonuses. This is NOT a new `SkillId` per weapon — it's a separate, lightweight progression.

### 8.1 Design Philosophy

The main skill system answers **"what can you do?"** — Mastery answers **"how good are you with this specific thing?"**

- **Attack** determines melee accuracy globally. **Sword Mastery** gives a bonus *on top* when wielding swords specifically.
- **Mining** determines what ores you can mine. **Pickaxe Mastery** makes you mine them faster.
- This creates a "depth over breadth" choice: a player who always mines with the same pickaxe tier becomes a specialist.

### 8.2 Weapon Categories (Combat)

| Category | Weapon Examples | Primary Skill Fed |
|----------|-----------------|-------------------|
| `SWORD` | Longsword, Shortsword, Scimitar, Rapier | Attack |
| `DAGGER` | Bronze Dagger, Rune Dagger, Poisoned Stiletto | Attack (fast/crit) |
| `AXE` | Battle Axe, War Axe, Great Axe | Strength |
| `MACE` | Mace, Warhammer, Flail | Strength (armor crush) |
| `SPEAR` | Spear, Halberd, Javelin | Attack + Defence |
| `BOW` | Shortbow, Longbow, Composite Bow | Ranged |
| `CROSSBOW` | Light Crossbow, Heavy Crossbow, Repeater | Ranged |
| `STAFF` | Mystic Staff, Elemental Rod, Wand | Sorcery |
| `UNARMED` | Fists, Knuckles, Chi Wraps | Martial Arts |
| `SHIELD` | Buckler, Kite Shield, Tower Shield | Defence |
| `THROWN` | Throwing Knife, Throwing Axe, Dart | Ranged (short range) |

### 8.3 Tool Categories (Skilling)

| Category | Tool Examples | Primary Skill Fed |
|----------|---------------|-------------------|
| `PICKAXE` | Bronze Pickaxe … Rune Pickaxe … Dragon Pickaxe | Mining |
| `HATCHET` | Bronze Hatchet … Adamant Hatchet … Crystal Hatchet | Logging |
| `FISHING_ROD` | Basic Rod, Fly Rod, Harpoon, Net | Fishing |
| `HAMMER` | Smithing Hammer, Forging Hammer | Smithing / Forging |
| `NEEDLE` | Bone Needle, Steel Needle | Tailoring |
| `CHISEL` | Gem Chisel, Stone Chisel | Jewelcrafting / Construction |
| `TINDERBOX` | Tinderbox, Flint & Steel, Ignition Kit | Firemaking |
| `SICKLE` | Herb Sickle, Golden Sickle | Harvesting / Farming |

### 8.4 Mastery Mechanics

```
MasteryCategory  (enum)   — SWORD, DAGGER, AXE, PICKAXE, HATCHET, etc.
MasteryState     (data)   — category, xp: Double, unlockedPerks: Set<String>
```

**XP ticking:** Every time `TickEngine` completes a `SkillAction`, if the player has a tool/weapon equipped, a **fraction** of the main skill XP also goes to the mastery category. Suggested ratio: **~20% of base XP**.

**Mastery levels:** Same `XPTable` curve but scaled (mastery 1–50, not 1–99). Milestones at:

| Mastery Level | Passive Bonus |
|---------------|---------------|
| 5 | +2% action speed with this type |
| 10 | +5% action speed, minor resource bonus |
| 15 | Unlock "Specialization" perk slot |
| 25 | +10% speed, chance at double resource |
| 35 | Unlock "Expertise" perk slot |
| 50 (cap) | Title + cosmetic + permanent 15% speed bonus |

### 8.5 Backend Model (`:core` proposal)

```kotlin
// core/.../skill/MasteryCategory.kt
enum class MasteryCategory(val displayName: String) {
    // Weapons
    SWORD("Sword"), DAGGER("Dagger"), AXE("Axe"),
    MACE("Mace"), SPEAR("Spear"), BOW("Bow"),
    CROSSBOW("Crossbow"), STAFF("Staff"),
    UNARMED("Unarmed"), SHIELD("Shield"), THROWN("Thrown"),
    // Tools
    PICKAXE("Pickaxe"), HATCHET("Hatchet"),
    FISHING_ROD("Fishing Rod"), HAMMER("Hammer"),
    NEEDLE("Needle"), CHISEL("Chisel"),
    TINDERBOX("Tinderbox"), SICKLE("Sickle"),
}

// core/.../model/GameModels.kt  (extend GameState)
data class MasteryState(
    val category: MasteryCategory,
    val xp: Double = 0.0,
)

// In GameState — add:
//   val masteries: Map<MasteryCategory, MasteryState>

// SkillAction — add optional field:
//   val masteryCategory: MasteryCategory? = null
```

**TickEngine change (minimal):** After awarding main skill XP, if `action.masteryCategory != null`, award `xpPerAction * 0.20` to that mastery. The mastery XP table can reuse `XPTable` with a different max level cap.

**Persistence:** New Room entity `mastery_states` table (category string PK, xp double). Merged into `GameState` on load, same pattern as `skill_states`.

### 8.6 Combat Skill Addition: Martial Arts

Added `MARTIAL_ARTS` to the Combat pillar in `SkillId.kt`:

- **Fantasy:** Unarmed combat, chi-powered strikes, discipline-based progression
- **Why separate from Attack/Strength:** Unarmed doesn't use weapon mastery — it IS the mastery. No equipment dependency makes it a "pure skill" alternative
- **Economy:** Requires no crafted gear to start (low barrier), but Tailoring can make Chi Wraps / Gi armor sets that boost it
- **Mastery link:** Feeds `UNARMED` mastery category exclusively

### 8.7 Implementation Priority

The mastery system is **Phase 7+** — it layers on TOP of the existing skill loop and should NOT block any skill data additions. The design is documented here so that when `SkillAction` data files are written, they can optionally tag `masteryCategory` even before the mastery tracking code exists.

**Step 0 (now):** Add `masteryCategory` field to `SkillAction` as `null` default — zero runtime impact.  
**Step 1:** Add `MasteryCategory` enum to `:core`.  
**Step 2:** Add `MasteryState` model + Room entity.  
**Step 3:** `TickEngine` awards mastery XP alongside skill XP.  
**Step 4:** UI — mastery level display on `SkillDetailScreen` tool/weapon section.

---

## 9) New Skill Recommendations — Post-v1.5.0 Economy Analysis

**`[ADDED 2026-04-01]:`** After shipping Equipment, Companions, and Prestige systems, the economy has new sinks and sources. Here are skills that close the loops.

### 9.1 Critical Economy Gaps (Post-v1.5.0)

**The problem:** Players can mine, log, fish, harvest, scavenge, smelt, cook, and brew — but most outputs just sit in the bank. Equipment exists but has no crafting path. Companions exist but no recruitment mechanic. Prestige exists but no gear to keep.

**What's missing:**
1. **Gem processing** — Mining produces ores but gems are untapped
2. **Equipment crafting** — Bars exist, gear exists, no bridge between them
3. **Companion recruitment** — Companions exist as a system but no way to earn them
4. **Gear dismantling** — Equipment can be equipped but not broken down for materials

### 9.2 Recommended New Skills

#### 9.2.1 Gemcutting (Crafting Pillar) — HIGH PRIORITY
- **Fantasy:** Cut raw gems into faceted stones for jewelry, enchantments, and trade
- **Inputs:** Raw gems from Mining (add `raw_sapphire`, `raw_ruby`, `raw_emerald`, `raw_diamond`, `raw_dragonstone` to MiningData)
- **Outputs:** `cut_sapphire`, `cut_ruby`, `cut_emerald`, `cut_diamond`, `cut_dragonstone`
- **Why now:** Mining already has gem-tier ore progression. Gemcutting creates a parallel track that feeds Jewelcrafting and Enchanting. Pure `inputItems` → `resourceId` flow — zero new mechanics.
- **File count:** 1 new `GemcuttingData.kt` (~60 lines)
- **Synergy:** Feeds Jewelcrafting → Equipment accessories → Prestige perks

#### 9.2.2 Forging (Crafting Pillar) — HIGH PRIORITY
- **Fantasy:** Hammer bars into weapons, armor, and tools
- **Inputs:** Bars from Smithing (bronze_bar, iron_bar, steel_bar, etc.)
- **Outputs:** Equipment items (`bronze_sword`, `iron_pickaxe`, `steel_armor`, etc.) — these are the same IDs used by `EquipmentRegistry`
- **Why now:** Equipment system is live with 19 items but no way to craft them. Forging closes the biggest economy gap: ore → bar → gear. This is the single highest-impact skill addition.
- **File count:** 1 new `ForgingData.kt` (~120 lines for 8 tiers × 3 item types)
- **Synergy:** Smithing → Forging → Equipment → Combat. Full production chain.

#### 9.2.3 Salvaging (Gathering Pillar) — MEDIUM PRIORITY
- **Fantasy:** Dismantle broken gear, ruins, and artifacts for rare components
- **Inputs:** None (pure gathering from world nodes)
- **Outputs:** `scrap_metal`, `enchanted_dust`, `ancient_rune_fragment`, `void_shard`, `spirit_essence`
- **Why now:** Scavenging covers surface oddments. Salvaging is the "deep" variant — dismantling high-value targets. Feeds Forging with rare materials and Enchanting with magical components.
- **File count:** 1 new `SalvagingData.kt` (~80 lines)
- **Note:** This is a rename/split of the existing `SCAVENGING` enum entry's scope. Keep Scavenging as "surface foraging," add Salvaging as "deep dismantling."

#### 9.2.4 Jewelcrafting (Crafting Pillar) — MEDIUM PRIORITY
- **Fantasy:** Combine cut gems with bars to create magical rings, amulets, and equipment accessories
- **Inputs:** Cut gems (Gemcutting) + bars (Smithing)
- **Outputs:** `sapphire_ring`, `ruby_amulet`, `diamond_bracelet`, `dragonstone_pendant` — these map to `EquipmentRegistry` accessory items
- **Why now:** Equipment has an ACCESSORY slot with items like `amulet_of_accuracy` and `ring_of_fortune`. Jewelcrafting is the crafting path for these.
- **File count:** 1 new `JewelcraftingData.kt` (~80 lines)
- **Synergy:** Mining → Gemcutting → Jewelcrafting → Equipment. Full gem pipeline.

#### 9.2.5 Firemaking (Crafting Pillar) — LOW PRIORITY (but easy)
- **Fantasy:** Burn logs for light, heat, and utility buffs
- **Inputs:** Logs from Logging (normal_logs, oak_logs, willow_logs, etc.)
- **Outputs:** `ashes`, `warmth_buff` (temporary), `cooked_food_bonus` (when near fire)
- **Why it's easy:** Pure `inputItems` consumption. No new outputs needed beyond ashes (feeds Alchemy).
- **File count:** 1 new `FiremakingData.kt` (~50 lines)
- **Synergy:** Logging → Firemaking → Alchemy (ashes). "Flame & Feast" synergy with Cooking.

### 9.3 Skill Split Reflections

**Firemaking → Firemaking + Bonfire Craft:** The split makes sense at scale but is premature. Ship base Firemaking first (logs → ashes/buffs), then add Bonfire Craft as a level-50+ sub-skill that combines multiple log types for area buffs. Don't split before the base exists.

**Runecrafting → Essence Mining + Runecrafting:** Essence Mining as a Gathering skill that feeds Runecrafting (Crafting) is a clean split. Essence Mining produces `pure_essence`, `chaotic_essence`, `cosmic_essence`. Runecrafting turns them into `air_rune`, `water_rune`, `fire_rune`, `chaos_rune`, etc. These runes feed Sorcery (Combat) and Enchanting (Crafting). This is Phase 2+, after Forging closes the equipment gap.

**Farming → Agriculture + Husbandry:** The V1 docs explicitly call this split. Agriculture = plant/harvest crops (fast cycle, food/herb output). Husbandry = raise animals (slow cycle, hide/wool/meat output). Both are valid, but Husbandry requires a timer/growth system that doesn't exist yet. Ship Agriculture first (instant-node harvesting like current skills), add Husbandry when growth timers are implemented.

### 9.4 Implementation Priority Order

| Priority | Skill | Lines of Code | New Mechanics | Economy Impact |
|----------|-------|---------------|---------------|----------------|
| **1** | Forging | ~120 | None (pure inputItems) | **Critical** — closes equipment gap |
| **2** | Gemcutting | ~60 | None | **High** — unlocks gem pipeline |
| **3** | Firemaking | ~50 | None | **Medium** — log sink, ash source |
| **4** | Jewelcrafting | ~80 | None | **High** — accessory crafting |
| **5** | Salvaging | ~80 | None | **Medium** — rare material source |
| **6** | Fletching | ~80 | None | **Medium** — ranged ammo pipeline |
| **7** | Agriculture | ~70 | Growth timers (deferred) | **Medium** — crop pipeline |
| **8** | Essence Mining | ~60 | None | **Low** — rune pipeline prep |
| **9** | Runecrafting | ~90 | None | **Low** — Sorcery dependency |

**Total for Phase 1 (Forging + Gemcutting + Firemaking): ~230 lines across 3 files.** All pure `inputItems` → `resourceId` — zero new TickEngine mechanics.

### 9.5 Checklist Status Audit

Cross-referencing `claudes_checklist_by_ryan.md` against current state:

| Section | Status | Notes |
|---------|--------|-------|
| 6. Bank Screen Upgrade | **6d ✅** (search/filter shipped) | 6a (categories), 6b (icons), 6c (craftable badge), 6e (detail sheet) remain |
| 7. Skill Detail Polish | **7a ✅** (locked actions shown) | 7b (XP/hr), 7c (best action badge), 7d (input availability), 7e (lore blurb) remain |
| 8. Combat Screen | ❌ Not started | All 5 items open |
| 9. Playtime Tracking | ❌ Not started | All 5 items open |
| 10. Visual Juice | ❌ Not started | All 5 items open |
| 11. Cosmetics | ❌ Not started | All 5 items open |
| 12. Daily Challenges | ❌ Not started | All 5 items open |
| 13. Prestige | **13a ✅** (model + UI shipped) | 13b-13e (badge, milestones, confirm dialog, catch-up) remain |
| 14. Mastery Trees | ❌ Not started | All 5 items open (Section 8 of this doc has the design) |
| 15. Companion Pets | **15a ✅** (model + UI shipped) | 15b-15e (recruitment, passive bonuses, pet widget, cosmetics) remain |

**Shipped: 4/75 items. Open: 71/75.** The biggest wins for effort are 7b (XP/hr — 1 line of math), 7d (input availability — bank lookup), and 6c (craftable badge — registry scan).
