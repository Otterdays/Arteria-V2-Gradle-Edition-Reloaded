# Arteria V2 — Settings System (Master Design & Checklist)

**Document type:** Master design + implementation checklist  
**Scope:** Native Android (Compose) settings surface; parity with Arteria V1 (`DOCS/ARTERIA-V1-DOCS/DOCU/`) and `DOCS/ROADMAP.md` Phase 7 QoL.  
**Status:** In progress — checkboxes track delivery (updated 2026-04-01).  
**Related code:** `SettingsScreen.kt`, `GameViewModel.kt`, persistence via profile/game DB as applicable.

---

## 1. Design intent

- **Single hub:** One overlay or full-screen settings entry from game shell; sub-screens only when a category needs depth (e.g. Danger zone confirmations).
- **KISS:** Ship toggles and links before sliders and per-skill audio matrices.
- **Fail safe:** Destructive actions live under **Danger** with confirm dialogs; defaults favor accessibility (respect system reduce-motion where possible).
- **Trace:** Cross-link implementation commits to this doc section IDs (e.g. §3 Appearance).

---

## 2. V1 parity snapshot

Old app (`IMPROVEMENTS.md`) grouped Settings as:

| V1 section        | Notes |
|-------------------|--------|
| Character         | Nickname |
| Appearance        | Theme |
| Gameplay          | Rules / sim UX |
| Audio             | SFX, soundscapes stub, test sound |
| Notifications     | Channel toggles when infra exists |
| Login bonus & Lumina | Economy entry |
| Premium           | Patron / shop framing |
| About             | Version, credits |
| Easter Egg        | Optional discoverability |
| Danger            | Reset / destructive |
| Mastery           | **Not** in Settings in V1 — opened from Skills header |

**V2 decision (record when chosen):** [ ] Mastery stays Skills-only · [ ] Mastery also linked from Settings

---

## 3. Implementation phases (rollout checklist)

Use this to sequence work; mark complete when shipped and tested.

- [x] **P0 — Shell:** Settings entry, back navigation, section list layout (matches `ArteriaPalette` / typography rules).
- [x] **P1 — About + Account:** Version/build, profile card, switch account (existing), nickname edit if in scope.
- [ ] **P2 — Appearance:** Theme mode (min: follow system + app dark); extend to named themes when tokens exist.
- [ ] **P3 — Audio:** Master mute + SFX toggle + test sound (no engine dependency beyond stub OK) — **test sound only** shipped 2026-04-01.
- [ ] **P4 — Accessibility:** Reduce motion + haptics master toggle + TalkBack labels on new controls.
- [ ] **P5 — Gameplay info:** Read-only offline cap / save cadence copy (no false promises) — **tick/save cadence** line shipped 2026-04-01; offline cap copy still open.
- [ ] **P6 — Economy links:** Lumina/login bonus entry points when those systems exist in V2.
- [ ] **P7 — Danger zone:** Delete local / reset with confirmation + optional export/import placeholders.
- [ ] **P8 — Notifications:** Per-channel toggles once push/local scheduling exists.

---

## 4. Category checklists

Legend: **Must** = P0–P2 core UX · **Should** = next slice · **Could** = backlog / future.

### 4.1 Account & profile

- [x] **Must** — Display name / nickname (edit + validation rules)
- [x] **Must** — Active profile indicator; last-played timestamp (read-only if available)
- [x] **Must** — Switch account
- [ ] **Could** — Avatar / portrait slot (placeholder UI until assets ship)
- [ ] **Could** — Sign-out / cloud account semantics (when auth exists)

### 4.2 Appearance & theme

*Sources: `THEMING.md`, `TECHNICAL_USER_MANUAL.md`*

- [ ] **Must** — Theme mode: System / Dark (minimum); extend to Light / Sepia / Midnight when palette tokens defined
- [ ] **Should** — Semantic palette only in UI (no ad-hoc hex in settings screens)
- [ ] **Should** — Status bar / system nav contrast follows chosen theme
- [ ] **Could** — Font scale / display size hook (or defer to system-only)
- [ ] **Could** — High-contrast or colorblind-friendly palette variant

### 4.3 Gameplay & simulation

- [ ] **Should** — Offline progression cap explained (informational; align with `TRUTH_DOCTRINE` / monetization doc when written)
- [ ] **Should** — WYWA / offline report: auto-show vs suppressed / summary-only (match product decision)
- [ ] **Could** — Explicit “collect & continue” vs auto-apply offline gains (V1 RN emphasized explicit apply — decide V2 policy)
- [x] **Could** — Tick / save cadence transparency (Settings shows interval seconds from `GameViewModel` constants)
- [ ] **Could** — Combat auto/offline AI preferences (when combat engine ships)
- [ ] **Could** — Offline queue UX toggles (when queue system ships per `IMPROVEMENTS.md` / roadmap)

### 4.4 Audio

*Sources: `TECHNICAL_USER_MANUAL.md`, `IMPROVEMENTS.md`, `CURRENT_IMPROVEMENTS.md`*

- [ ] **Should** — Master volume or global mute
- [ ] **Should** — Split: SFX vs music (when music exists)
- [ ] **Could** — Per-category: tick SFX, UI SFX, ambient
- [ ] **Could** — Idle soundscapes toggle + honest “Coming soon” sublabel if loops absent
- [x] **Should** — Test sound button
- [ ] **Could** — Per-skill / per-soundscape sliders when asset IDs exist

