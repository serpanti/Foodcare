package ru.foodcare.foodcare.ui.viewModel.backup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.foodcare.foodcare.di.modules.viewModel.IODispatcher
import ru.foodcare.foodcare.domain.backup.BackupRepository
import ru.foodcare.foodcare.foodcare
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BackupViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val appContext: Context,
    @param:IODispatcher private val context: CoroutineContext
) : ViewModel() {

    fun backup(dbDir: File, dbName: String, output: OutputStream) {
        viewModelScope.launch(context) {
            repository.backupDatabase(dbDir, dbName, output)
        }
    }

    fun restore(dbDir: File, input: InputStream) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(context) {
                repository.restoreDatabase(dbDir, input)
            }

            appContext.foodcare.restart()
        }
    }
}
