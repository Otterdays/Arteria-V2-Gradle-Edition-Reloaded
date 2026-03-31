package com.arteria.game.navigation

object NavRoutes {
    const val AccountSelect = "account_select"
    const val AccountCreate = "account_create"
    const val Game = "game/{profileId}"

    /** Path segment for [Game]; id is URL-encoded for safety. */
    fun gamePath(profileId: String): String {
        val safe = android.net.Uri.encode(profileId)
        return "game/$safe"
    }
}
