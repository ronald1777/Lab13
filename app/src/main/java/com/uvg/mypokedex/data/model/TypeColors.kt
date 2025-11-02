package com.uvg.mypokedex.data.model

import androidx.compose.ui.graphics.Color

object TypeColors {
    fun getColor(type: PokeType): Color {
        return when (type) {
            PokeType.NORMAL -> Color(0xFFA8A878)
            PokeType.FIRE -> Color(0xFFF08030)
            PokeType.WATER -> Color(0xFF6890F0)
            PokeType.ELECTRIC -> Color(0xFFF8D030)
            PokeType.GRASS -> Color(0xFF78C850)
            PokeType.ICE -> Color(0xFF98D8D8)
            PokeType.FIGHTING -> Color(0xFFC03028)
            PokeType.POISON -> Color(0xFFA040A0)
            PokeType.GROUND -> Color(0xFFE0C068)
            PokeType.FLYING -> Color(0xFFA890F0)
            PokeType.PSYCHIC -> Color(0xFFF85888)
            PokeType.BUG -> Color(0xFFA8B820)
            PokeType.ROCK -> Color(0xFFB8A038)
            PokeType.GHOST -> Color(0xFF705898)
            PokeType.DRAGON -> Color(0xFF7038F8)
            PokeType.DARK -> Color(0xFF705848)
            PokeType.STEEL -> Color(0xFFB8B8D0)
            PokeType.FAIRY -> Color(0xFFEE99AC)
            PokeType.UNKNOWN -> Color(0xFF68A090)
        }
    }

    fun getBackgroundColor(type: PokeType): Color {
        return getColor(type).copy(alpha = 0.1f)
    }
}