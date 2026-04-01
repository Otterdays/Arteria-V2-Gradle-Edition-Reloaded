<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

# STYLE_GUIDE

> [!WARNING]
> **ATTENTION:** Do NOT remove or delete existing texts, updates, docs, or anything else in this document. Only append, compact, or update.

Project-specific conventions for Arteria code and docs. For visual/UI style, see **zhip-ai-styling.md**. For doc index, catalogs, and checklists, see [SUMMARY.md](../../SUMMARY.md) §Documentation Index.

**`[AMENDED 2026-03-31]:`** This file is a **dual-track** guide: **Part A** is the active **Gradle / Kotlin / Jetpack Compose** rules for this repository. **Part B** (§1–§10 below) retains the **legacy React Native / Expo** wording for historical and parity reference.

### Contents

| Part | What it covers |
|------|----------------|
| **A.1** | Trace tags (Kotlin) |
| **A.2** | Code limits |
| **A.3** | Comment prefixes |
| **A.4** | Naming (Kotlin / Android) |
| **A.5** | Architecture and async |
| **A.6** | Touch targets (Compose) |
| **A.7** | Theming (Material 3 + Arteria) |
| **A.8** | Build and identity (Gradle) |
| **A.9** | Error handling |
| **A.10** | Feature UI (game shell) |
| **§1–§10** | Legacy RN/Expo (trace, limits, comments, naming, touch, requirements, theming, build scripts, tokens, workbench UI) |

---

## Part A — V2 Kotlin / Jetpack Compose (this repo)

Use this section for **`Arteria-V2-Gradle-Edition-Reloaded`** (Gradle root with `:app`, `:core`). Prefer **[CLAUDE.md](../../../CLAUDE.md)** at the repo root for the full agent checklist; this section is the compact style summary.

### A.1 Trace tags (Kotlin)

Link implementation to documentation:

```kotlin
// [TRACE: DOCS/SCRATCHPAD.md — startup account/session persistence]
// [TRACE: DOCS/ARCHITECTURE.md — offline tick processing]
```

- Use when behavior is specified in a doc.
- Format: `[TRACE: path — optional context]`
- Paths are relative to the **repository root** (often under `DOCS/`).

### A.2 Code limits

| Rule | Limit |
|------|-------|
| Line length | 100 characters |
| Function length | 50 lines |
| File length | 400 lines |

Split or extract when exceeding; prefer small, focused types and files.

### A.3 Comment prefixes

| Prefix | Meaning |
|--------|---------|
| `TODO:` | Planned work not yet started |
| `FIXME:` | Known bug or temporary hack |
| `NOTE:` | Non-obvious rationale or caveat |

Prefer clear names and types over comments. Comment **why**, not what.

### A.4 Naming (Kotlin / Android)

| Context | Convention | Example |
|---------|------------|---------|
| Classes / types | PascalCase | `GameViewModel` |
| Functions / properties | camelCase | `saveGameState` |
| Packages | lowercase, no underscores | `com.arteria.game.ui.game` |
| Constants | UPPER_SNAKE | `MAX_OFFLINE_MS` |

### A.5 Architecture and async

- **MVVM:** no business logic in `Activity` or Composables; use `ViewModel`s.
- **State:** expose UI state with `StateFlow` (typically `StateFlow` with replay 1); one-shot events with `SharedFlow`.
- **Coroutines:** `viewModelScope` / `lifecycleScope` only — never `GlobalScope`.
- **I/O and tick work:** keep Room and repository calls off the main thread (Room + coroutines); CPU-heavy simulation on `Dispatchers.Default` where already established.

### A.6 Touch targets and interaction (Compose)

- Prefer **full-row** or **full-card** hit areas for settings and skill actions (`Modifier.clickable` / `Button` patterns), not tiny overlays that block touches.
- Avoid stacking non-interactive layers above tappable content unless deliberately forwarding input.

### A.7 Theming (Material 3 + Arteria)

- **Colors:** **`ArteriaPalette` only** in UI code — no hardcoded hex. Semantic tokens (e.g. `BgApp`, `TextPrimary`, `AccentPrimary`).
- **Typography:** **`MaterialTheme.typography`** only — do not set arbitrary `fontFamily` in screens; Cinzel and body fonts are wired in **`ArteriaTheme.kt`**.
- For the full token list, see **CLAUDE.md** → Theme & Design System.

### A.8 Build and identity (Gradle)

- Local builds: `./gradlew :app:assembleDebug` (see **CLAUDE.md** for install and test tasks).
- **Do not** apply Expo `app.config.js` or V1 batch-script flows to this project unless you are explicitly working in the legacy app.

### A.9 Error handling

- Use `try`/`catch` or `runCatching` at coroutine and I/O boundaries.
- Do not swallow exceptions silently — log or map to UI error state in the ViewModel.

### A.10 Feature UI (skills / game shell)

- Follow existing **Compose** screens under `app/.../ui/game/` (e.g. hub tabs, skill detail, bank). For component-level patterns, see **`ui/components/`** (e.g. docking / game chrome) and **CLAUDE.md** → UI Component library.

