package ru.mirea.barnatovich.shoppinglist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.mirea.barnatovich.shoppinglist.data.Purchase

val BoughtColor = Color(0xFFB9F6CA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(viewModel: ShoppingViewModel, onOpen: (Long) -> Unit) {
    val purchases by viewModel.purchases.collectAsState()
    val highlight by viewModel.highlightBought.collectAsState()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список покупок") },
                actions = {
                    Text("Цвет купленных", style = MaterialTheme.typography.labelLarge)
                    Switch(
                        checked = highlight,
                        onCheckedChange = { viewModel.setHighlightBought(it) },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (purchases.isEmpty()) {
                Text("Список пуст", modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(purchases, key = { it.id }) { purchase ->
                    PurchaseItem(
                        purchase = purchase,
                        highlight = highlight,
                        onClick = { onOpen(purchase.id) },
                        onToggle = { viewModel.toggleBought(purchase) },
                        onDelete = { viewModel.delete(purchase) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        PurchaseDialog(
            initial = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, quantity, note ->
                viewModel.add(name, quantity, note)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun PurchaseItem(
    purchase: Purchase,
    highlight: Boolean,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    // фон купленных меняется, только если включён переключатель
    val colors = if (highlight && purchase.isBought) {
        CardDefaults.cardColors(containerColor = BoughtColor)
    } else {
        CardDefaults.cardColors()
    }
    Card(
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            Checkbox(checked = purchase.isBought, onCheckedChange = { onToggle() })
            Column(Modifier.weight(1f)) {
                Text(
                    purchase.name,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (purchase.isBought) TextDecoration.LineThrough else null
                )
                if (purchase.quantity.isNotBlank()) {
                    Text(purchase.quantity, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}
