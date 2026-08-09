package com.github.jameshnsears.chance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

val LocalNavigator = compositionLocalOf<Navigator> {
    error("No Navigator provided")
}

@Composable
fun rememberNavigationState(
    startRoute: ChanceNavKey,
    topLevelRoutes: Set<ChanceNavKey>
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(key as NavKey)
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

class NavigationState(
    val startRoute: ChanceNavKey,
    topLevelRoute: MutableState<ChanceNavKey>,
    val backStacks: Map<ChanceNavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute: ChanceNavKey by topLevelRoute

    @Composable
    fun toDecoratedEntries(
        entryProvider: (ChanceNavKey) -> NavEntry<NavKey>
    ): List<NavEntry<NavKey>> {
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = { key -> entryProvider(key as ChanceNavKey) }
            )
        }

        return getTopLevelRoutesInUse()
            .flatMap { decoratedEntries[it] ?: emptyList() }
    }

    private fun getTopLevelRoutesInUse(): List<ChanceNavKey> =
        if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            // we need both so startRoute (setup) is preserved when moving to roll
            listOf(startRoute, topLevelRoute)
        }
}

class Navigator(val state: NavigationState) {
    private val _reselectEvents = MutableSharedFlow<ChanceNavKey>(extraBufferCapacity = 1)
    val reselectEvents = _reselectEvents.asSharedFlow()

    fun navigate(route: ChanceNavKey) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route as NavKey)
        }
    }

    fun onReselect(route: ChanceNavKey) {
        _reselectEvents.tryEmit(route)
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
