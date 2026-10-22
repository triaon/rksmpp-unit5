package ru.mirea.barnatovich.shoppinglist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(viewModel: ShoppingViewModel, purchaseId: Long, onBack: () -> Unit) {
    val purchaseFlow = remember(purchaseId) { viewModel.purchase(purchaseId) }
    val purchase by purchaseFlow.collectAsState(null)
    val highlight by viewModel.highlightBought.collectAsState()
    var showEditDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Покупка") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        val item = purchase ?: return@Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = if (highlight && item.isBought) CardDefaults.cardColors(containerColor = BoughtColor)
                else CardDefaults.cardColors(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.name, style = MaterialTheme.typography.headlineSmall)
                    Text("Количество: ${item.quantity.ifBlank { "не указано" }}")
                    Text("Заметка: ${item.note.ifBlank { "—" }}")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = item.isBought,
                            onCheckedChange = { viewModel.toggleBought(item) }
                        )
                        Text(if (item.isBought) "Куплено" else "Не куплено")
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { showEditDialog = true }, modifier = Modifier.weight(1f)) {
                    Text("Изменить")
                }
                OutlinedButton(
                    onClick = {
                        viewModel.delete(item)
                        onBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Удалить")
                }
            }
        }

        if (showEditDialog) {
            PurchaseDialog(
                initial = item,
                onDismiss = { showEditDialog = false },
                onConfirm = { name, quantity, note ->
                    viewModel.update(item.copy(name = name.trim(), quantity = quantity.trim(), note = note.trim()))
                    showEditDialog = false
                }
            )
        }
    }
}
