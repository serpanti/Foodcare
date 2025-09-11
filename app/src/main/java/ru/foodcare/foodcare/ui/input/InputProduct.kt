package ru.foodcare.foodcare.ui.input

import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties
import ru.foodcare.foodcare.domain.product.Product

class InputProduct(var name: String = "",
                   var production: String  = "",
                   var amount: Int = 1,
                   var type: Product.Companion.UnitType = Product.Companion.UnitType.Piece,
                   var calories: Double? = null,
                   var protein: Double? = null,
                   var fat: Double? = null,
                   var carbohydrates: Double? = null,
                   var fiber: Double? = null) : Input {
    override fun isCorrect(): Boolean {
        return (calories != null && protein != null && fat != null &&
                carbohydrates != null && fiber != null &&
                name.isNotEmpty() && production.isNotEmpty())
    }

    fun toProduct(): Product {
        val nutrientsProperties = NutrientsProperties(calories = calories ?: 0.0,
            protein = protein ?: 0.0, fat = fat ?: 0.0, carbohydrates = carbohydrates ?: 0.0,
            fiber = fiber ?: 0.0)

        return Product(name, production, amount, type, nutrientsProperties)
    }
}