package com.cyberfeedforward.fidgetgames.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Home : Destination
    
    @Serializable
    data object Profile : Destination
    
    @Serializable
    data object Settings : Destination

    @Serializable
    data object About : Destination
}
