package ru.foodcare.foodcare.presentation.mock

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.ProductRepository
import ru.foodcare.foodcare.presentation.viewModel.product.ProductViewModel

class MockProductViewModel : ProductViewModel(
    repository = object : ProductRepository {
        override suspend fun addProduct(product: Product) {
            TODO("Not yet implemented")
        }

        override suspend fun removeProduct(product: Product) {
            TODO("Not yet implemented")
        }

        override suspend fun updateProduct(
            newProduct: Product,
            oldProduct: Product
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun getProducts(): List<Product> {
            return listOf(Product("Томат", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                24.0, 1.1, 0.2, 3.8, 1.4), Product("Огурец", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                14.0, 0.8, 0.1, 2.5, 1.0)) + List (10) { Product("Томат", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                24.0, 1.1, 0.2, 3.8, 1.4) }
        }

        override fun observeProducts(): Flow<List<Product>> {
            return flowOf(listOf(Product("Томат", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                24.0, 1.1, 0.2, 3.8, 1.4), Product("Огурец", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                14.0, 0.8, 0.1, 2.5, 1.0)) + List (10) { Product("Томат", "Домашнее производство",
                100, Product.Companion.UnitType.Gram,
                24.0, 1.1, 0.2, 3.8, 1.4) })
        }

        override suspend fun getProduct(
            name: String,
            production: String
        ): List<Product> {
            TODO("Not yet implemented")
        }

        override suspend fun getProductByName(name: String): List<Product> {
            TODO("Not yet implemented")
        }

        override suspend fun getProductByProduction(production: String): List<Product> {
            TODO("Not yet implemented")
        }
    }, Dispatchers.IO
)