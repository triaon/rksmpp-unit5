package ru.mirea.barnatovich.shoppinglist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import ru.mirea.barnatovich.shoppinglist.data.Purchase

// диалог и для создания (initial = null), и для редактирования
@Composable
fun PurchaseDialog(
    initial: Purchase?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, quantity: String, note: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(initial?.name ?: "") }
    var quantity by rememberSaveable { mutableStateOf(initial?.quantity ?: "") }
    var note by rememberSaveable { mutableStateOf(initial?.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Новая покупка" else "Редактирование покупки") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Количество") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Заметка") },
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, quantity, note) }, enabled = name.isNotBlank()) {
                Text(if (initial == null) "Добавить" else "Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}
