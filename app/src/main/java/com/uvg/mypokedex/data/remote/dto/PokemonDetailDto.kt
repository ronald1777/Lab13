package com.uvg.mypokedex.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.uvg.mypokedex.data.model.PokeType
import com.uvg.mypokedex.data.model.Pokemon

data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: SpritesDto,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("stats") val stats: List<StatDto>
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String?
)

data class TypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: TypeDto
)

data class TypeDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class StatDto(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: StatNameDto
)

data class StatNameDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

fun PokemonDetailDto.toDomain(): Pokemon {
    val imageUrl = sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
        ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"

    val typesList = types.map { PokeType.fromString(it.type.name) }

    val statsMap = stats.associate { it.stat.name to it.baseStat }

    return Pokemon(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = imageUrl,
        types = typesList,
        height = height,
        weight = weight,
        hp = statsMap["hp"] ?: 0,
        attack = statsMap["attack"] ?: 0,
        defense = statsMap["defense"] ?: 0,
        specialAttack = statsMap["special-attack"] ?: 0,
        specialDefense = statsMap["special-defense"] ?: 0,
        speed = statsMap["speed"] ?: 0
    )
}
