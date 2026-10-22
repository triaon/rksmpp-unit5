package ru.mirea.barnatovich.myplants.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mirea.barnatovich.myplants.data.PhotoStorage
import java.io.File

class PlantsViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = PhotoStorage(application)

    private val _photos = MutableStateFlow<List<File>>(emptyList())
    val photos: StateFlow<List<File>> = _photos

    init {
        refresh()
    }

    // сканирование папки: при запуске и после нового фото
    fun refresh() {
        viewModelScope.launch {
            _photos.value = withContext(Dispatchers.IO) { storage.scan() }
        }
    }

    fun newPhotoFile(): File = storage.newPhotoFile()

    fun onPhotoTaken(file: File, success: Boolean) {
        if (!success) file.delete()
        refresh()
    }

    fun delete(file: File) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { storage.delete(file) }
            refresh()
        }
    }

    suspend fun export(file: File): Boolean =
        withContext(Dispatchers.IO) { storage.exportToGallery(file) }
}
