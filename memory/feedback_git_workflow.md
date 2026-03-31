---
name: Avoid branches, work directly on main
description: User prefers direct main branch commits over feature branches
type: feedback
---

**Rule:** Work directly on the main branch. Do not create feature branches or worktrees.

**Why:** User explicitly stated preference against branch workflow ("I hate that you made a branch"). Direct commits to main are preferred for this project.

**How to apply:** When making changes to this codebase, commit directly to main. Skip the EnterWorktree / PR workflow unless explicitly asked by the user.
