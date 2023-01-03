package com.example.havira.dish.data.local

import com.example.havira.database.HaviraDatabase
import com.example.havira.dish.domain.IDishDataSource
import com.example.havira.dish.domain.model.Dish
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class SqlDelightDishDataSource(
    db: HaviraDatabase
) : IDishDataSource {

    private val queries = db.dishQueries

    override suspend fun getAllDishes(): List<Dish> {
        return queries.getAllDishes().executeAsList().map { it.toDish() }
    }

    override suspend fun getDishById(id: Long): Dish? {
        return queries.getDishById(id).executeAsOneOrNull()?.toDish()
    }

    override suspend fun deleteDishById(id: Long) {
        queries.deleteDishById(id)
    }

    override suspend fun insertDish(dish: Dish) {
        queries.insertDish(
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

}