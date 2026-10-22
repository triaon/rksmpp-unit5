package ru.mirea.barnatovich.shoppinglist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.mirea.barnatovich.shoppinglist.ShoppingApp
import ru.mirea.barnatovich.shoppinglist.data.Purchase
import ru.mirea.barnatovich.shoppinglist.data.PurchaseRepository
import ru.mirea.barnatovich.shoppinglist.data.SettingsStore

class ShoppingViewModel(
    private val repository: PurchaseRepository,
    private val settings: SettingsStore
) : ViewModel() {

    val purchases: StateFlow<List<Purchase>> =
        repository.purchases.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val highlightBought: StateFlow<Boolean> =
        settings.highlightBought.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch { repository.importFromJsonIfNeeded() }
    }

    fun purchase(id: Long): Flow<Purchase?> = repository.purchase(id)

    fun add(name: String, quantity: String, note: String) {
        viewModelScope.launch {
            repository.add(Purchase(name = name.trim(), quantity = quantity.trim(), note = note.trim()))
        }
    }

    fun update(purchase: Purchase) {
        viewModelScope.launch { repository.update(purchase) }
    }

    fun toggleBought(purchase: Purchase) = update(purchase.copy(isBought = !purchase.isBought))

    fun delete(purchase: Purchase) {
        viewModelScope.launch { repository.delete(purchase) }
    }

    fun setHighlightBought(value: Boolean) {
        viewModelScope.launch { settings.setHighlightBought(value) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ShoppingApp
                ShoppingViewModel(app.repository, app.settings)
            }
        }
    }
}
