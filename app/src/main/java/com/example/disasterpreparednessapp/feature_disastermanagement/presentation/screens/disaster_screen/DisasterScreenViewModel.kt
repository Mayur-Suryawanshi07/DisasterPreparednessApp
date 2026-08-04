package com.example.disasterpreparednessapp.feature_disastermanagement.presentation.screens.disaster_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Change these imports:
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.GetDisaster // Corrected import
import com.example.disasterpreparednessapp.feature_disastermanagement.domain.use_cases.SearchDisaster // Corrected import
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisasterScreenViewModel @Inject constructor(
    // Inject specific Sachet use cases
    private val getDisaster: GetDisaster, // Changed
    private val searchDisaster: SearchDisaster // Changed
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisasterScreenState())
    val uiState: StateFlow<DisasterScreenState> = _uiState.asStateFlow()

    init {
        loadDisasterEvents()
    }

    fun loadDisasterEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(uiState = DisasterScreenUiState.Loading, isRefreshing = true) } // Indicate loading/refreshing

            try {
                // Use the injected GetSachetUseCase directly
                getDisaster().collect { events -> // Changed: directly invoke the use case
                    val newUiState = if (events.isEmpty()) {
                        DisasterScreenUiState.Empty
                    } else {
                        DisasterScreenUiState.Success(events)
                    }
                    _uiState.update { it.copy(uiState = newUiState, isRefreshing = false) } // Stop refreshing
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(uiState = DisasterScreenUiState.Error(e.message ?: "Unknown error occurred"), isRefreshing = false) // Stop refreshing
                }
            }
        }
    }

    fun searchDisasterEvents(query: String) {
        if (query.isBlank()) {
            loadDisasterEvents() // If search query is blank, load all events
            _uiState.update { it.copy(searchQuery = "") } // Clear search query in state
            return
        }
         if (query.length < 2) { // Keep your validation
            _uiState.update {
                it.copy(uiState = DisasterScreenUiState.Error("Search query must be at least 2 characters"))
            }
            return
        }


        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchQuery = query,
                    uiState = DisasterScreenUiState.Loading,
                    isRefreshing = true // Indicate loading/refreshing
                )
            }

            try {
                // Use the injected SearchSachetUseCase directly
                searchDisaster(query).collect { events -> // Changed: directly invoke the use case
                    val newUiState = if (events.isEmpty()) {
                        // Consider a different state for "no search results" vs "general empty"
                        DisasterScreenUiState.Success(emptyList()) // Or a specific NoSearchResults state
                    } else {
                        DisasterScreenUiState.Success(events)
                    }
                    _uiState.update { it.copy(uiState = newUiState, isRefreshing = false) } // Stop refreshing
                }
            } catch (e: IllegalArgumentException) { // Catch specific exception from use case
                 _uiState.update {
                    it.copy(uiState = DisasterScreenUiState.Error(e.message ?: "Invalid search query"), isRefreshing = false) // Stop refreshing
                }
            }
            catch (e: Exception) {
                _uiState.update {
                    it.copy(uiState = DisasterScreenUiState.Error(e.message ?: "Search failed"), isRefreshing = false) // Stop refreshing
                }
            }
        }
    }

    fun refreshEvents() {
        _uiState.update { it.copy(searchQuery = "") } // Clear search query on refresh
        loadDisasterEvents() // This will set isRefreshing = true at the start and false at the end
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        loadDisasterEvents()
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategory = categoryId) }
        // TODO: Implement category filtering, this will likely involve another use case and data loading
    }

    fun retry() {
         _uiState.update { it.copy(searchQuery = "") } // Clear search query on retry as well
        loadDisasterEvents()
    }
}