package com.rhysstever.restaurantorders.ui

import java.time.LocalDate

data class RestaurantUIState(
    val restaurants: List<Restaurant> = emptyList(),
    val selectedRestaurant: Restaurant? = null,
    val selectedVisit: Visit? = null,
    val onlyShowFavorites: Boolean = false,
    val isNewRestaurantInputInvalid: Boolean? = null,
    val isRestaurantRenameInputInvalid: Boolean = false,
    val isNewVisitInputInvalid: Boolean? = null,
    val isVisitRenameInputInvalid: Boolean = false,
    val isNewOrderInputInvalid: Boolean? = null,
)

data class Restaurant(
    val name: String,
    val isFavorite: Boolean = false,
    val visits: List<Visit> = emptyList(),
)

data class Visit(
    val name: String,
    val orders: List<Order> = emptyList(),
    val dateVisited: LocalDate? = null,
    val rating: Int? = null,
    val notes: String = "",
)

data class Order(
    val name: String,
    val rating: Int? = null,
    val notes: String = "",
)

val demoUIState = RestaurantUIState(
    restaurants = listOf(
        Restaurant(
            name = "Italian Market",
            isFavorite = false,
            visits = emptyList()
        ),
        Restaurant(
            name = "Mexican Bar that has a really long name for some reason and I don't know why it is so long",
            isFavorite = true,
            visits = listOf(
                Visit(
                    name = "Visit 1",
                    orders = listOf(
                        Order(
                            name = "Birria Tacos",
                            rating = 5,
                            notes = "Great flavors"),
                        Order(
                            name = "Carne Asada Fajitas",
                            rating = 4,
                            notes = "The plate was sizzling"
                        )
                    ),
                    dateVisited = LocalDate.now().minusDays(3),
                    rating = 5,
                    notes = "Great food, service, and atmosphere!"
                ),
                Visit(
                    name = "Visit 2 that has a really really long name and I mean a really long name",
                    orders = listOf(
                        Order(
                            name = "Chicken Enchiladas",
                            rating = 3,
                            notes = "A little dry"),
                        Order(
                            name = "Shrimp Tacos that were quite delicious, I really liked them, but why is this included in its name?",
                            rating = 4,
                            notes = "Good but not as good as the Birria Tacos"
                        )
                    ),
                    dateVisited = LocalDate.now().minusDays(1),
                    rating = 4,
                    notes = "Tried some new things, but did not like them as much as the first visit."
                )
            )
        ),
        Restaurant(
            name = "Burger Joint",
            isFavorite = false,
            visits = listOf(
                Visit(name = "Visit 1")
            )
        )
    ),
    selectedRestaurant = null,
    selectedVisit = null,
    onlyShowFavorites = false,
    isNewRestaurantInputInvalid = null,
    isRestaurantRenameInputInvalid = false,
    isNewOrderInputInvalid = null
)

val demoUIStateSelected = demoUIState.copy(
    selectedRestaurant = demoUIState.restaurants[1],
    selectedVisit = demoUIState.restaurants[1].visits[1]
)