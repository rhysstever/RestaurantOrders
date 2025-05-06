package com.rhysstever.restaurantorders.ui

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val onlyShowFavorites: Boolean = false,
    val isNewRestaurantInputInvalid: Boolean? = null,
)

data class Restaurant(
    val name: String,
    val isFavorite: Boolean = false
)