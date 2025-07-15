package com.rhysstever.restaurantorders.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class RestaurantViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RestaurantUIState())
    val uiState: StateFlow<RestaurantUIState> = _uiState.asStateFlow()

    fun toggleShowingFavorites() {
        _uiState.update { currentState ->
            currentState.copy(
                onlyShowFavorites = !currentState.onlyShowFavorites
            )
        }
    }

    inner class RestaurantContent {
        fun checkNewRestaurantInput(newRestaurantInput: String) {
            _uiState.update { currentState ->
                // Check if there is a selected restaurant
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    // When a restaurant is selected (renaming restaurant)

                    // The input is invalid if there is no input or it is blank (full of whitespace)
                    if(newRestaurantInput.isEmpty() || newRestaurantInput.isBlank()) {
                        currentState.copy(
                            isNewRestaurantInputInvalid = true
                        )
                    }
                    // The input is valid if the input is the same as the selected restaurant name
                    else if(newRestaurantInput == selectedRestaurant.name) {
                        currentState.copy(
                            isNewRestaurantInputInvalid = false
                        )
                    }
                    // The input is valid if the input is an unique restaurant name
                    else {
                        currentState.copy(
                            isNewRestaurantInputInvalid = currentState.restaurants
                                .filter { it != selectedRestaurant }    // Exclude the selected restaurant
                                .map { it.name }.contains(newRestaurantInput)
                        )
                    }
                } ?: run {
                    // When no restaurant is selected (adding restaurant)

                    // The input is null if there is no input
                    if(newRestaurantInput.isEmpty()) {
                        currentState.copy(
                            isNewRestaurantInputInvalid = null
                        )
                    }
                    // The input is invalid if the input is blank (full of whitespace)
                    else if(newRestaurantInput.isBlank()) {
                        currentState.copy(
                            isNewRestaurantInputInvalid = true
                        )
                    }
                    // The input is valid if it is an unique restaurant name
                    else {
                        currentState.copy(
                            isNewRestaurantInputInvalid = currentState.restaurants
                                .map { it.name }.contains(newRestaurantInput)
                        )
                    }
                }
            }
        }

        fun addRestaurant(restaurantName: String) {
            val newRestaurant = Restaurant(restaurantName)
            addRestaurant(newRestaurant = newRestaurant)
        }

        fun addRestaurant(newRestaurant: Restaurant) {
            _uiState.update { currentState ->
                // If the list is not null, try to add the new restaurant to the list
                if(currentState.restaurants.contains(newRestaurant)) {
                    // If the restaurant already exists, return the current state
                    currentState
                } else {
                    // If the restaurant is a new restaurant, add it to the list
                    val newList = currentState.restaurants.toMutableList()
                    newList.add(newRestaurant)
                    // Sort the new list by name
                    val sortedList = newList.toList().sortedBy { it.name.lowercase(Locale.ROOT) }

                    // Return the current state with the new list
                    currentState.copy(
                        restaurants = sortedList
                    )
                }
            }
        }

        fun checkRestaurantRenameInput(newRestaurantInput: String) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    if(newRestaurantInput.isEmpty() || newRestaurantInput.isBlank()) {
                        currentState.copy(
                            isRestaurantRenameInputInvalid = true
                        )
                    } else if(newRestaurantInput == selectedRestaurant.name) {
                        currentState.copy(
                            isRestaurantRenameInputInvalid = false
                        )
                    } else {
                        currentState.copy(
                            isRestaurantRenameInputInvalid = currentState.restaurants
                                .filter { it != selectedRestaurant }
                                .map { it.name }
                                .contains(newRestaurantInput)
                        )
                    }
                } ?: currentState
            }
        }

        fun renameRestaurant(newRestaurantName: String) {
            // Copy the current restaurant
            val newRestaurant = uiState.value.selectedRestaurant!!.copy(
                name = newRestaurantName
            )

            // Remove the old restaurant from the list
            _uiState.update { currentState ->
                val restaurantIndex = currentState.restaurants.indexOf(currentState.selectedRestaurant)
                val newList = currentState.restaurants.toMutableList()
                newList.removeAt(restaurantIndex)

                // Sort the new list by name
                val sortedList = newList.toList().sortedBy { it.name.lowercase(Locale.ROOT) }

                currentState.copy(
                    restaurants = sortedList,
                    selectedRestaurant = newRestaurant
                )
            }

            // Add the new restaurant to the list
            addRestaurant(newRestaurant = newRestaurant)
        }

        fun selectRestaurant(restaurant: Restaurant?) {
            _uiState.update { currentState ->
                restaurant?.let {
                    if(currentState.restaurants.contains(restaurant)) {
                        // Set the restaurant input as the name of the selected restaurant
                        checkRestaurantRenameInput(restaurant.name)

                        // Set the given restaurant as the selected restaurant
                        currentState.copy(
                            selectedRestaurant = restaurant
                        )
                    } else {
                        currentState.copy(
                            selectedRestaurant = null
                        )
                    }
                } ?: run {
                    currentState.copy(
                        selectedRestaurant = null
                    )
                }
            }
        }

        fun toggleRestaurantIsFavorite(restaurant: Restaurant) {
            _uiState.update { currentState ->
                val restaurantIndex = currentState.restaurants.indexOf(restaurant)
                if(restaurantIndex != -1) {
                    val newList = currentState.restaurants.toMutableList()
                    newList[restaurantIndex] = restaurant.copy(isFavorite = !restaurant.isFavorite)

                    currentState.copy(
                        restaurants = newList,
                        selectedRestaurant = newList[restaurantIndex]
                    )
                } else {
                    currentState
                }
            }
        }
    }

    inner class VisitContent {
        fun checkNewVisitInput(visitName: String) {
            if(visitName.isEmpty()) {
                // If there is no input, set the input validity to null
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewVisitInputInvalid = null
                    )
                }
            } else if(visitName.isBlank()) {
                // If the input is blank (full of whitespace), set the input to invalid
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewVisitInputInvalid = true
                    )
                }
            } else {
                // Otherwise it is valid
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewVisitInputInvalid = false
                    )
                }
            }
        }

        fun addVisit(restaurant: Restaurant, newVisit: Visit) {
            _uiState.update { currentState ->
                val restaurantIndex = currentState.restaurants.indexOf(restaurant)

                if(restaurantIndex != -1) {
                    val newRestaurantList = currentState.restaurants.toMutableList()
                    val newVisitList = currentState.restaurants[restaurantIndex].visits.toMutableList()

                    if(newVisitList.contains(newVisit)) {
                        // If the order already exists, return the current state
                        return@update currentState
                    } else {
                        // If the order is a new order, add it to the list
                        newVisitList.add(newVisit)
                    }

                    // Update the restaurant with the new visit
                    // And sort the visits list by rating, then by name
                    val newRestaurant = restaurant.copy(
                        visits = newVisitList.toList().sortedWith(compareBy(Visit::rating, Visit::name))
                    )
                    newRestaurantList[restaurantIndex] = newRestaurant

                    currentState.copy(
                        restaurants = newRestaurantList,
                        selectedRestaurant = newRestaurantList[restaurantIndex]
                    )
                } else {
                    currentState
                }
            }
        }

        fun removeVisit(visit: Visit) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { currentRestaurant ->
                    // Get the index of the current selected restaurant
                    val restaurantIndex = currentState.restaurants.indexOf(currentRestaurant)

                    // Check if the restaurant index is valid and if the order exists in the restaurant
                    if(restaurantIndex != -1 && currentRestaurant.visits.contains(visit)) {
                        // Get the list of orders and remove the given order
                        val newOrderList = currentRestaurant.visits.toMutableList()
                        newOrderList.remove(visit)

                        // Create a new restaurant with the updated order list
                        val newRestaurant = currentRestaurant.copy(
                            visits = newOrderList.toList()
                        )

                        // Get the list of all restaurants and update it with the new restaurant
                        val newRestaurantList = currentState.restaurants.toMutableList()
                        newRestaurantList[restaurantIndex] = newRestaurant

                        // Update the restaurants list and selected restaurant
                        currentState.copy(
                            restaurants = newRestaurantList,
                            selectedRestaurant = newRestaurantList[restaurantIndex]
                        )
                    } else {
                        currentState
                    }
                } ?: currentState
            }
        }

        fun selectVisit(visit: Visit?) {
            _uiState.update { currentState ->
                visit?.let {
                    if(currentState.selectedRestaurant != null
                        && currentState.selectedRestaurant.visits.contains(visit)) {
                        // Set the given restaurant as the selected restaurant
                        currentState.copy(
                            selectedVisit = visit
                        )
                    } else {
                        currentState.copy(
                            selectedVisit = null
                        )
                    }
                } ?: run {
                    currentState.copy(
                        selectedVisit = null
                    )
                }
            }
        }

        fun replaceSelectedVisit(newVisit: Visit) {


            // Remove the current visit
            _uiState.update { currentState ->
                // Find the restaurant index, exiting early if not found
                val restaurantIndex = currentState.restaurants.indexOf(currentState.selectedRestaurant)
                if(restaurantIndex == -1) {
                    return@update currentState
                }

                // Find the visit index, exiting early if not found
                val visitIndex = currentState.restaurants[restaurantIndex].visits.indexOf(currentState.selectedVisit)
                if(visitIndex == -1) {
                    return@update currentState
                }

                // Replace the currently selected visit with the new visit
                val newVisitList = currentState.restaurants[restaurantIndex].visits.toMutableList()
                newVisitList[visitIndex] = newVisit

                // Sort visits by date visited
                val sortedVisitsList = newVisitList.toList().sortedBy { it.dateVisited }

                // Update the restaurants list with the new visits list
                val newRestaurantList = currentState.restaurants.toMutableList()
                newRestaurantList[restaurantIndex] = newRestaurantList[restaurantIndex].copy(visits = sortedVisitsList)

                currentState.copy(
                    restaurants = newRestaurantList,
                    selectedRestaurant = newRestaurantList[restaurantIndex],
                    selectedVisit = newVisit
                )
            }
        }
    }

    inner class OrderContent {
        fun checkNewOrderInput(orderName: String) {
            if(orderName.isEmpty()) {
                // If there is no input, set the input validity to null
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewOrderInputInvalid = null
                    )
                }
            } else if(orderName.isBlank()) {
                // If the input is blank (full of whitespace), set the input to invalid
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewOrderInputInvalid = true
                    )
                }
            } else {
                // If the input is already a restaurant name, the input is invalid
                _uiState.update { currentState ->
                    currentState.copy(
                        isNewOrderInputInvalid = currentState.selectedVisit!!.orders
                            .map { it.name }.contains(orderName)
                    )
                }
            }
        }

        fun addNewOrder(restaurant: Restaurant, visit: Visit, order: Order) {
            _uiState.update { currentState ->
                val restaurantIndex = currentState.restaurants.indexOf(restaurant)

                if(restaurantIndex == -1) {
                    // If the restaurant does not exist, return the current state
                    return@update currentState
                }

                val newRestaurantList = currentState.restaurants.toMutableList()
                val visitIndex = currentState.restaurants[restaurantIndex].visits.indexOf(visit)

                if(visitIndex == -1) {
                    // If the visit does not exist, return the current state
                    return@update currentState
                }

                val newVisitList = currentState.restaurants[restaurantIndex].visits.toMutableList()
                val newOrderList = currentState.restaurants[restaurantIndex].visits[visitIndex].orders.toMutableList()

                if(newOrderList.contains(order)) {
                    // If the order already exists, return the current state
                    return@update currentState
                } else {
                    // If the order is a new order, add it to the list
                    newOrderList.add(order)
                }

                // Update the visit with the new order
                // And sort the orders list by rating, then by name
                val newVisit = visit.copy(
                    orders = newOrderList.toList().sortedWith(compareBy(Order::rating, Order::name))
                )
                newVisitList[visitIndex] = newVisit
                newRestaurantList[restaurantIndex] = restaurant.copy(
                    visits = newVisitList.toList()
                )

                currentState.copy(
                    restaurants = newRestaurantList,
                    selectedRestaurant = newRestaurantList[restaurantIndex],
                    selectedVisit = newVisitList[visitIndex]
                )
            }
        }

        fun removeOrder(order: Order) {
            _uiState.update { currentState ->
                currentState.selectedVisit?.let { currentVisit ->
                    // Get the index of the current selected restaurant
                    val visitIndex = currentState.selectedRestaurant!!.visits.indexOf(currentVisit)

                    // Check if the visit index is valid and if the order exists in the visit
                    if(visitIndex != -1 && currentVisit.orders.contains(order)) {
                        // Get the list of orders and remove the given order
                        val newOrderList = currentVisit.orders.toMutableList()
                        newOrderList.remove(order)

                        // Create a new visit with the updated order list
                        val newVisit = currentVisit.copy(
                            orders = newOrderList.toList()
                        )

                        // Get the list of all visits and update it with the new visit
                        val newVisitList = currentState.selectedRestaurant.visits.toMutableList()
                        newVisitList[visitIndex] = newVisit

                        // Update the restaurants list and selected restaurant
                        currentState.copy(
                            selectedVisit = newVisit
                        )
                    } else {
                        currentState
                    }
                } ?: currentState
            }
        }
    }
}