package com.uvg.mypokedex.data.remote

import com.uvg.mypokedex.data.remote.dto.PokemonDetailDto
import com.uvg.mypokedex.data.remote.dto.PokemonListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonListDto>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(
        @Path("id") id: Int
    ): Response<PokemonDetailDto>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(
        @Path("name") name: String
    ): Response<PokemonDetailDto>
}