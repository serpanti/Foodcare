package ru.foodcare.foodcare.di.modules.repositories

import dagger.Binds
import dagger.Module
import ru.foodcare.foodcare.data.databaseBackup.BackupRepositoryImpl
import ru.foodcare.foodcare.domain.backup.BackupRepository
import javax.inject.Singleton

@Module
interface BackupRepositoryModule {
    @Binds
    @Singleton
    fun bindsDateRepository(backupRepositoryImpl: BackupRepositoryImpl) : BackupRepository
}
