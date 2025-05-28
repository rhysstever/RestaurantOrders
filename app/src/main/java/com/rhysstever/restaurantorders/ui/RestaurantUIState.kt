package com.rhysstever.restaurantorders.ui

import java.time.LocalDate

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val onlyShowFavorites: Boolean = false,
    val isNewRestaurantInputInvalid: Boolean? = null,
    val selectedRestaurant: Restaurant? = null,
    val isRestaurantRenameInputInvalid: Boolean = false,
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
    val notes: String,
    val dateCreated: LocalDate,
    val dateOrdered: LocalDate?
)