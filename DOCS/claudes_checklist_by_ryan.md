listen up buddy, this doc is going to have stuff i put here to help synthesis into workable tasks for the most advanced AI Agents to work on, like claude opus, or "Opie" as i call him.
===========================DO NOT DELETE ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^========================================================================

---

## ✅ DONE — Sections 1–5 (30/30)

All 30 items shipped. Details in SCRATCHPAD.md Agent Credits.

| # | Section | Status |
|---|---------|--------|
| 1 | Hub Screen (Command Center) | 8/8 ✅ |
| 2 | Alive Bottom Bar | 7/7 ✅ |
| 3 | Skill Navigation Redesign | 7/7 ✅ |
| 4 | Settings Out of Nav Bar | 5/5 ✅ |
| 5 | "Just One More" Nudge System | 7/7 ✅ |

---

## 6. Bank Screen Upgrade

- [ ] **6a.** Group items by category (Ores, Bars, Logs, Fish, Herbs, Potions, Salvage) with collapsible section headers
- [ ] **6b.** Item cards show quantity, item name, and a subtle icon placeholder — styled to match `ArteriaPalette` card system
- [ ] **6c.** "Craftable now" badge — highlight items that are inputs to an affordable recipe the player can start immediately
- [ ] **6d.** Search / filter bar — filter by item name or category
- [ ] **6e.** Tap an item → mini detail sheet: what it's used for, which skill produces it, current quantity

---

## 7. Skill Detail Screen Polish

- [ ] **7a.** Show locked actions greyed out with level requirement visible — player can see the full action list from day one
- [ ] **7b.** XP/hr estimate label on each action card — calculated from `xpPerAction / actionTimeMs * 3600000`
- [ ] **7c.** "Best action" highlight — auto-badge the highest-XP-per-hour unlocked action
- [ ] **7d.** Input item availability indicator on crafting actions — green/red tint based on whether bank has enough materials
- [ ] **7e.** Skill lore blurb — short flavour text pulled from `SkillId.description`, shown below the hero header

---

## 8. Combat Screen — First Pass

- [ ] **8a.** Basic combat loop UI — Attack / Strength / Defence / Hitpoints live stat bars
- [ ] **8b.** Enemy card — name, HP bar, combat level, loot preview
- [ ] **8c.** Auto-attack toggle with active animation (sword pulse on each hit cycle)
- [ ] **8d.** Combat XP split display — shows XP awarded to each combat skill per kill
- [ ] **8e.** Loot drop feed — scrolling mini-cards of drops that go to bank automatically

---

## 9. Playtime & Session Tracking

- [ ] **9a.** Track total playtime in `GameState` (increment per tick, persist to Room)
- [ ] **9b.** Session start time — show "this session: 14m" on the Hub quick-stats row
- [ ] **9c.** Playtime stat visible in Settings (total time played, avg session length)
- [ ] **9d.** Offline efficiency score — "you earned X XP/hr while away" shown in the offline gains card
- [ ] **9e.** Lifetime stats screen (stretch) — total XP earned, items banked, actions completed across all time

---

## 10. Visual Juice & Polish

- [ ] **10a.** Level-up flash — brief full-screen accent colour pulse + Cinzel "LEVEL UP" text when a skill levels, replacing or enhancing the snackbar
- [ ] **10b.** Action completion micro-animation — XP number floats up from the active training card on each action tick
- [ ] **10c.** Bank item gain animation — item quantity briefly pulses gold when new items land
- [ ] **10d.** Star map node pulse — training skill node in `SkillStarMap` glows and breathes while active
- [ ] **10e.** Sound design hooks — `SoundEngine.kt` stub with `playActionComplete()`, `playLevelUp()`, `playItemGained()` wired to the existing "Test sound" in settings

---

## 11. Cosmetics & Customization

- [ ] **11a.** Cosmetic cosmetics currency — `CosmeticTokens` earned from level-ups, purchasable in a cosmetics shop UI
- [ ] **11b.** Skill trails — animated particle effects when training a skill (golden sparkles for mining, wavy lines for fishing, etc.)
- [ ] **11c.** Avatar customization — choose player portrait (generated via palette, or sprite select), displayed in top bar + Hub
- [ ] **11d.** Cosmetic cosmetics unlocks — achievement-tied cosmetics (e.g., "reach level 99 Smithing" → exclusive gold pickaxe trail)
- [ ] **11e.** Cosmetics preview system — tap any cosmetic in shop to preview it live on your character before purchase

---

## 12. Daily Challenges & Quests

- [ ] **12a.** `DailyChallenge` model — objective (e.g., "Gather 500 Logs"), reward (100 cosmetic tokens + 2 rare items), per-player lockout until server reset
- [ ] **12b.** Challenge card UI on Hub — shows today's challenge, progress bar, tap to jump to relevant skill or abandon
- [ ] **12c.** Streak counter — visual counter of consecutive-day challenge completions, cosmetic multiplier for streaks
- [ ] **12d.** Challenge reward animations — confetti burst + gold coins flying to bank when challenge completes
- [ ] **12e.** Server-driven challenges — backend can push seasonal challenge lists (e.g., "Spring Fishing Challenge") without app update

---

## 13. Prestige / Ascension System

- [ ] **13a.** Ascension threshold — at level 99 (or 1000 total XP), player can "Ascend" to reset all skills to level 0 but unlock a prestige currency multiplier (+10% XP/resource gains, permanent)
- [ ] **13b.** Prestige cosmetic badge — small "Ascended ✨" badge on the Hub + skill cards showing ascension count
- [ ] **13c.** Ascension milestone rewards — first ascension unlocks exclusive cosmetics; 5th ascension unlocks a new skill tier or passive ability
- [ ] **13d.** Confirm dialog with warning — "Resetting grants a 1.1x permanent multiplier but erases all XP. Continue?" with 3-second delay button
- [ ] **13e.** Post-ascension catch-up — briefly show a 2.0x XP multiplier for first 24h after ascension to ease the reset pain

---

## 14. Mastery Trees — Passive Talent System

- [ ] **14a.** `MasteryTree` per skill — unlocked passive perks at levels 10 / 25 / 50 / 75 / 99 (e.g., "Mining 25: +5% ore yield")
- [ ] **14b.** Mastery UI — tree grid in each `SkillDetailScreen` showing unlocked perks with icons + tooltips
- [ ] **14c.** Perk types — XP boost, resource multiplier, action speed, unlock secret actions, cosmetic trails
- [ ] **14d.** Mastery point system (stretch) — earn 1 point per level to spend on any skill's tree (let player customize)
- [ ] **14e.** Global mastery bonus row on Hub — show active perk stack (e.g., "+15% XP from 3 active perks") live

---

## 15. Companion Pets & Followers

- [ ] **15a.** `PetCompanion` model — name, species (Dragon / Owl / Fox), level, loyalty stat, equipped cosmetic
- [ ] **15b.** Pet recruitment — rare loot drop or crafting recipe unlocks a pet companion with random traits
- [ ] **15c.** Passive pet bonuses — pet levels alongside you, grants permanent +5% XP per pet level (scales to +500% at level 99)
- [ ] **15d.** Pet UI widget on Hub — small animated pet portrait card showing level, loyalty, and active bonus, tap to pet (loyalty +1, cosmetic animation)
- [ ] **15e.** Pet cosmetics & skins — unlock pet outfit cosmetics (knitted sweater, gold armor, wizard hat) via loot or cosmetic shop, visually shown on pet widget
