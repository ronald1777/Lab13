package com.uvg.mypokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uvg.mypokedex.data.model.Pokemon
import com.uvg.mypokedex.data.model.PokemonStat

@Entity(tableName = "cached_pokemon")
@TypeConverters(Converters::class)
data class CachedPokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: List<String>,
    val weight: Float,
    val height: Float,
    val stats: List<PokemonStat>,
    val imageUrl: String,
    val lastFetchedAt: Long = System.currentTimeMillis()
)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStatsList(value: List<PokemonStat>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStatsList(value: String): List<PokemonStat> {
        val type = object : TypeToken<List<PokemonStat>>() {}.type
        return gson.fromJson(value, type)
    }
}

fun CachedPokemon.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        type = type,
        weight = weight,
        height = height,
        stats = stats,
        imageUrl = imageUrl
    )
}

fun Pokemon.toCache(): CachedPokemon {
    return CachedPokemon(
        id = id,
        name = name,
        type = type,
        weight = weight,
        height = height,
        stats = stats,
        imageUrl = imageUrl,
        lastFetchedAt = System.currentTimeMillis()
    )
}