### 4.5 Haptics & feedback

*Sources: `IMPROVEMENTS.md`, `DOCS/ROADMAP.md` Phase 7*

- [ ] **Should** — Global haptics on/off
- [ ] **Could** — Per-event class: level-up, rare drop, bank sell, errors (document mapping table in code or doc appendix)
- [ ] **Could** — Intensity / “light tap only”

### 4.6 Motion & performance

*Sources: `DOCS/ROADMAP.md` Phase 7*

- [ ] **Should** — Reduce motion: respect `android.settings` / Compose accessibility + optional in-app override
- [ ] **Could** — Docking / glitch “lite” mode or intensity slider
- [ ] **Could** — Starfield / nebula / particle density (when parameters exist)

### 4.7 Notifications

*Sources: `IMPROVEMENTS.md`*

- [ ] **Could** — Daily reset / quest reminder
- [ ] **Could** — Login bonus ready
- [ ] **Could** — Idle cap or offline report ready (needs channel infra)
- [ ] **Could** — Patch notes / update board style alert

### 4.8 Economy: Lumina, shop, premium

*Sources: `IMPROVEMENTS.md`, `MASTER_DESIGN_DOC.md`, `TRUTH_DOCTRINE.md`*

- [ ] **Could** — Lumina balance + deep link to shop (when economy exists in V2)
- [ ] **Could** — Login bonus & streak entry
- [ ] **Could** — Premium / patron copy: bank slots, cosmetics, **non-pay-to-win** disclosure
- [ ] **Could** — Restore purchases (Play Billing)

### 4.9 Progression & deep links

- [ ] **Could** — Mastery entry (Skills-only vs also Settings — see §2)
- [ ] **Could** — Stats summary or nav to stats surface
- [ ] **Could** — Daily quests + reroll policy copy (Lumina reroll parity)
- [ ] **Could** — Synergy Journal entry (`SYNERGIES.md`)

### 4.10 World & content discovery

- [ ] **Could** — Chronicle / achievements link (roadmap Phase 9)
- [ ] **Could** — Bestiary link
- [ ] **Could** — Explore / world map link

### 4.11 Data, privacy, danger zone

- [ ] **Should** — Delete local data / reset profile (with confirmation + irreversibility copy)
- [ ] **Could** — Export save
- [ ] **Could** — Import save
- [ ] **Could** — Clear cache / resync
- [ ] **Could** — Analytics off / crash reporting off (if SDKs added)
- [ ] **Should** — Open-source licenses / third-party notices (often under About)

### 4.12 About & meta

- [x] **Must** — App version / build string (`BuildConfig.VERSION_NAME` / `VERSION_CODE`, `buildConfig = true`)
- [ ] **Should** — What’s New / changelog entry point
- [ ] **Could** — Credits / attribution
- [ ] **Could** — External links: docs, roadmap, support
- [ ] **Could** — Easter egg entry (discoverability policy TBD)

### 4.13 Developer / advanced

- [ ] **Could** — Debug-only panel: FPS, tick debug, offline sim helpers (guard with `BuildConfig.DEBUG` or equivalent)

### 4.14 Accessibility & localization

- [ ] **Should** — `contentDescription` / semantics on all new settings controls
- [ ] **Could** — In-app language override (if i18n ships)
- [ ] **Could** — Text scaling overrides beyond system

### 4.15 Experimental / high-risk gameplay options

- [ ] **Could** — “Honest mode” / offline cap opt-in (`MASTER_DESIGN_DOC` concept) — requires legal/product review + strong UX warning

---

## 5. Definition of done (per item)

When checking a box above, confirm:

- [ ] Persisted where needed (DataStore / Room / meta) with migration plan if schema changes
- [ ] Applied at runtime (no “toggle that does nothing”)
- [ ] Documented in `DOCS/SCRATCHPAD.md` or changelog if user-visible behavior changes
- [ ] No hardcoded hex — `ArteriaPalette` / theme tokens only in UI

---

## 6. Source index

| Source | Use |
|--------|-----|
| `DOCS/ARTERIA-V1-DOCS/DOCU/IMPROVEMENTS.md` | V1 Settings inventory, audio, soundscapes |
| `DOCS/ARTERIA-V1-DOCS/DOCU/THEMING.md` | Theme IDs, persistence ideas |
| `DOCS/ARTERIA-V1-DOCS/DOCU/TECHNICAL_USER_MANUAL.md` | Theme variants, sound engine, WYWA flow |
| `DOCS/ROADMAP.md` | Phase 7 — a11y, motion, haptics, WYWA parity |
| `DOCS/ARTERIA-V1-DOCS/DOCU/SYNERGIES.md` | Settings → Synergy Journal |
| `DOCS/ARTERIA-V1-DOCS/DOCU/MASTER_DESIGN_DOC.md` | Lumina, tone, optional “honest mode” |
| `DOCS/ARTERIA-V1-DOCS/DOCU/TRUTH_DOCTRINE.md` | Monetization / offline framing |

---

## 7. Revision log

| Date | Change |
|------|--------|
| 2026-04-01 | Shipped parity slice: profile rename (`Room` + `AccountViewModel`), last played, About/version, tick/save cadence copy, test sound; wired via `AccountSessionInfo` / `GameScreen` / `ArteriaApp`. |
| — | Initial master design + checklist (converted from mega-list) |

_Add new rows at the **top** of the table; do not delete prior rows._
