package com.uvg.mypokedex.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Utilidades para manejo de red
 */
object NetworkUtils {
    
    /**
     * Verifica si hay conexión a Internet disponible
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
