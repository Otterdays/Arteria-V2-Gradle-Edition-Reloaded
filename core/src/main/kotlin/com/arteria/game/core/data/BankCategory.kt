package com.arteria.game.core.data

/**
 * Heuristic bank grouping for UI — id-pattern based until items carry explicit tags.
 */
enum class BankItemCategory(val displayName: String, val sortOrder: Int) {
    ORES_AND_BARS("Ores & bars", 0),
    LOGS_AND_WOOD("Logs & wood", 1),
    FISH_AND_FOOD("Fish & food", 2),
    HERBS("Herbs & crops", 3),
    POTIONS("Potions & brews", 4),
    GEMS("Gems & stones", 5),
    EQUIPMENT("Gear & tools", 6),
    SALVAGE("Salvage & parts", 7),
    COMBAT("Combat drops", 8),
    MISC("Other", 99),
}

object BankCategoryRules {

    fun categoryFor(itemId: String): BankItemCategory {
        val id = itemId.lowercase()
        return when {
            id.endsWith("_ore") || id.endsWith("_bar") || id == "coal" -> BankItemCategory.ORES_AND_BARS
            id.contains("log") || id.contains("plank") || id.contains("wood") ||
                id.contains("frame") || id.contains("staff") || id.contains("shield") ->
                BankItemCategory.LOGS_AND_WOOD
            id.contains("fish") || id.contains("shrimp") || id.contains("lobster") ||
                id.contains("shark") || id.contains("sardine") || id.contains("trout") ||
                id.contains("salmon") || id.contains("tuna") || id.contains("swordfish") ||
                id.startsWith("cooked_") || id.startsWith("raw_") ->
                BankItemCategory.FISH_AND_FOOD
            id.contains("herb") || id.contains("seed") || id.contains("crop") ||
                id.contains("grain") || id.contains("flax") || id.contains("wheat") ->
                BankItemCategory.HERBS
            id.contains("potion") || id.contains("elixir") || id.contains("brew") ||
                id.contains("vial") ->
                BankItemCategory.POTIONS
            id.contains("gem") || id.contains("sapphire") || id.contains("emerald") ||
                id.contains("ruby") || id.contains("diamond") || id.contains("cut_") ->
                BankItemCategory.GEMS
            id.contains("salvage") || id.contains("scrap") || id.contains("salvaged") ->
                BankItemCategory.SALVAGE
            id == "rat_tail" || id.contains("_tail") || id.contains("charm") ||
                id.contains("pouch") ->
                BankItemCategory.COMBAT
            id.contains("pickaxe") || id.contains("axe") || id.contains("sword") ||
                id.contains("armor") || id.contains("ring") || id.contains("amulet") ||
                id.contains("helm") || id.contains("hat") ->
                BankItemCategory.EQUIPMENT
            else -> BankItemCategory.MISC
        }
    }

    fun groupItems(itemIds: Collection<String>): Map<BankItemCategory, List<String>> =
        itemIds
            .groupBy { categoryFor(it) }
            .toSortedMap(compareBy { it.sortOrder })
}
