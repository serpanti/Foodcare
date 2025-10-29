package ru.foodcare.foodcare.domain.backup

import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface BackupRepository {
    suspend fun backupDatabase(dbDir: File, dbName: String, output: OutputStream)
    suspend fun restoreDatabase(dbDir: File, input: InputStream)
}
