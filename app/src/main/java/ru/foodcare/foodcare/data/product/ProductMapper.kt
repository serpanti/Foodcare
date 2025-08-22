package ru.foodcare.foodcare.data.product

import ru.foodcare.foodcare.domain.Product as ProductDomain

object ProductMapper {
    fun toDomain(entity: Product) = ProductDomain(
            name = entity.name,
            production = entity.production,
            amount = entity.amount,
            type = mapStringToUnitType(entity.type),
            calories = entity.calories,
            protein = entity.protein,
            fat = entity.fat,
            carbohydrates = entity.carbohydrates,
            fiber = entity.fiber
        )

    fun fromDomain(domain: ProductDomain, idOptional: Int = 0): Product = Product(
            id = idOptional,
            name = domain.name,
            production = domain.production,
            amount = domain.amount,
            type = mapUnitTypeToString(domain.type),
            calories = domain.calories,
            protein = domain.protein,
            fat = domain.fat,
            carbohydrates = domain.carbohydrates,
            fiber = domain.fiber
        )

    private fun mapStringToUnitType(type: String): ProductDomain.Companion.UnitType {
        return when (type) {
            "мл" -> ProductDomain.Companion.UnitType.Milliliter
            "грамм" -> ProductDomain.Companion.UnitType.Gram
            "шт" -> ProductDomain.Companion.UnitType.Piece
            else -> ProductDomain.Companion.UnitType.Piece
        }
    }

    private fun mapUnitTypeToString(type: ProductDomain.Companion.UnitType): String {
        return when (type) {
            ProductDomain.Companion.UnitType.Milliliter -> "мл"
            ProductDomain.Companion.UnitType.Gram -> "грамм"
            ProductDomain.Companion.UnitType.Piece -> "шт"
        }
    }
}