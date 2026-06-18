<!-- PRESERVATION RULE: Never delete or replace content. Append or annotate only. -->

# FUTURE UPDATES — canonical backlog & suggestions

**`[AMENDED 2026-06-18]:`** All **future work**, **QoL ideas**, **release carve proposals**, and **agent task synthesis** for this repo live in **`DOCS/FUTURE UPDATES/`**. Do not add new suggestion docs at repo root or loose under `DOCS/` — put them here.

---

## For AI agents (read this first)

| Order | When | Doc |
|-------|------|-----|
| 1 | Live handoff, blockers, what shipped | [`DOCS/SCRATCHPAD.md`](../SCRATCHPAD.md) |
| 2 | Product truth (version, tabs, skills) | [`DOCS/SUMMARY.md`](../SUMMARY.md) → Live Product Snapshot |
| 3 | **Next release slice** | [`RELEASE_PLAN.md`](RELEASE_PLAN.md) |
| 4 | Structured agent tasks (Opie / Opus synthesis) | [`claudes_checklist_by_ryan.md`](claudes_checklist_by_ryan.md) |
| 5 | Settings & platform UX backlog | [`master_settings_suggestions_doc.md`](master_settings_suggestions_doc.md) |
| 6 | 100-item brainstorm picker | [`top-100-next-todo.md`](top-100-next-todo.md) |

**Operational docs stay outside this folder:** `SBOM.md`, `ARCHITECTURE.md`, `ROADMAP.md`, `SKILLS_EXPANSION_NATIVE.md`, `MIGRATION_SPEC.md`.

**When you ship player-visible work:** update `SCRATCHPAD.md`, and if release-worthy also `ChangelogScreen.kt`, `app/build.gradle.kts`, README badge — per `AGENTS.md` / `CLAUDE.md` handoff rules. Mark items done in the relevant FUTURE UPDATES checklist (do not delete rows).

---

## Folder index

| File | Purpose |
|------|---------|
| **README.md** | This hub — routing rules for humans and agents. |
| **RELEASE_PLAN.md** | Near-term release spine (1.10.x carved slices, 1.11 horizon). |
| **claudes_checklist_by_ryan.md** | Ryan → agent synthesis checklist (sections 6+ open; §1–5 shipped). |
| **master_settings_suggestions_doc.md** | Settings design spec + rollout checklist. |
| **top-100-next-todo.md** | 100-item checkbox backlog distilled from roadmap + settings + skills. |

---

## Adding new suggestions

1. **Prefer amending an existing file** in this folder (append at top of the relevant section — most-recent-first).
2. **New topic file:** create under `DOCS/FUTURE UPDATES/` only; add a row to the index table above in this README.
3. **Code trace tags:** link implementations with `// [TRACE: DOCS/FUTURE UPDATES/<file>.md — …]`.
4. **Legacy paths:** `DOCS/claudes_checklist_by_ryan.md`, `DOCS/RELEASE_PLAN.md`, etc. are **redirect stubs** — edit files in this folder, not the stubs.

---

## Related (not in this folder)

| Doc | Role |
|-----|------|
| [`DOCS/ROADMAP.md`](../ROADMAP.md) | Phased delivery history (append `[DONE]` markers). |
| [`DOCS/SKILLS_EXPANSION_NATIVE.md`](../SKILLS_EXPANSION_NATIVE.md) | Skill roster vs trainables playbook. |
| [`DOCS/agent_prompt_report_for_research.md`](../agent_prompt_report_for_research.md) | Deep research handoff brief (architecture inventory). |
| [`DOCS/ARTERIA-V1-DOCS/DOCU/IMPROVEMENTS.md`](../ARTERIA-V1-DOCS/DOCU/IMPROVEMENTS.md) | V1 design ideas (read-only parity reference). |
