package ru.foodcare.foodcare.data.meal

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.foodcare.foodcare.data.date.DateDAO
import ru.foodcare.foodcare.data.product.ProductDAO
import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.meal.MealRepository

class MealRepositoryImpl(private val mealDao: MealDAO,
                         private val productDao: ProductDAO,
                         private val dateDao: DateDAO): MealRepository {
    override fun observeYears(): Flow<List<Int>> {
        return dateDao.observeYears()
    }

    override fun observeMonths(year: Int): Flow<List<Int>> {
        return dateDao.observeMonths(year)
    }

    override fun observeDays(
        year: Int,
        month: Int
    ): Flow<List<Int>> {
        return dateDao.observeDays(year, month)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeDay(
        year: Int,
        month: Int,
        day: Int
    ): Flow<List<Meal>> {
        return dateDao.getDate(year, month, day)
                .map { it.firstOrNull()?.id }
                .filterNotNull()
                .flatMapLatest { mealDao.observeDay(it) }
                .map { list ->
                    list.map {
                        MealMapper.toDomain(it)
                    }
                }
    }

    override suspend fun addMeal(meal: Meal) {
        var dateId =
            dateDao.getDateAndInsert(meal.date.year, meal.date.monthValue, meal.date.dayOfMonth)
                .firstOrNull()?.id
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { productId ->
            dateId?.let { dateId ->
                mealDao.addMeal(MealMapper.fromDomain(meal, productId, dateId))
            }
        }
    }

    override suspend fun removeMeal(meal: Meal) {
        val dateId =
            dateDao.getSuspendDate(meal.date.year, meal.date.monthValue, meal.date.dayOfMonth)
                .firstOrNull()?.id
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { productId ->
            dateId?.let { dateId ->
                mealDao.removeMeal(MealMapper.fromDomain(meal, productId, dateId))
            }
        }
    }

    override suspend fun updateMeal(meal: Meal) {
        val dateId =
            dateDao.getSuspendDate(meal.date.year, meal.date.monthValue, meal.date.dayOfMonth)
                .firstOrNull()?.id
        val productId =
            productDao.getProduct(meal.product.name, meal.product.production).firstOrNull()?.id
        productId?.let { productId ->
            dateId?.let { dateId ->
                mealDao.updateMeal(MealMapper.fromDomain(meal, productId, dateId))
            }
        }
    }
}