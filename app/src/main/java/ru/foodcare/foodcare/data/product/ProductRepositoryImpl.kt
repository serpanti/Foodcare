package ru.foodcare.foodcare.data.product

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository

class ProductRepositoryImpl(private val dao: ProductDAO) : ProductRepository {
    val scope = CoroutineScope(Dispatchers.IO)

    override fun addProduct(product: Product) {
        scope.launch { dao.addProduct(ProductMapper.fromDomain(product)) }
    }

    override fun removeProduct(product: Product) {
        scope.launch { dao.removeProduct(ProductMapper.fromDomain(product))}
    }

    override fun updateProduct(newProduct: Product, oldProduct: Product) {
        val product = ProductMapper.fromDomain(newProduct)
        scope.launch { dao.updateProduct(product.name, product.production, product.amount, product.type,
            product.calories, product.protein, product.fat, product.carbohydrates, product.fiber,
            oldProduct.name, oldProduct.production)}
    }

    override suspend fun getProducts(): List<Product> {
        return dao.getProducts().map{ProductMapper.toDomain(it)}
    }

    override suspend fun getProduct(
        name: String,
        production: String
    ): List<Product> {
        return dao.getProduct(name, production).map{ProductMapper.toDomain(it)}
    }

    override suspend fun getProductByName(name: String): List<Product> {
        return dao.getProductByName(name).map{ProductMapper.toDomain(it)}
    }

    override suspend fun getProductByProduction(production: String): List<Product> {
        return dao.getProductByProduction(production).map{ProductMapper.toDomain(it)}
    }
}