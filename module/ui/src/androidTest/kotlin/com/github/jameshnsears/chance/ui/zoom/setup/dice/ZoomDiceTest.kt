package com.github.jameshnsears.chance.ui.zoom.setup.dice

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.dice.DialogBag
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceTabTestTag
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.LocalNavigator
import com.github.jameshnsears.chance.ui.navigation.Navigator
import com.github.jameshnsears.chance.ui.navigation.rememberNavigationState
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class ZoomDiceTest : AndroidTestHelper() {
    @After
    fun tearDown() {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
        )
    }

    @Test
    fun sideDescription() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag

        val diceStory = bagDataTestDouble.diceStory
        repositoryBag.store(mutableListOf(diceStory))

        val viewModel = ZoomDiceAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryLocked
        )

        composeRule.setContent {
            val navigationState = rememberNavigationState(
                startRoute = ChanceNavKey.SetupDice,
                topLevelRoutes = setOf(ChanceNavKey.SetupDice)
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

            val entryProvider = entryProvider {
                entry<ChanceNavKey.SetupDice> {
                    ZoomBag(viewModel)
                }
                entry<ChanceNavKey.DialogDice>(
                    metadata = DialogSceneStrategy.dialog(
                        DialogProperties(
                            usePlatformDefaultWidth = false,
                            decorFitsSystemWindows = false
                        )
                    )
                ) { key ->
                    val diceBag by viewModel.diceBagList.collectAsStateWithLifecycle()
                    val dice = diceBag.find { it.uuid == key.diceUuid }
                    val side = dice?.sides?.find { it.uuid == key.sideUuid }
                    if (dice != null && side != null) {
                        val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
                            key = "${dice.uuid}-${side.uuid}",
                            factory = DialogDiceAndroidViewModelFactory(
                                ApplicationProvider.getApplicationContext(),
                                viewModel.repositoryBag,
                                dice,
                                side
                            )
                        )

                        DialogBag(
                            onClose = { navigator.goBack() },
                            dialogDiceAndroidViewModel = dialogDiceAndroidViewModel
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            val decoratedEntryProvider: (ChanceNavKey) -> NavEntry<NavKey> =
                { key -> entryProvider(key) as NavEntry<NavKey> }

            CompositionLocalProvider(LocalNavigator provides navigator) {
                NavDisplay(
                    entries = navigationState.toDecoratedEntries(decoratedEntryProvider),
                    onBack = { navigator.goBack() },
                    sceneStrategies = listOf(dialogStrategy)
                )
            }
        }

        // multiple sides have same description tag in this Composable
        val descriptionTag = "${ZoomDiceTestTag.ZOOM_SIDE_DESCRIPTION}-${diceStory.title}"
        waitForTag(descriptionTag)
        composeRule.onAllNodesWithTag(descriptionTag)[0].assertIsDisplayed()
    }

    @Test
    fun showEpochUuid() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
            UtilityFeature.Flag.UI_SHOW_UUID
        )

        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag
        val firstDice = bagDataTestDouble.d6
        repositoryBag.store(mutableListOf(firstDice))

        val viewModel = ZoomDiceAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryLocked
        )

        composeRule.setContent {
            val navigationState = rememberNavigationState(
                startRoute = ChanceNavKey.SetupDice,
                topLevelRoutes = setOf(ChanceNavKey.SetupDice)
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

            val entryProvider = entryProvider {
                entry<ChanceNavKey.SetupDice> {
                    ZoomBag(viewModel)
                }
                entry<ChanceNavKey.DialogDice>(
                    metadata = DialogSceneStrategy.dialog(
                        DialogProperties(
                            usePlatformDefaultWidth = false,
                            decorFitsSystemWindows = false
                        )
                    )
                ) { key ->
                    val diceBag by viewModel.diceBagList.collectAsStateWithLifecycle()
                    val dice = diceBag.find { it.uuid == key.diceUuid }
                    val side = dice?.sides?.find { it.uuid == key.sideUuid }
                    if (dice != null && side != null) {
                        val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
                            key = "${dice.uuid}-${side.uuid}",
                            factory = DialogDiceAndroidViewModelFactory(
                                ApplicationProvider.getApplicationContext(),
                                viewModel.repositoryBag,
                                dice,
                                side
                            )
                        )

                        DialogBag(
                            onClose = { navigator.goBack() },
                            dialogDiceAndroidViewModel = dialogDiceAndroidViewModel
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            val decoratedEntryProvider: (ChanceNavKey) -> NavEntry<NavKey> =
                { key -> entryProvider(key) as NavEntry<NavKey> }

            CompositionLocalProvider(LocalNavigator provides navigator) {
                NavDisplay(
                    entries = navigationState.toDecoratedEntries(decoratedEntryProvider),
                    onBack = { navigator.goBack() },
                    sceneStrategies = listOf(dialogStrategy)
                )
            }
        }

        waitForText(firstDice.title)
        composeRule.onNodeWithText(firstDice.title).assertIsDisplayed()
        composeRule.onNodeWithText(firstDice.uuid).assertIsDisplayed()
    }

    @Test
    fun sideOpensDialog() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag
        val allDice = bagDataTestDouble.allDice()
        repositoryBag.store(allDice)

        val viewModel = ZoomDiceAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryLocked
        )

        composeRule.setContent {
            val navigationState = rememberNavigationState(
                startRoute = ChanceNavKey.SetupDice,
                topLevelRoutes = setOf(ChanceNavKey.SetupDice)
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

            val entryProvider = entryProvider {
                entry<ChanceNavKey.SetupDice> {
                    ZoomBag(viewModel)
                }
                entry<ChanceNavKey.DialogDice>(
                    metadata = DialogSceneStrategy.dialog(
                        DialogProperties(
                            usePlatformDefaultWidth = false,
                            decorFitsSystemWindows = false
                        )
                    )
                ) { key ->
                    val diceBag by viewModel.diceBagList.collectAsStateWithLifecycle()
                    val dice = diceBag.find { it.uuid == key.diceUuid }
                    val side = dice?.sides?.find { it.uuid == key.sideUuid }
                    if (dice != null && side != null) {
                        val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
                            key = "${dice.uuid}-${side.uuid}",
                            factory = DialogDiceAndroidViewModelFactory(
                                ApplicationProvider.getApplicationContext(),
                                viewModel.repositoryBag,
                                dice,
                                side
                            )
                        )

                        DialogBag(
                            onClose = { navigator.goBack() },
                            dialogDiceAndroidViewModel = dialogDiceAndroidViewModel
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            val decoratedEntryProvider: (ChanceNavKey) -> NavEntry<NavKey> =
                { key -> entryProvider(key) as NavEntry<NavKey> }

            CompositionLocalProvider(LocalNavigator provides navigator) {
                NavDisplay(
                    entries = navigationState.toDecoratedEntries(decoratedEntryProvider),
                    onBack = { navigator.goBack() },
                    sceneStrategies = listOf(dialogStrategy)
                )
            }
        }

        val firstDice = allDice[0]
        val firstSide = firstDice.sides[0]
        val sideTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-${firstDice.title}-${firstSide.number}"

        waitForTag(sideTag)
        composeRule.onNodeWithTag(sideTag).performClick()

        waitForTag(DialogDiceTabTestTag.TAB_SIDE)
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).assertIsDisplayed()
    }
}
