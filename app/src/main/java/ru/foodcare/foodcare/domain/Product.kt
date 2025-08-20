package ru.foodcare.foodcare.domain

data class Product(val name: String, val production: String,
                   val amount: Int,
                   val type: UnitType,
                   val calories: Double,
                   val protein: Double,
                   val fat: Double,
                   val carbohydrates: Double,
                   val fiber: Double) {

    companion object {
        enum class UnitType {
            Milliliter,
            Gram,
            Piece
        }
    }
}


