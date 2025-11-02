package com.uvg.mypokedex.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import kotlin.random.Random

@Composable
fun StablePokemonList(pokemons: List<String>) {
    val randomColor = remember {
        String.format("#%06x", Random.nextInt(0, 0xFFFFFF))
    }

    SideEffect {
        println("Composing StablePokemonList with color $randomColor")
    }

    Button(onClick = { }) {
        Text(text = "Tengo ${pokemons.size} Pokémon favoritos")
    }
}
/*IA utilizada para entender porque el button debía estar vacía
*/