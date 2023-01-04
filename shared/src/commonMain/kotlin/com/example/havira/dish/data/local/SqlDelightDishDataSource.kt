package com.example.havira.dish.data.local

import com.example.havira.database.HaviraDatabase
import com.example.havira.dish.domain.IDishDataSource
import com.example.havira.dish.domain.model.Dish
import com.example.havira.dish.domain.model.DishPrep
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class SqlDelightDishDataSource(
    db: HaviraDatabase
) : IDishDataSource {

    private val dishQueries = db.dishQueries
    private val dishPrepQueries = db.dishPrepQueries

    override suspend fun getAllDishes(): List<Dish> {
        return dishQueries.getAllDishes().executeAsList().map { it.toDish() }
    }

    override suspend fun getDishById(id: Long): Dish? {
        val dish = dishQueries.getDishById(id).executeAsOneOrNull()?.toDish() ?: return null
        val dishPreps = dishPrepQueries.getDishPrepsByDishId(id).executeAsList().map { it.toDishPrep() }
        dish.dishPreps = dishPreps
        return dish
    }

    override suspend fun deleteDishById(id: Long) {
        dishQueries.deleteDishById(id)
    }

    override suspend fun insertDish(dish: Dish) {
        dishQueries.insertDish(
            id = dish.id,
            title = dish.title,
            description = dish.desc,
            rating = dish.rating.toLong(),
            nof_ratings = dish.nofRatings.toLong(),
            last_made = dish.lastMade?.toInstant(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds(),
            created = dish.created.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }

    override suspend fun insertDishPrep(dishPrep: DishPrep) {
        dishPrepQueries.insertDishPrep(
            id = dishPrep.id,
            dish_id = dishPrep.dishId,
            rating = dishPrep.rating.toLong(),
            date = dishPrep.date
        )
    }

}