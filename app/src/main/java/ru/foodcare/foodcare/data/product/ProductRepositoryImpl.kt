package ru.foodcare.foodcare.data.product

import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository

class ProductRepositoryImpl(private val dao: ProductDAO) : ProductRepository {
    override suspend fun addProduct(product: Product) {
        dao.addProduct(ProductMapper.fromDomain(product))
    }

    override suspend fun removeProduct(product: Product) {
        val id = dao.getProduct(product.name, product.production).firstOrNull()?.id
        id?.let { id ->
            dao.removeProduct(ProductMapper.fromDomain(product, id))}
    }

    override suspend fun updateProduct(newProduct: Product, oldProduct: Product) {
        val id = dao.getProduct(oldProduct.name, oldProduct.production).firstOrNull()?.id
        id?.let { id ->
            dao.updateProduct(ProductMapper.fromDomain(newProduct, id))
        }
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