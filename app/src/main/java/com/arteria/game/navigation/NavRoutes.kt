package com.arteria.game.navigation

object NavRoutes {
    const val AccountSelect = "account_select"
    const val AccountCreate = "account_create"
    const val PlayPlaceholder = "play/{profileId}"

    /** Path segment for [PlayPlaceholder]; id is URL-encoded for safety. */
    fun playPath(profileId: String): String {
        val safe = android.net.Uri.encode(profileId)
        return "play/$safe"
    }
}
