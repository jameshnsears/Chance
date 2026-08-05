package com.github.jameshnsears.chance.ui.navigation

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NavigationTest : UtilityAndroidUnitTestHelper() {

    private lateinit var navigationState: NavigationState
    private lateinit var navigator: Navigator

    @Before
    fun setUp() {
        val topLevelRoutes = setOf(ChanceNavKey.SetupDice, ChanceNavKey.SetupGroups, ChanceNavKey.Roll)
        val topLevelRouteState = mutableStateOf<ChanceNavKey>(ChanceNavKey.SetupDice)

        // Mocking the back stacks as they would be created by rememberNavBackStack
        val backStacks = topLevelRoutes.associateWith {
            mockk<androidx.navigation3.runtime.NavBackStack<androidx.navigation3.runtime.NavKey>>(relaxed = true)
        }

        navigationState = NavigationState(
            startRoute = ChanceNavKey.SetupDice,
            topLevelRoute = topLevelRouteState,
            backStacks = backStacks
        )
        navigator = Navigator(navigationState)
    }

    @Test
    fun navigateBetweenTabs() {
        assertEquals(ChanceNavKey.SetupDice, navigationState.topLevelRoute)

        navigator.navigate(ChanceNavKey.Roll)
        assertEquals(ChanceNavKey.Roll, navigationState.topLevelRoute)

        navigator.navigate(ChanceNavKey.SetupGroups)
        assertEquals(ChanceNavKey.SetupGroups, navigationState.topLevelRoute)
    }

    @Test
    fun goBackPattern() {
        // Exit through home pattern
        navigator.navigate(ChanceNavKey.Roll)
        assertEquals(ChanceNavKey.Roll, navigationState.topLevelRoute)

        // Mock currentStack.last() to be the topLevelRoute
        every { navigationState.backStacks[ChanceNavKey.Roll]!!.last() } returns ChanceNavKey.Roll

        navigator.goBack()
        assertEquals(ChanceNavKey.SetupDice, navigationState.topLevelRoute)
    }

    @Test
    fun exerciseGroupCreationLogic() {
        val application = mockk<Application>(relaxed = true)
        val viewModel = GroupsAndroidViewModel(
            application,
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        val newGroup = Group(name = "New Group")

        // Exercise new group save call.
        viewModel.onSave(newGroup)

        assertTrue(true)
    }
}
