package ru.mirea.barnatovich.shoppinglist.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsStore(private val context: Context) {

    private val highlightKey = booleanPreferencesKey("highlight_bought")
    private val importedKey = booleanPreferencesKey("json_imported")

    val highlightBought: Flow<Boolean> = context.dataStore.data.map { it[highlightKey] ?: false }

    suspend fun setHighlightBought(value: Boolean) {
        context.dataStore.edit { it[highlightKey] = value }
    }

    suspend fun isImported(): Boolean = context.dataStore.data.first()[importedKey] ?: false

    suspend fun setImported() {
        context.dataStore.edit { it[importedKey] = true }
    }
}
