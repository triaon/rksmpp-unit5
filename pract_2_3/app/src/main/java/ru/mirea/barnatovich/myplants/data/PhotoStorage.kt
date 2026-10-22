package ru.mirea.barnatovich.myplants.data

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoStorage(private val context: Context) {

    // приватная папка приложения: Android/data/<package>/files/Pictures
    private val picturesDir: File
        get() = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

    fun scan(): List<File> {
        val files = picturesDir.listFiles { f -> f.isFile && f.name.endsWith(".jpg") && f.length() > 0 }
        return files?.sortedByDescending { it.name } ?: emptyList()
    }

    fun newPhotoFile(): File {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(picturesDir, "IMG_$time.jpg")
    }

    fun delete(file: File): Boolean = file.delete()

    // копия фото в общую галерею через MediaStore
    fun exportToGallery(file: File): Boolean {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyPlants")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val uri = resolver.insert(collection, values) ?: return false
        return try {
            resolver.openOutputStream(uri)?.use { out ->
                file.inputStream().use { it.copyTo(out) }
            } ?: return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }
            true
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            false
        }
    }
}
