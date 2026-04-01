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
- [x] **P2 — Appearance:** Theme **Dark / Follow system** + light shell (`ArteriaTheme`, space brush, Docking). Named themes (Sepia/Midnight) still open.
- [x] **P3 — Audio:** Master sound toggle + test sound; per-SFX/music split still open.
- [x] **P4 — Accessibility:** Reduce motion (system animator scale + app toggle) + haptics master; full TalkBack audit still open.
- [x] **P5 — Gameplay info:** Offline cap copy + tick/save cadence; monetization-aligned cap doc still open.
- [ ] **P6 — Economy links:** Stub row “planned”; real Lumina/login bonus when systems exist.
- [x] **P7 — Danger zone:** Reset progress + delete profile (confirmed); export/import not shipped.
- [ ] **P8 — Notifications:** Disabled stub row; real channels when infra exists.

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

- [x] **Must** — Theme mode: **Follow system** + **Dark** (light shell when system light); Sepia/Midnight still open
- [x] **Should** — Semantic palette only in UI (`ArteriaPalette` / `ArteriaContentColors`)
- [ ] **Should** — Status bar / system nav contrast follows chosen theme (edge-to-edge follow-up)
- [ ] **Could** — Font scale / display size hook (or defer to system-only)
- [ ] **Could** — High-contrast or colorblind-friendly palette variant

### 4.3 Gameplay & simulation

- [x] **Should** — Offline progression cap explained (uses `TickEngine.DEFAULT_MAX_OFFLINE_MS` / 24h in UI copy)
- [x] **Should** — WYWA / offline report: show vs suppress (DataStore `showOfflineReport`)
- [ ] **Could** — Explicit “collect & continue” vs auto-apply offline gains (V1 RN emphasized explicit apply — decide V2 policy)
- [x] **Could** — Tick / save cadence transparency (Settings shows interval seconds from `GameViewModel` constants)
- [ ] **Could** — Combat auto/offline AI preferences (when combat engine ships)
- [ ] **Could** — Offline queue UX toggles (when queue system ships per `IMPROVEMENTS.md` / roadmap)

### 4.4 Audio

*Sources: `TECHNICAL_USER_MANUAL.md`, `IMPROVEMENTS.md`, `CURRENT_IMPROVEMENTS.md`*

- [x] **Should** — Master volume or global mute (boolean **Sound** toggle)
- [ ] **Should** — Split: SFX vs music (when music exists)
- [ ] **Could** — Per-category: tick SFX, UI SFX, ambient
- [x] **Could** — Idle soundscapes toggle + honest “Coming soon” sublabel if loops absent
- [x] **Should** — Test sound button
- [ ] **Could** — Per-skill / per-soundscape sliders when asset IDs exist

### 4.5 Haptics & feedback

*Sources: `IMPROVEMENTS.md`, `DOCS/ROADMAP.md` Phase 7*

- [x] **Should** — Global haptics on/off (level-up haptic wired)
- [ ] **Could** — Per-event class: level-up, rare drop, bank sell, errors (document mapping table in code or doc appendix)
- [ ] **Could** — Intensity / “light tap only”

### 4.6 Motion & performance

*Sources: `DOCS/ROADMAP.md` Phase 7*

- [x] **Should** — Reduce motion: `ANIMATOR_DURATION_SCALE` + in-app toggle; Docking/Glitch respect
- [ ] **Could** — Docking / glitch “lite” mode or intensity slider (beyond on/off)
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

- [x] **Should** — Delete local data / reset profile (with confirmation + irreversibility copy)
- [ ] **Could** — Export save
- [ ] **Could** — Import save
- [ ] **Could** — Clear cache / resync
- [ ] **Could** — Analytics off / crash reporting off (if SDKs added)
- [x] **Should** — Open-source licenses / third-party notices (`OpenSourceNoticesScreen` + `ARTERIA_OSS_NOTICES`)

### 4.12 About & meta

- [x] **Must** — App version / build string (`BuildConfig.VERSION_NAME` / `VERSION_CODE`, `buildConfig = true`)
- [x] **Should** — What’s New / changelog entry point
- [x] **Could** — Credits / attribution (`CreditsScreen`)
- [ ] **Could** — External links: docs, roadmap, support
- [ ] **Could** — Easter egg entry (discoverability policy TBD)

### 4.13 Developer / advanced

- [x] **Could** — Debug-only panel: **Remove offline time cap** (`BuildConfig.DEBUG` + `debugRemoveOfflineCap`)

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
| 2026-04-01 | **Backlog wave:** DataStore prefs, theme/motion/haptics/sound/offline-report, OSS+Credits, reset/delete profile, DEBUG offline cap, `TickEngine.DEFAULT_MAX_OFFLINE_MS`, `MainActivity` locals. |
| 2026-04-01 | Shipped parity slice: profile rename (`Room` + `AccountViewModel`), last played, About/version, tick/save cadence copy, test sound; wired via `AccountSessionInfo` / `GameScreen` / `ArteriaApp`. |
| — | Initial master design + checklist (converted from mega-list) |

_Add new rows at the **top** of the table; do not delete prior rows._
