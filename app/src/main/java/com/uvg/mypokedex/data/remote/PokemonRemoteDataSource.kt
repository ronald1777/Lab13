package com.uvg.mypokedex.data.remote

import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.remote.dto.RetrofitInstance
import com.uvg.mypokedex.data.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class PokemonRemoteDataSource(
    private val apiService: PokeApiService = RetrofitInstance.api
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonList(limit, offset)

                if (response.isSuccessful) {
                    val pokemonList = response.body()?.results ?: emptyList()

                    val pokemonDetails = pokemonList.map { entry ->
                        async {
                            getPokemonById(entry.id).getOrNull()
                        }
                    }.awaitAll().filterNotNull()

                    Result.success(pokemonDetails)
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getPokemonById(id: Int): Result<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonById(id)

                if (response.isSuccessful) {
                    val pokemonDto = response.body()
                    if (pokemonDto != null) {
                        Result.success(pokemonDto.toDomain())
                    } else {
                        Result.failure(Exception("Pokemon not found"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun searchPokemonByName(name: String): Result<Pokemon> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPokemonByName(name.lowercase())

                if (response.isSuccessful) {
                    val pokemonDto = response.body()
                    if (pokemonDto != null) {
                        Result.success(pokemonDto.toDomain())
                    } else {
                        Result.failure(Exception("Pokemon not found"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}