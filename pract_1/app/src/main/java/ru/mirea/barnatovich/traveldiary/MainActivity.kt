package ru.mirea.barnatovich.traveldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mirea.barnatovich.traveldiary.ui.DiaryListScreen
import ru.mirea.barnatovich.traveldiary.ui.DiaryViewModel
import ru.mirea.barnatovich.traveldiary.ui.EntryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val vm: DiaryViewModel = viewModel()
                val entries by vm.entries.collectAsState()
                val isLoading by vm.isLoading.collectAsState()

                // null — список; "" — новая запись; иначе имя файла открытой записи
                var openedFile by rememberSaveable { mutableStateOf<String?>(null) }

                if (openedFile == null) {
                    DiaryListScreen(
                        entries = entries,
                        isLoading = isLoading,
                        onAdd = { openedFile = "" },
                        onOpen = { openedFile = it.fileName },
                        onDelete = { vm.delete(it.fileName) }
                    )
                } else {
                    val entry = entries.find { it.fileName == openedFile }
                    EntryScreen(
                        entry = entry,
                        onSave = { title, text ->
                            vm.save(entry, title, text)
                            openedFile = null
                        },
                        onBack = { openedFile = null }
                    )
                }
            }
        }
    }
}
