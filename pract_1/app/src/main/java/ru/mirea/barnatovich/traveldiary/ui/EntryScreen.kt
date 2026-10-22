package ru.mirea.barnatovich.traveldiary.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mirea.barnatovich.traveldiary.data.DiaryEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    entry: DiaryEntry?,
    onSave: (title: String, text: String) -> Unit,
    onBack: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(entry?.title ?: "") }
    var text by rememberSaveable { mutableStateOf(entry?.text ?: "") }

    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (entry == null) "Новая запись" else "Запись") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок (необязательно)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Что интересного было в поездке?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Назад")
                }
                Button(
                    onClick = { onSave(title, text) },
                    enabled = text.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
