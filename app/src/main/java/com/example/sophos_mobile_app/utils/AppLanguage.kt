package com.example.sophos_mobile_app.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class AppLanguage @Inject constructor() {
    //val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("xx-YY")
    // Fetching the current application locale using the AndroidX support Library
    private var _currentLocaleName = if (!AppCompatDelegate.getApplicationLocales().isEmpty) {
        // Fetches the current Application Locale from the list
        AppCompatDelegate.getApplicationLocales()[0]?.displayName
    } else {
        // Fetches the default System Locale
        Locale.getDefault().displayName
    }
     val currentLocaleName get() = _currentLocaleName

    private suspend fun setLanguage(language: String){
        withContext(Dispatchers.Main){
            val localeList = LocaleListCompat.forLanguageTags(language)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    suspend fun changeLanguage(){
        _currentLocaleName?.let {
            if ( "español" in it.lowercase()){
                setLanguage("en")
            }
            else{
                setLanguage("es")
            }
        }
    }

}