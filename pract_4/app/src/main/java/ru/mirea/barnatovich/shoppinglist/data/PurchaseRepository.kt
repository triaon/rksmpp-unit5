package ru.mirea.barnatovich.shoppinglist.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class PurchaseJson(
    val name: String,
    val quantity: String = "",
    val note: String = "",
    val bought: Boolean = false
)

class PurchaseRepository(
    private val context: Context,
    private val dao: PurchaseDao,
    private val settings: SettingsStore
) {
    val purchases: Flow<List<Purchase>> = dao.getAll()

    fun purchase(id: Long): Flow<Purchase?> = dao.getById(id)

    // разовый импорт из assets/purchases.json при первом запуске
    suspend fun importFromJsonIfNeeded() = withContext(Dispatchers.IO) {
        if (settings.isImported()) return@withContext
        val text = context.assets.open("purchases.json").bufferedReader().use { it.readText() }
        val items = Json.decodeFromString<List<PurchaseJson>>(text)
        dao.insertAll(items.map { Purchase(name = it.name, quantity = it.quantity, note = it.note, isBought = it.bought) })
        settings.setImported()
    }

    suspend fun add(purchase: Purchase) = dao.insert(purchase)

    suspend fun update(purchase: Purchase) = dao.update(purchase)

    suspend fun delete(purchase: Purchase) = dao.delete(purchase)
}
