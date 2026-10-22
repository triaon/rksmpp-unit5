package ru.mirea.barnatovich.myplants

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.mirea.barnatovich.myplants.ui.PlantsScreen
import ru.mirea.barnatovich.myplants.ui.PlantsViewModel
import java.io.File

private val PlantColors = lightColorScheme(
    primary = Color(0xFF2E7D32),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = PlantColors) {
                val vm: PlantsViewModel = viewModel()
                val photos by vm.photos.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                // путь к файлу, в который камера пишет текущий снимок
                var pendingPath by rememberSaveable { mutableStateOf<String?>(null) }

                val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                    pendingPath?.let { vm.onPhotoTaken(File(it), success) }
                    pendingPath = null
                }

                fun openCamera() {
                    val file = vm.newPhotoFile()
                    pendingPath = file.absolutePath
                    val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
                    takePicture.launch(uri)
                }

                val permissions = buildList {
                    add(Manifest.permission.CAMERA)
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }.toTypedArray()

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { result ->
                    if (result[Manifest.permission.CAMERA] == true) {
                        openCamera()
                    } else {
                        Toast.makeText(this, "Без доступа к камере фото не сделать", Toast.LENGTH_SHORT).show()
                    }
                }

                PlantsScreen(
                    photos = photos,
                    snackbarHostState = snackbarHostState,
                    onTakePhoto = { permissionLauncher.launch(permissions) },
                    onExport = { file ->
                        scope.launch {
                            val ok = vm.export(file)
                            snackbarHostState.showSnackbar(
                                if (ok) "Фото добавлено в галерею" else "Не удалось экспортировать фото"
                            )
                        }
                    },
                    onDelete = { vm.delete(it) }
                )
            }
        }
    }
}