---

## Part B — Legacy V1 sections (React Native / Expo)

The numbered sections **§1–§10** below are **preserved** for the older TypeScript / React Native codebase. When porting behavior to Kotlin, treat them as **design and UX intent**, and implement using **Part A** rules above.

---

## 1. Trace Tags

Link code to docs with trace tags:

```ts
// [TRACE: DOCU/SCRATCHPAD.md]
// [TRACE: DOCU/ARCHITECTURE.md — Data Flow]
```

- Use when a file implements or depends on documented behavior.
- Format: `[TRACE: path — optional context]`
- Keep paths relative to repo root.

---

## 2. Code Limits

| Rule | Limit |
|------|-------|
| Line length | 100 chars |
| Function length | 50 lines |
| File length | 400 lines |

Split or extract when exceeding. Prefer smaller, focused modules.

---

## 3. Comment Prefixes

| Prefix | Meaning |
|--------|---------|
| `TODO:` | Planned work, not yet done |
| `FIXME:` | Known bug or hack to fix |
| `NOTE:` | Non-obvious rationale or caveat |

Use sparingly. Prefer types and clear naming over comments.

---

## 4. Naming Conventions

| Context | Convention | Example |
|---------|------------|---------|
| JS/TS | camelCase | `handleAltarPress` |
| Python | snake_case | `process_delta` |
| CSS | kebab-case | `section-card` |
| Constants | UPPER_SNAKE or PascalCase | `RUNE_ALTARS` |

---

## 5. Touch Targets (Mobile)

- **Settings rows:** Whole row is pressable (Pressable wraps label + Switch). Switch is display-only; row press toggles.
- **Skill nodes:** Use BouncyButton or TouchableOpacity for full-card hit area.
- Avoid `pointerEvents="none"` on interactive parents; use only when intentionally passing through (e.g. overlay).

---

## 6. Requirements Indicators

For craftable/skill nodes with multiple requirements (level, essence, narrative):

- Show compact badge row: `[Lv. X ✓] [emoji N/batch] [📖 Story]`
- Locked state: muted border, `reqBadgeLocked` style.
- Out-of-resource: red-tinted `reqBadgeEmpty` for essence badge.

---

## 7. Theming

- **Primary:** Use `useTheme().palette` for theme-aware colors. See DOCU/THEMING.md.
- **Phase 4 complete:** `Palette` export removed. Use `useTheme().palette` or `THEMES.dark` for fallback.
- **Tokens:** `bgApp`, `textPrimary`, `accentPrimary`, etc. — semantic names, not hex.
- **ThemeId:** `'system' | 'dark' | 'light' | 'sepia'`. Persisted in `player.settings.themeId`.

**Migration pattern (Phase 3):**
```ts
const { palette } = useTheme();
const styles = useMemo(
  () => StyleSheet.create({
    container: { flex: 1, backgroundColor: palette.bgApp },
    // ... all color tokens from palette
  }),
  [palette]
);
```
- Child components that need themed styles receive `styles` as a prop.
- Layout tokens (`Spacing`, `Radius`, `FontSize`) stay as static imports — only color tokens move to `palette`.
- Override `CardStyle` colors: `...CardStyle, borderColor: palette.border`.

---

## 8. Build Scripts & App Identity

| Script | App Name | When to Use |
|--------|----------|-------------|
| `2_Build_APK_Local.bat` | **Arteria** | Shareable prod APK |
| `1_Run_Local_Android_Build.bat` | **Arteria-dev** | Dev client for Fast Refresh |

Both can coexist on the same device. `app.config.js` reads `ARTERIA_LEAN_PROD`; batch scripts set/unset it. See EXPO_GUIDE §5b.

---

## 9. Theme Tokens

Import from `@/constants/theme`:

| Token | Source | Use |
|-------|--------|-----|
| `palette.*` | `useTheme().palette` | Theme-aware colors (bgApp, bgCard, textPrimary, accentPrimary, gold, etc.) |
| `Spacing`, `Radius`, `FontSize` | Static import | Layout tokens (unchanged by theme) |
| `CardStyle` | Static import | Shared card border/shadow — override `borderColor` from `palette` when themed |
| `FontCinzel`, `FontCinzelBold` | Static import | Header fonts |
| `THEMES.dark` | Static import | Fallback palette (e.g. ErrorBoundary outside ThemeProvider). Prefer `useTheme().palette`. |

Avoid hardcoded hex in components. Use semantic tokens.

---

## 10. Skill Workbench UI (v0.6.0)

For artisan skill screens, prefer the **workbench paradigm** over the plain card-list pattern. Components in `components/skill/`:

- **SkillHeroHeader** — skill title, level, XP bar, active recipe, XP/hour.
- **SkillCategoryRail** — segmented chips for category filtering.
- **RecipeWorkbenchCard** — explicit input/output slots, level gate, affordability, Craft/Stop.
- **StickyTaskDock** — sticky bottom CTA; materials summary.

Woodworking is the reference implementation. See DOCU/SKILLS_ARCHITECTURE.md §0 for design principles and migration path.
