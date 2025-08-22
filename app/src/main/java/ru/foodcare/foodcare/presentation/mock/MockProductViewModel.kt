package ru.foodcare.foodcare.presentation.mock

import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository
import ru.foodcare.foodcare.presentation.viewModel.ProductViewModel

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
    }
)