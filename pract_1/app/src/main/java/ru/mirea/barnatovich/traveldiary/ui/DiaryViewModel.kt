package ru.mirea.barnatovich.traveldiary.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mirea.barnatovich.traveldiary.data.DiaryEntry
import ru.mirea.barnatovich.traveldiary.data.DiaryStorage

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = DiaryStorage(application)

    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // единственное полное сканирование папки
        viewModelScope.launch {
            _entries.value = withContext(Dispatchers.IO) { storage.loadAll() }
            _isLoading.value = false
        }
    }

    fun save(editing: DiaryEntry?, title: String, text: String) {
        viewModelScope.launch {
            if (editing == null) {
                val created = withContext(Dispatchers.IO) { storage.create(title, text) }
                // новая запись вставляется в начало списка
                _entries.update { listOf(created) + it }
            } else {
                val updated = withContext(Dispatchers.IO) { storage.update(editing, title, text) }
                _entries.update { list ->
                    list.map { if (it.fileName == editing.fileName) updated else it }
                }
            }
        }
    }

    fun delete(fileName: String) {
        viewModelScope.launch {
            val deleted = withContext(Dispatchers.IO) { storage.delete(fileName) }
            if (deleted) {
                _entries.update { list -> list.filterNot { it.fileName == fileName } }
            }
        }
    }
}
