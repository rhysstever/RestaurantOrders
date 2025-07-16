package com.rhysstever.restaurantorders.ui

import android.util.Log
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

        fun addVisit(newVisit: Visit) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    val newVisitList = selectedRestaurant.visits.toMutableList()
                    newVisitList.add(newVisit)

                    // Update the restaurant with the new visit
                    // And sort the visits list by rating, then by name
                    val newRestaurant = selectedRestaurant.copy(
                        visits = newVisitList.toList().sortedWith(compareBy(Visit::rating, Visit::name))
                    )

                    // Create a new list of restaurants with the updated restaurant
                    val restaurantsList = currentState.restaurants.toMutableList()
                    restaurantsList.remove(selectedRestaurant)
                    restaurantsList.add(newRestaurant)

                    currentState.copy(
                        restaurants = restaurantsList,
                        selectedRestaurant = newRestaurant
                    )
                } ?: run {
                    Log.v("RAS Error", "Error adding new Visit. No restaurant selected.")
                    return@update currentState
                }
            }
        }

        fun editSelectedVisit(updatedVisit: Visit) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    currentState.selectedVisit?.let { selectedVisit ->
                        // Create a new list of visits with the updated visit, removing the (now) old visit
                        val visitsList = selectedRestaurant.visits.toMutableList()
                        visitsList.remove(selectedVisit)
                        visitsList.add(updatedVisit)

                        // Create a new restaurant with the updated visits list
                        val newRestaurant = selectedRestaurant.copy(
                            visits = visitsList.toList().sortedWith(compareBy(Visit::rating, Visit::name))
                        )

                        // Create a new list of restaurants with the updated restaurant
                        val restaurantsList = currentState.restaurants.toMutableList()
                        restaurantsList.remove(selectedRestaurant)
                        restaurantsList.add(newRestaurant)

                        currentState.copy(
                            restaurants = restaurantsList.toList().sortedBy { it.name.lowercase(Locale.ROOT) },
                            selectedRestaurant = newRestaurant,
                            selectedVisit = updatedVisit
                        )
                    } ?: run {
                        Log.v("RAS Error", "Error editing Visit. No visit selected.")
                        return@update currentState
                    }
                } ?: run {
                    Log.v("RAS Error", "Error editing Visit. No restaurant selected.")
                    return@update currentState
                }
            }
        }

        fun removeVisit(visit: Visit) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    // Get the list of visits and remove the given visit
                    val newVisitList = currentState.selectedRestaurant.visits.toMutableList()
                    newVisitList.remove(visit)

                    // Create a new restaurant with the updated order list
                    val newRestaurant = currentState.selectedRestaurant.copy(
                        visits = newVisitList.toList()
                    )

                    // Create a new list of restaurants with the updated restaurant
                    val restaurantsList = currentState.restaurants.toMutableList()
                    restaurantsList.remove(selectedRestaurant)
                    restaurantsList.add(newRestaurant)

                    // Update the restaurants list and selected restaurant
                    // Deselect the visit if it was the selected visit
                    currentState.copy(
                        restaurants = restaurantsList,
                        selectedRestaurant = newRestaurant,
                        selectedVisit = if(currentState.selectedVisit == visit) {
                            null
                        } else {
                            currentState.selectedVisit
                        }
                    )
                } ?: run {
                    Log.v("RAS Error", "Error Removing Visit. No restaurant selected.")
                    return@update currentState
                }
            }
        }

        fun selectVisit(visit: Visit?) {
            _uiState.update { currentState ->
                visit?.let {
                    if(currentState.selectedRestaurant != null
                        && currentState.selectedRestaurant.visits.contains(visit)) {
                        // Set the given restaurant as the selected restaurant
                        currentState.copy(selectedVisit = visit)
                    } else {
                        currentState.copy(selectedVisit = null)
                    }
                } ?: currentState.copy(selectedVisit = null)
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

        fun addNewOrder(order: Order) {
            _uiState.update { currentState ->
                currentState.selectedRestaurant?.let { selectedRestaurant ->
                    currentState.selectedVisit?.let { selectedVisit ->
                        val newOrderList = selectedVisit.orders.toMutableList()

                        if(newOrderList.contains(order)) {
                            // If the order already exists, return the current state
                            Log.v("RAS Error", "Error Adding new Order. New order already exists: ${selectedVisit.orders}")
                            return@update currentState
                        } else {
                            // If the order is a new order, add it to the list
                            newOrderList.add(order)
                        }

                        // Update the visit with the new order
                        // And sort the orders list by rating, then by name
                        val newVisit = selectedVisit.copy(
                            orders = newOrderList.toList().sortedWith(compareBy(Order::rating, Order::name))
                        )
                        val newVisitList = selectedRestaurant.visits.toMutableList()
                        newVisitList.remove(selectedVisit)
                        newVisitList.add(newVisit)

                        val newRestaurant = selectedRestaurant.copy(
                            visits = newVisitList
                        )

                        val newRestaurantList = currentState.restaurants.toMutableList()
                        newRestaurantList.remove(selectedRestaurant)
                        newRestaurantList.add(newRestaurant)

                        currentState.copy(
                            restaurants = newRestaurantList,
                            selectedRestaurant = newRestaurant,
                            selectedVisit = newVisit
                        )
                    } ?: run {
                        Log.v("RAS Error", "Error Adding new Order. No visit selected.")
                        return@update currentState
                    }
                } ?: run {
                    Log.v("RAS Error", "Error Adding new Order. No restaurant selected.")
                    return@update currentState
                }
            }
        }

        fun removeOrder(order: Order) {
            _uiState.update { currentState ->
                if(currentState.selectedRestaurant == null
                    || currentState.selectedVisit == null
                    || !currentState.selectedVisit.orders.contains(order)) {
                    // Exit early if there is no selected restaurant or visit
                    // Also exit early if the selected visit does not contain the order to remove
                    Log.v("RAS Error", "Error Removing Order. Restaurant selection: ${currentState.selectedRestaurant}. Visit Selection: ${currentState.selectedVisit}. If both have a value, then the visit does not contain the order to remove")
                    return@update currentState
                }

                // Get the list of orders and remove the given order
                val newOrderList = currentState.selectedVisit.orders.toMutableList()
                newOrderList.remove(order)

                // Create a new visit with the updated order list
                val newVisit = currentState.selectedVisit.copy(
                    orders = newOrderList.toList()
                )
                // Get the list of visits and replace the selected visit with the new visit
                val newVisitList = currentState.selectedRestaurant.visits.toMutableList()
                newVisitList[newVisitList.indexOf(currentState.selectedVisit)] = newVisit

                // Create a new restaurant with the updated order list
                val newRestaurant = currentState.selectedRestaurant.copy(
                    visits = newVisitList.toList()
                )

                // Get the list of all restaurants and update it with the new restaurant
                val newRestaurantList = currentState.restaurants.toMutableList()
                newRestaurantList[currentState.restaurants.indexOf(currentState.selectedRestaurant)] = newRestaurant

                // Update the restaurants list and selected restaurant
                currentState.copy(
                    restaurants = newRestaurantList,
                    selectedRestaurant = newRestaurant,
                    selectedVisit = newVisit
                )
            }
        }
    }
}