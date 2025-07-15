package com.rhysstever.restaurantorders

sealed interface RestaurantDestination {
    val route: String
}

data object Home : RestaurantDestination {
    override val route = "Home"
}

data object AddRestaurant : RestaurantDestination {
    override val route = "Add Restaurant"
}

data object RestaurantInfo : RestaurantDestination {
    override val route = "Restaurant Info"
}

data object AddVisit : RestaurantDestination {
    override val route = "Add Visit"
}

data object VisitInfo : RestaurantDestination {
    override val route = "Visit Info"
}

data object AddOrder : RestaurantDestination {
    override val route = "Add Order"
}