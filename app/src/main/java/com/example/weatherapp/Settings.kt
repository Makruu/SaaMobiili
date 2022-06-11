package com.example.weatherapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager


//TURHA VIELÄ MUTTA TEIN DARKMODEN OPTIOIMINTIA VARTEN JOS JÄÄ AIKAA
object Settings {

    private const val THEME = "theme"
    private const val THEME_LIGHT = "light"
    private const val THEME_DARK = "dark"
    private const val THEME_DEFAULT = "default"

    fun getNightMode(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return when (prefs.getString(THEME, THEME_DEFAULT))
        {
            THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }
    }
}