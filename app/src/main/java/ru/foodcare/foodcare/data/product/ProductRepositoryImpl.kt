package ru.foodcare.foodcare.data.product

import ru.foodcare.foodcare.domain.Product
import ru.foodcare.foodcare.domain.ProductRepository

class ProductRepositoryImpl(private val dao: ProductDAO) : ProductRepository {
    override fun addProduct(product: Product) {
        dao.addProduct(ProductMapper.fromDomain(product))
    }

    override fun removeProduct(product: Product) {
        dao.removeProduct(ProductMapper.fromDomain(product))
    }

    override fun updateProduct(newProduct: Product, oldProduct: Product) {
        val product = ProductMapper.fromDomain(newProduct)
        dao.updateProduct(product.name, product.production, product.amount, product.type,
            product.calories, product.protein, product.fat, product.carbohydrates, product.fiber,
            oldProduct.name, oldProduct.production)
    }

    override fun getProducts(): List<Product> {
        return dao.getProducts().map{ProductMapper.toDomain(it)}
    }

    override fun getProduct(
        name: String,
        production: String
    ): List<Product> {
        return dao.getProduct(name, production).map{ProductMapper.toDomain(it)}
    }

    override fun getProductByName(name: String): List<Product> {
        return dao.getProductByName(name).map{ProductMapper.toDomain(it)}
    }

    override fun getProductByProduction(production: String): List<Product> {
        return dao.getProductByProduction(production).map{ProductMapper.toDomain(it)}
    }
}