package com.arteria.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.room.withTransaction
import com.arteria.game.data.game.GameDatabase
import com.arteria.game.data.game.GameRepository
import com.arteria.game.data.profile.ProfileDatabase
import com.arteria.game.data.profile.RoomProfileRepository
import com.arteria.game.navigation.NavRoutes
import com.arteria.game.ui.account.AccountCreationScreen
import com.arteria.game.ui.account.AccountSelectionScreen
import com.arteria.game.ui.account.AccountSessionInfo
import com.arteria.game.ui.account.AccountViewModel
import com.arteria.game.ui.game.GameScreen
import kotlinx.coroutines.launch

@Composable
fun ArteriaApp(modifier: Modifier = Modifier) {
    // [TRACE: DOCS/SCRATCHPAD.md — startup account/session persistence]
    val appContext = LocalContext.current.applicationContext
    val database = remember(appContext) {
        Room.databaseBuilder(
            appContext,
            ProfileDatabase::class.java,
            "arteria_profiles.db",
        ).build()
    }
    val repository = remember(database) { RoomProfileRepository(database) }
    val gameDatabase = remember(appContext) {
        Room.databaseBuilder(
            appContext,
            GameDatabase::class.java,
            "arteria_game.db",
        ).addMigrations(
            GameDatabase.MIGRATION_1_2,
            GameDatabase.MIGRATION_2_3,
            GameDatabase.MIGRATION_3_4,
            GameDatabase.MIGRATION_4_5,
            GameDatabase.MIGRATION_5_6,
        )
            .build()
    }
    val gameRepository = remember(gameDatabase) {
        GameRepository(
            dao = gameDatabase.gameDao(),
            runInTransaction = { block -> gameDatabase.withTransaction { block() } },
        )
    }
    val accountViewModel: AccountViewModel = viewModel(
        factory = AccountViewModel.factory(repository),
    )
    val uiState by accountViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.AccountSelect,
        modifier = modifier,
    ) {
        composable(NavRoutes.AccountSelect) {
            AccountSelectionScreen(
                accounts = uiState.accounts,
                selectedId = uiState.selectedId,
                errorMessage = uiState.errorMessage,
                onSelectAccount = accountViewModel::selectAccount,
                onCreateNewClick = { navController.navigate(NavRoutes.AccountCreate) },
                onContinueWithAccount = {
                    scope.launch {
                        val selectedId = accountViewModel.continueWithSelectedProfile()
                        if (selectedId != null) {
                            navController.navigate(NavRoutes.gamePath(selectedId))
                        }
                    }
                },
            )
        }
        composable(NavRoutes.AccountCreate) {
            AccountCreationScreen(
                onBack = { navController.popBackStack() },
                errorMessage = uiState.errorMessage,
                onClearError = accountViewModel::clearError,
                onCreateAccount = { name ->
                    scope.launch {
                        val created = accountViewModel.createProfile(name)
                        if (created) {
                            navController.popBackStack()
                        }
                    }
                },
            )
        }
        composable(
            route = NavRoutes.Game,
            arguments = listOf(
                navArgument("profileId") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("profileId").orEmpty()
            val profileId = android.net.Uri.decode(raw)
            var accountSession by remember(profileId) {
                mutableStateOf(
                    AccountSessionInfo(
                        displayName = "Preparing session…",
                        lastPlayedAtEpochMs = 0L,
                        gameMode = "Standard",
                    ),
                )
            }
            LaunchedEffect(profileId) {
                accountViewModel.resolveSession(profileId)?.let { accountSession = it }
                    ?: run {
                        accountSession = AccountSessionInfo(
                            displayName = accountViewModel.resolveDisplayName(profileId),
                            lastPlayedAtEpochMs = 0L,
                            gameMode = "Standard",
                        )
                    }
            }
            GameScreen(
                profileId = profileId,
                gameRepository = gameRepository,
                accountSession = accountSession,
                onRefreshAccountSession = {
                    accountViewModel.resolveSession(profileId)?.let { accountSession = it }
                },
                onRenameDisplayName = { newName -> accountViewModel.updateDisplayName(profileId, newName) },
                onResetGameProgress = {
                    runCatching { gameRepository.resetProgressForProfile(profileId) }
                },
                onDeleteProfileEverywhere = {
                    runCatching {
                        gameRepository.deleteAllGameDataForProfile(profileId)
                        repository.deleteProfile(profileId).getOrThrow()
                    }
                },
                onProfileFullyDeleted = {
                    navController.popBackStack(NavRoutes.AccountSelect, inclusive = false)
                },
                onBackToAccounts = {
                    navController.popBackStack(NavRoutes.AccountSelect, inclusive = false)
                },
            )
        }
    }
}
