package com.uvg.mypokedex.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.repository.PokemonRepository
import com.uvg.mypokedex.data.repository.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = PokemonRepository(application.applicationContext)

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        viewModelScope.launch {
            repository.isConnected.collect { connected ->
                _isConnected.value = connected
            }
        }
    }

    fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            repository.getPokemonById(pokemonId).collect { result ->
                when (result) {
                    is UiState.Loading -> {
                        _uiState.value = DetailUiState.Loading
                    }
                    is UiState.Success -> {
                        _uiState.value = DetailUiState.Success(result.data)
                    }
                    is UiState.Error -> {
                        _uiState.value = DetailUiState.Error(result.message)
                    }
                    is UiState.Empty -> {
                        _uiState.value = DetailUiState.Error("Pokémon no encontrado")
                    }
                }
            }
        }
    }

    fun retry(pokemonId: Int) {
        loadPokemonDetail(pokemonId)
    }
}

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(val pokemon: Pokemon) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
