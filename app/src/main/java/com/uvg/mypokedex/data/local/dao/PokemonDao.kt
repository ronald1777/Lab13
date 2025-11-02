package com.uvg.mypokedex.data.local.dao

import androidx.room.*
import com.uvg.mypokedex.data.local.entity.CachedPokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<CachedPokemon>>

    @Query("SELECT * FROM cached_pokemon WHERE id = :pokemonId")
    suspend fun getPokemonById(pokemonId: Int): CachedPokemon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: CachedPokemon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemon: List<CachedPokemon>)

    @Delete
    suspend fun deletePokemon(pokemon: CachedPokemon)

    @Query("DELETE FROM cached_pokemon")
    suspend fun deleteAllPokemon()

    @Query("SELECT COUNT(*) FROM cached_pokemon")
    suspend fun getPokemonCount(): Int

    @Query("SELECT * FROM cached_pokemon WHERE name LIKE :name LIMIT 1")
    suspend fun searchPokemonByName(name: String): CachedPokemon?

    @Query("SELECT * FROM cached_pokemon WHERE lastFetchedAt < :timestamp")
    suspend fun getStaleCache(timestamp: Long): List<CachedPokemon>
}
