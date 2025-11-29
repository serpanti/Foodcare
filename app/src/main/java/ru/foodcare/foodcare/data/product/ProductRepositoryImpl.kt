package ru.foodcare.foodcare.data.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.foodcare.foodcare.domain.product.Product
import ru.foodcare.foodcare.domain.product.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val dao: ProductDAO) : ProductRepository {
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

    override fun observeProducts(): Flow<List<Product>> {
        return dao.observeProducts().map{list ->
            list.map{ProductMapper.toDomain(it)}
        }
    }

    override suspend fun getProduct(
        name: String,
        production: String
    ): List<Product> {
        return dao.getProduct(name, production).map{ProductMapper.toDomain(it)}
    }

    override fun observeProductsByName(name: String): Flow<List<Product>> {
        return dao.observeProductsByName(name).map{ list ->
            list.map{ProductMapper.toDomain(it)}
        }
    }

    override fun observeProductsByProduction(production: String): Flow<List<Product>> {
        return dao.observeProductsByProduction(production).map{ list ->
            list.map{ProductMapper.toDomain(it)}
        }
    }

    override suspend fun getProductByName(name: String): List<Product> {
        return dao.getProductByName(name).map{ProductMapper.toDomain(it)}
    }

    override suspend fun getProductByProduction(production: String): List<Product> {
        return dao.getProductByProduction(production).map{ProductMapper.toDomain(it)}
    }
}