package com.uvg.mypokedex.data.repository

import android.content.Context
import com.uvg.mypokedex.data.connectivity.ConnectivityObserver
import com.uvg.mypokedex.data.connectivity.NetworkConnectivityObserver
import com.uvg.mypokedex.data.datastore.UserPreferences
import com.uvg.mypokedex.data.local.database.PokemonDatabase
import com.uvg.mypokedex.data.local.entity.toCache
import com.uvg.mypokedex.data.local.entity.toDomain
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.PokemonRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PokemonRepository(
    private val context: Context,
    private val remoteDataSource: PokemonRemoteDataSource = PokemonRemoteDataSource(),
    private val connectivityObserver: ConnectivityObserver = NetworkConnectivityObserver(context)
) {

    private val database = PokemonDatabase.getDatabase(context)
    private val pokemonDao = database.pokemonDao()
    private val userPreferences = UserPreferences(context)

    val isConnected: Flow<Boolean> = connectivityObserver.observe().map { status ->
        status == ConnectivityObserver.Status.Available
    }

    fun getSortPreferences(): Flow<Pair<String, Boolean>> {
        return combine(
            userPreferences.sortType,
            userPreferences.isAscending
        ) { sortType, isAscending ->
            Pair(sortType, isAscending)
        }
    }

    suspend fun saveSortPreferences(sortType: String, isAscending: Boolean) {
        userPreferences.saveSortPreferences(sortType, isAscending)
    }

    fun getPokemonList(limit: Int = 20, offset: Int = 0): Flow<UiState<List<Pokemon>>> = flow {
        emit(UiState.Loading)

        try {
            val cachedCount = pokemonDao.getPokemonCount()

            if (cachedCount > 0) {
                pokemonDao.getAllPokemon().collect { cachedPokemon ->
                    val domainList = cachedPokemon.map { it.toDomain() }
                    if (domainList.isNotEmpty()) {
                        emit(UiState.Success(domainList))
                    }
                }
            }

            if (connectivityObserver.isConnected()) {
                val result = remoteDataSource.getPokemonList(limit, offset)

                result.fold(
                    onSuccess = { pokemonList ->
                        val cachedList = pokemonList.map { it.toCache() }
                        pokemonDao.insertAllPokemon(cachedList)

                        if (pokemonList.isNotEmpty()) {
                            emit(UiState.Success(pokemonList))
                        } else {
                            emit(UiState.Empty)
                        }
                    },
                    onFailure = { exception ->
                        if (cachedCount == 0) {
                            emit(UiState.Error(exception.message ?: "Error desconocido"))
                        }
                    }
                )
            } else if (cachedCount == 0) {
                emit(UiState.Error("No hay conexión a internet y no hay datos en caché"))
            }

        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getPokemonById(id: Int): Flow<UiState<Pokemon>> = flow {
        emit(UiState.Loading)

        try {
            val cachedPokemon = pokemonDao.getPokemonById(id)

            if (cachedPokemon != null) {
                emit(UiState.Success(cachedPokemon.toDomain()))
            }

            if (connectivityObserver.isConnected()) {
                val result = remoteDataSource.getPokemonById(id)

                result.fold(
                    onSuccess = { pokemon ->
                        pokemonDao.insertPokemon(pokemon.toCache())
                        emit(UiState.Success(pokemon))
                    },
                    onFailure = { exception ->
                        if (cachedPokemon == null) {
                            emit(UiState.Error(exception.message ?: "Error desconocido"))
                        }
                    }
                )
            } else if (cachedPokemon == null) {
                emit(UiState.Error("No hay conexión a internet y no hay datos en caché"))
            }

        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error desconocido"))
        }
    }

    fun searchPokemonByName(name: String): Flow<UiState<Pokemon>> = flow {
        emit(UiState.Loading)

        try {
            val cachedPokemon = pokemonDao.searchPokemonByName(name.lowercase())

            if (cachedPokemon != null) {
                emit(UiState.Success(cachedPokemon.toDomain()))
            }

            if (connectivityObserver.isConnected()) {
                val result = remoteDataSource.searchPokemonByName(name)

                result.fold(
                    onSuccess = { pokemon ->
                        pokemonDao.insertPokemon(pokemon.toCache())
                        emit(UiState.Success(pokemon))
                    },
                    onFailure = { exception ->
                        if (cachedPokemon == null) {
                            emit(UiState.Error(exception.message ?: "Pokémon no encontrado"))
                        }
                    }
                )
            } else if (cachedPokemon == null) {
                emit(UiState.Error("No hay conexión a internet y no hay datos en caché"))
            }

        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error al buscar"))
        }
    }

    suspend fun forceSync(): Result<Unit> {
        return if (connectivityObserver.isConnected()) {
            try {
                val result = remoteDataSource.getPokemonList(limit = 100, offset = 0)
                result.fold(
                    onSuccess = { pokemonList ->
                        val cachedList = pokemonList.map { it.toCache() }
                        pokemonDao.insertAllPokemon(cachedList)
                        Result.success(Unit)
                    },
                    onFailure = { exception ->
                        Result.failure(exception)
                    }
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("No hay conexión a internet"))
        }
    }

    suspend fun clearCache() {
        pokemonDao.deleteAllPokemon()
    }
}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
}
