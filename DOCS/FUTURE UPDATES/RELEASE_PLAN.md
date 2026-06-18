<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

# Release plan — 1.10.x QoL spine

Living carve path for near-term player-visible slices. **Canonical path:** `DOCS/FUTURE UPDATES/RELEASE_PLAN.md` (this file). Hub: **`DOCS/FUTURE UPDATES/README.md`**. **App version truth:** `app/build.gradle.kts` + first `APP_CHANGELOG` card.

---

## Shipped: 1.10.3 — Settings & bank shell

- Settings sectioned (Account / Journey / Experience / Simulation / About)
- Bank category grouping (Grouped/Flat)
- Skill XP/hr + Best XP/hr badge

---

## Shipped: 1.10.4 — Discovery layer

**Theme:** Help players understand *what items do* and *what combat/offline gave them*.

| Item | Status |
|------|--------|
| `ItemUsageIndex` — produced-by / used-in reverse index | ✅ |
| Bank **Craft now** badge on affordable recipe inputs | ✅ |
| Bank item **detail sheet** (description, produced by, used in) | ✅ |
| Skill **lore blurb** from `SkillId.description` | ✅ |
| Combat **XP split** line on kill + readable loot names | ✅ |
| Combat **Recent drops** strip | ✅ |
| Offline report **~XP/hr** efficiency line | ✅ |

---

## Next: 1.10.5 — Combat & session clarity (proposed)

**Theme:** Make fighting and returning feel legible without new systems.

| Priority | Item | Source |
|----------|------|--------|
| P0 | Combat **attack tempo** indicator (player/enemy swing interval progress) | top-100 #9 |
| P0 | Hub **session timer** (“this session: 14m”) — lightweight, no Room yet | checklist §9b |
| P1 | Combat **equipment strip** snapshot on encounter panel | top-100 #10 |
| P1 | Bank **category icons** / emoji placeholders (§6b) | checklist |
| P1 | Skill detail **locked actions** always visible with greyed train | checklist §7a |
| P2 | Hub offline card **efficiency** mirror (same math as dialog) | checklist §9d |
| P2 | Prestige multipliers wired into `TickEngine` | top-100 #5 |

**Out of scope for 1.10.5:** new skills, Room schema for playtime, notification infra.

---

## Horizon: 1.11.0 — Economy smoke (proposed)

- Production-chain QA UX (mine → gem → jewel → equip) guided nudge on Hub
- Summoning pouch smoke fixes + scroll polish
- Second encounter zone (one new enemy/location)
- `Companion` unlock persistence in Room (top-100 #6)

---

## Backlog index

Full 100-item brainstorm: [`top-100-next-todo.md`](top-100-next-todo.md) (this folder).  
Agent synthesis checklist: [`claudes_checklist_by_ryan.md`](claudes_checklist_by_ryan.md).  
Folder hub: [`README.md`](README.md).
