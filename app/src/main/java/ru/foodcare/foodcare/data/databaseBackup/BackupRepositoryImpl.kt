package ru.foodcare.foodcare.data.databaseBackup

import ru.foodcare.foodcare.domain.backup.BackupRepository
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor() : BackupRepository {
    override suspend fun backupDatabase(dbDir: File, dbName: String, output: OutputStream) {
        BackupManager.zipDatabase(dbDir, dbName, output)
    }

    override suspend fun restoreDatabase(dbDir: File, input: InputStream) {
        BackupManager.unzipDatabase(dbDir, input)
    }
}
