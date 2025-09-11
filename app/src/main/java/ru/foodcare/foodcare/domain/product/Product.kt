package ru.foodcare.foodcare.domain.product

import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties

data class Product(val name: String, val production: String,
                   val amount: Int,
                   val type: UnitType,
                   val nutrientsProperties: NutrientsProperties) {

    companion object {
        enum class UnitType {
            Milliliter,
            Gram,
            Piece
        }
    }
}