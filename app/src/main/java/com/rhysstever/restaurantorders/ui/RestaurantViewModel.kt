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
                        isNewOrderInputInvalid = currentState.selectedRestaurant!!.orders
                            .map { it.name }.contains(orderName)
                    )
                }
            }
        }

        fun addNewOrder(restaurant: Restaurant, order: Order) {
            _uiState.update { currentState ->
                val restaurantIndex = currentState.restaurants.indexOf(restaurant)
                if(restaurantIndex != -1) {
                    val newRestaurantList = currentState.restaurants.toMutableList()
                    val newOrderList = currentState.restaurants[restaurantIndex].orders.toMutableList()

                    if(newOrderList.contains(order)) {
                        // If the order already exists, return the current state
                        return@update currentState
                    } else {
                        // If the order is a new order, add it to the list
                        newOrderList.add(order)
                    }

                    // Update the restaurant with the new order
                    // And sort the orders list by rating, then by name
                    val newRestaurant = restaurant.copy(
                        orders = newOrderList.toList().sortedWith(compareBy(Order::rating, Order::name))
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

        fun removeOrder(order: Order) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { currentRestaurant ->
                    // Get the index of the current selected restaurant
                    val restaurantIndex = currentState.restaurants.indexOf(currentRestaurant)

                    // Check if the restaurant index is valid and if the order exists in the restaurant
                    if(restaurantIndex != -1 && currentRestaurant.orders.contains(order)) {
                        // Get the list of orders and remove the given order
                        val newOrderList = currentRestaurant.orders.toMutableList()
                        newOrderList.remove(order)

                        // Create a new restaurant with the updated order list
                        val newRestaurant = currentRestaurant.copy(
                            orders = newOrderList.toList()
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
    }
}