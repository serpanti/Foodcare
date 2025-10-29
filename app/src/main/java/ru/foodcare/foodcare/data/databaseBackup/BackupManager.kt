package ru.foodcare.foodcare.data.databaseBackup

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

import java.util.zip.ZipOutputStream

object BackupManager {
    fun zipDatabase(dbDir: File, dbName: String, output: OutputStream) {
        val dbFiles = listOf(
            File(dbDir, dbName),
            File(dbDir, "$dbName-wal"),
            File(dbDir, "$dbName-shm")
        ).filter { it.exists() }

        ZipOutputStream(output).use { zos ->
            dbFiles.forEach { file ->
                FileInputStream(file).use { fis ->
                    val entry = ZipEntry(file.name)
                    zos.putNextEntry(entry)
                    fis.copyTo(zos)
                    zos.closeEntry()
                }
            }
        }
    }

    fun unzipDatabase(dbDir: File, input: InputStream) {
        val tmpFile = File.createTempFile("backup", ".zip", dbDir)
        tmpFile.outputStream().use { output -> input.copyTo(output) }

        ZipFile(tmpFile).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                val outFile = File(dbDir, entry.name)
                outFile.parentFile?.mkdirs()
                zip.getInputStream(entry).use { input ->
                    FileOutputStream(outFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}