package ru.mirea.barnatovich.traveldiary.data

// Одна запись дневника = один файл в filesDir
data class DiaryEntry(
    val fileName: String,
    val timestamp: Long,
    val title: String,
    val text: String
)
