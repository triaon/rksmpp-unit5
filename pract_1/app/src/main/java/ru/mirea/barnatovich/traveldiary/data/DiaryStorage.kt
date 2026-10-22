package ru.mirea.barnatovich.traveldiary.data

import android.content.Context
import java.io.File

class DiaryStorage(context: Context) {

    private val dir: File = context.filesDir

    // Полное сканирование папки, вызывается один раз при старте
    fun loadAll(): List<DiaryEntry> {
        val files = dir.listFiles { file -> file.isFile && file.name.endsWith(".txt") } ?: return emptyList()
        return files.mapNotNull { readEntry(it) }.sortedByDescending { it.timestamp }
    }

    fun create(title: String, text: String): DiaryEntry {
        val timestamp = System.currentTimeMillis()
        val file = File(dir, buildFileName(timestamp, title))
        file.writeText(text)
        return DiaryEntry(file.name, timestamp, title.trim(), text)
    }

    fun update(old: DiaryEntry, title: String, text: String): DiaryEntry {
        val newName = buildFileName(old.timestamp, title)
        if (newName != old.fileName) {
            File(dir, old.fileName).renameTo(File(dir, newName))
        }
        File(dir, newName).writeText(text)
        return old.copy(fileName = newName, title = title.trim(), text = text)
    }

    fun delete(fileName: String): Boolean = File(dir, fileName).delete()

    private fun readEntry(file: File): DiaryEntry? {
        // имя файла: timestamp_заголовок.txt или timestamp.txt
        val base = file.name.removeSuffix(".txt")
        val timestamp = base.substringBefore("_").toLongOrNull() ?: return null
        val title = if (base.contains("_")) base.substringAfter("_").replace('_', ' ') else ""
        return DiaryEntry(file.name, timestamp, title, file.readText())
    }

    private fun buildFileName(timestamp: Long, title: String): String {
        // убираем символы, которые нельзя использовать в имени файла
        val safeTitle = title.trim()
            .replace(Regex("[\\\\/:*?\"<>|]"), "")
            .replace(' ', '_')
            .take(40)
        return if (safeTitle.isEmpty()) "$timestamp.txt" else "${timestamp}_$safeTitle.txt"
    }
}
