package com.rhysstever.restaurantorders.ui

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val onlyShowFavorites: Boolean = false,
    val isNewRestaurantInputInvalid: Boolean? = null,
    val selectedRestaurant: Restaurant? = null,
    val isNewOrderInputInvalid: Boolean? = null,
)

data class Restaurant(
    val name: String,
    val isFavorite: Boolean = false,
    val orders: List<Order> = emptyList()
)

data class Order(
    val name: String,
    val rating: Int,
    val notes: String
)