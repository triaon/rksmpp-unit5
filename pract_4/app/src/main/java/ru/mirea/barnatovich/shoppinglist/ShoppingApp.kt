package ru.mirea.barnatovich.shoppinglist

import android.app.Application
import ru.mirea.barnatovich.shoppinglist.data.AppDatabase
import ru.mirea.barnatovich.shoppinglist.data.PurchaseRepository
import ru.mirea.barnatovich.shoppinglist.data.SettingsStore

// простой ручной DI: зависимости создаются один раз на всё приложение
class ShoppingApp : Application() {
    val settings by lazy { SettingsStore(this) }
    val repository by lazy { PurchaseRepository(this, AppDatabase.create(this).purchaseDao(), settings) }
}
