package com.uvg.mypokedex.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uvg.mypokedex.data.local.dao.PokemonDao
import com.uvg.mypokedex.data.local.entity.CachedPokemon

/**
 * Base de datos principal de Room para la aplicación
 */
@Database(
    entities = [CachedPokemon::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {

    /**
     * DAO para operaciones de Pokémon
     */
    abstract fun pokemonDao(): PokemonDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        private const val DATABASE_NAME = "pokemon_database"

        /**
         * Obtiene la instancia única de la base de datos (Singleton)
         */
        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
