package com.rhysstever.restaurantorders.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class RestaurantViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RestaurantUIState())
    val uiState: StateFlow<RestaurantUIState> = _uiState.asStateFlow()

    var newRestaurantInput by mutableStateOf("")
        private set

    var newOrderInput by mutableStateOf("")
        private set

    fun toggleShowingFavorites() {
        _uiState.update { currentState ->
            currentState.copy(
                onlyShowFavorites = !currentState.onlyShowFavorites
            )
        }
    }

    fun updateNewRestaurantInput(newInput: String) {
        newRestaurantInput = newInput
        checkNewRestaurantInput()
    }

    fun checkNewRestaurantInput() {
        if(newRestaurantInput.isEmpty()) {
            // If there is no input, set the input validity to null
            _uiState.update { currentState ->
                currentState.copy(
                    isNewRestaurantInputInvalid = null
                )
            }
        } else if(newRestaurantInput.isBlank()) {
            // If the input is blank (full of whitespace), set the input to invalid
            _uiState.update { currentState ->
                currentState.copy(
                    isNewRestaurantInputInvalid = true
                )
            }
        } else {
            // If the input is already a restaurant name, the input is invalid
            _uiState.update { currentState ->
                currentState.copy(
                    isNewRestaurantInputInvalid = currentState.restaurants
                        .map { it.name }.contains(newRestaurantInput)
                )
            }
        }
    }

    fun addRestaurant() {
        val newRestaurant = Restaurant(newRestaurantInput)

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

    fun updateSelectedRestaurant(restaurant: Restaurant) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedRestaurant = restaurant
            )
        }
    }

    fun toggleRestaurantIsFavorite(restaurant: Restaurant) {
        _uiState.update { currentState ->
            val restaurantIndex = currentState.restaurants.indexOf(restaurant)
            if(restaurantIndex != -1) {
                val newList = currentState.restaurants.toMutableList()
                newList[restaurantIndex] = restaurant.copy(isFavorite = !restaurant.isFavorite)

                // Update the selected restaurant
                updateSelectedRestaurant(newList[restaurantIndex])

                currentState.copy(
                    restaurants = newList
                )
            } else {
                currentState
            }
        }
    }

    fun updateNewOrderInput(newInput: String) {
        newOrderInput = newInput
        checkNewOrderInput()
    }

    fun checkNewOrderInput() {
        if(newOrderInput.isEmpty()) {
            // If there is no input, set the input validity to null
            _uiState.update { currentState ->
                currentState.copy(
                    isNewOrderInputInvalid = null
                )
            }
        } else if(newOrderInput.isBlank()) {
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
                        .map { it.name }.contains(newRestaurantInput)
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

                // Update the selected restaurant
                updateSelectedRestaurant(newRestaurantList[restaurantIndex])

                currentState.copy(
                    restaurants = newRestaurantList
                )
            } else {
                currentState
            }
        }
    }
}