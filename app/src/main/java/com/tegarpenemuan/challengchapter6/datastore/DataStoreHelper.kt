package com.tegarpenemuan.challengchapter6.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

fun Context.pref(): DataStoreManager {
    return DataStoreManager(this)
}

class DataStoreManager(
    private val context: Context
) {
    companion object {
        private const val PREFS_SETTINGS = "sharedprefkotlin12345"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PREFS_SETTINGS,
        )
        private fun sharedPreferencesMigration(context: Context) =
            listOf(SharedPreferencesMigration(context, PREFS_SETTINGS))

        private val PREF_IS_LOGIN = booleanPreferencesKey("PREF_IS_LOGIN")
        private val PREF_USERNAME = stringPreferencesKey("PREF_USERNAME")
        private val PREF_PASSWORD = stringPreferencesKey("PREF_PASSWORD")
        private val PREF_EMAIL = stringPreferencesKey("PREF_EMAIL")
    }

    fun setPrefLogin(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.setValue(PREF_IS_LOGIN, value)
        }
    }

    fun setPrefUsername(value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.setValue(PREF_USERNAME, value)
        }
    }

    fun setPrefEmail(value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.setValue(PREF_EMAIL, value)
        }
    }

    fun setPrefPassword(value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.setValue(PREF_PASSWORD, value)
        }
    }

    fun getPrefLogin() = runBlocking { context.dataStore.getValue(PREF_IS_LOGIN, false).firstOrNull() }
    fun getPrefUsername() = runBlocking { context.dataStore.getValue(PREF_USERNAME, "").firstOrNull() }
    fun getPrefEmail() = runBlocking { context.dataStore.getValue(PREF_EMAIL, "").firstOrNull() }
    fun getPrefPassword() = runBlocking { context.dataStore.getValue(PREF_PASSWORD, "").firstOrNull() }

    fun clearPref() = runBlocking { context.dataStore.clear() }
    fun removePrefLogin() = runBlocking { context.dataStore.removeValue(PREF_IS_LOGIN) }
    fun removePrefEmail() = runBlocking { context.dataStore.removeValue(PREF_EMAIL) }
    fun removePrefUsername() = runBlocking { context.dataStore.removeValue(PREF_USERNAME) }
    fun removePrefPassword() = runBlocking { context.dataStore.removeValue(PREF_PASSWORD) }
}

fun <T> DataStore<Preferences>.getValue(
    key: Preferences.Key<T>,
    defaultValue: T
) = this.data
    .catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    this.edit { preferences ->
        preferences[key] = value
    }
}

suspend fun <T> DataStore<Preferences>.removeValue(key: Preferences.Key<T>) {
    this.edit { preferences ->
        preferences.remove(key)
    }
}

suspend fun DataStore<Preferences>.clear() {
    this.edit { preferences ->
        preferences.clear()
    }
}
