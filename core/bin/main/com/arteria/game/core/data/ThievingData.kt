package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Thieving yields coins and small valuables — fast actions, lower XP per action than mining.

object ThievingData {
    val MARKET_COINS    = ItemDef("market_coins",    "Market Coins",    "A few copper coins lifted from a merchant's stall")
    val STOLEN_TRINKET  = ItemDef("stolen_trinket",  "Stolen Trinket",  "A shiny bauble pilfered from a distracted trader")
    val GUARDS_POUCH    = ItemDef("guards_pouch",    "Guard's Pouch",   "A small purse lifted from an off-duty guard")
    val NOBLES_GEM      = ItemDef("nobles_gem",      "Noble's Gem",     "A cut gem lifted from a noble's coat pocket")
    val GUILD_SEAL      = ItemDef("guild_seal",      "Guild Seal",      "A tradeable seal from a merchant guild")
    val VAULT_FRAGMENT  = ItemDef("vault_fragment",  "Vault Fragment",  "A shard of refined gold from a merchant vault")
    val ASTRAL_COIN     = ItemDef("astral_coin",     "Astral Coin",     "Magical currency humming with ley energy")
    val VOID_SHARD      = ItemDef("void_shard",      "Void Shard",      "A fragment of pure void essence — priceless")

    val items: Map<String, ItemDef> = listOf(
        MARKET_COINS, STOLEN_TRINKET, GUARDS_POUCH, NOBLES_GEM,
        GUILD_SEAL, VAULT_FRAGMENT, ASTRAL_COIN, VOID_SHARD,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("thieve_stall",       SkillId.THIEVING, "Market Stall",      1,  8.0,  1800L, "market_coins"),
        SkillAction("thieve_trader",      SkillId.THIEVING, "Wandering Trader",  10, 15.0,  2400L, "stolen_trinket"),
        SkillAction("thieve_guard",       SkillId.THIEVING, "City Guard",        25, 26.0,  3000L, "guards_pouch"),
        SkillAction("thieve_noble",       SkillId.THIEVING, "Pompous Noble",     40, 45.0,  3600L, "nobles_gem"),
        SkillAction("thieve_guild",       SkillId.THIEVING, "Guild Storehouse",  55, 75.0,  4800L, "guild_seal"),
        SkillAction("thieve_vault",       SkillId.THIEVING, "Merchant Vault",    70, 120.0, 6000L, "vault_fragment"),
        SkillAction("thieve_astral_bank", SkillId.THIEVING, "Astral Treasury",   82, 200.0, 7800L, "astral_coin"),
        SkillAction("thieve_void_node",   SkillId.THIEVING, "Void Node",         95, 350.0,10200L, "void_shard"),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
