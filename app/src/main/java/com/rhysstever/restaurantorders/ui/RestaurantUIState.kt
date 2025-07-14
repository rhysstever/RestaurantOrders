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
    val rating: Int?,
    val dateOrdered: LocalDate?,
    val notes: String
)

val demoUIState = RestaurantUIState(
    restaurants = listOf(
        Restaurant("Restaurant A", true, emptyList()),
        Restaurant("Restaurant B", false, listOf(
            Order("Order 1", 5, LocalDate.now(), "Great service!"),
            Order("Order 2", 4, LocalDate.now().minusDays(1), "Good food.")
        )),
        Restaurant("Restaurant C", true, listOf(
            Order("Order 3", 3, LocalDate.now().minusDays(2), "Average experience."),
            Order("Order 4", null, LocalDate.now().minusDays(3), "No rating yet.")
        ))
    ),
    onlyShowFavorites = false,
    isNewRestaurantInputInvalid = null,
    selectedRestaurant = null,
    isRestaurantRenameInputInvalid = false,
    isNewOrderInputInvalid = null
)

val demoUIStateSelected = demoUIState.copy(selectedRestaurant = demoUIState.restaurants[1])