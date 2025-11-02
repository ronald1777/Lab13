package com.uvg.mypokedex.data.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokeType>,
    val height: Int,
    val weight: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {
    val formattedId: String
        get() = "#${id.toString().padStart(3, '0')}"

    val heightInMeters: Float
        get() = height / 10f

    val weightInKg: Float
        get() = weight / 10f

    val primaryType: PokeType
        get() = types.firstOrNull() ?: PokeType.UNKNOWN

    val maxStat: Int
        get() = maxOf(hp, attack, defense, specialAttack, specialDefense, speed)
}
