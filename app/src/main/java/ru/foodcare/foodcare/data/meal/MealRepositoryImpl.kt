package ru.foodcare.foodcare.data.meal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.foodcare.foodcare.data.product.ProductDAO
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository

class MealRepositoryImpl(private val mealDao: MealDAO,
                         private val productDao: ProductDAO): MealRepository {
    override fun observeYears(): Flow<List<Int>> {
        return mealDao.observeYears()
    }

    override fun observeMonths(year: Int): Flow<List<Int>> {
        return mealDao.observeMonths(year)
    }

    override fun observeDays(
        year: Int,
        month: Int
    ): Flow<List<Int>> {
        return mealDao.observeDays(year, month)
    }

    override fun observeDay(
        year: Int,
        month: Int,
        day: Int
    ): Flow<List<Meal>> {
        return mealDao.observeDay(year, month, day).map { list ->
            list.map {
                MealMapper.toDomain(it)
            }
        }
    }

    override suspend fun addMeal(meal: Meal) {
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { id ->
            mealDao.addMeal(MealMapper.fromDomain(meal, id))}
    }

    override suspend fun removeMeal(meal: Meal) {
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { id ->
            mealDao.removeMeal(MealMapper.fromDomain(meal, id))}
    }

    override suspend fun updateMeal(meal: Meal) {
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { id ->
            mealDao.updateMeal(MealMapper.fromDomain(meal, id))}
    }
}