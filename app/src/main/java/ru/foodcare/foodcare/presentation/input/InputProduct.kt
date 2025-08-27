package ru.foodcare.foodcare.presentation.input

import ru.foodcare.foodcare.domain.product.Product

class InputProduct(var name: String = "",
                   var production: String  = "",
                   var amount: Int = 1,
                   var type: Product.Companion.UnitType = Product.Companion.UnitType.Piece,
                   var calories: Double? = null,
                   var protein: Double? = null,
                   var fat: Double? = null,
                   var carbohydrates: Double? = null,
                   var fiber: Double? = null) {
    fun isCorrect(): Boolean {
        return (calories != null && protein != null && fat != null &&
                carbohydrates != null && fiber != null &&
                name.isNotEmpty() && production.isNotEmpty())
    }

    fun toProduct(): Product {
        return Product(
            name, production, amount, type,
            calories ?: 0.0, protein ?: 0.0, fat ?: 0.0, carbohydrates ?: 0.0, fiber ?: 0.0
        )
    }
}