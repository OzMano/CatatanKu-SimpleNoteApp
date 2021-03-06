package id.julham.catatanku.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class AppThemeMode(private val isDarkModeOn: Boolean, context: Context) {

    private val appSettingPrefs = context.getSharedPreferences("AppThemeModePrefs", 0)
    private val sharedPrefsEdit = appSettingPrefs.edit()

    fun setTheme() {
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefsEdit.putBoolean("DarkMode", true)
            sharedPrefsEdit.apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefsEdit.putBoolean("DarkMode", false)
            sharedPrefsEdit.apply()
        }

    }
}