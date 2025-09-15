package ru.foodcare.foodcare.di.modules.repositories

import dagger.Binds
import dagger.Module
import ru.foodcare.foodcare.data.product.ProductRepositoryImpl
import ru.foodcare.foodcare.domain.product.ProductRepository
import javax.inject.Singleton

@Module
interface ProductRepositoryModule {
    @Binds
    @Singleton
    fun bindsProductRepository(productRepositoryImpl: ProductRepositoryImpl) : ProductRepository
}