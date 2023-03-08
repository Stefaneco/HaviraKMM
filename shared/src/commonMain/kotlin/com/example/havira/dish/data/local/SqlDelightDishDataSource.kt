package com.example.havira.dish.data.local

import com.example.havira.database.HaviraDatabase
import com.example.havira.dish.domain.IDishDataSource
import com.example.havira.dish.domain.model.Dish
import com.example.havira.dish.domain.model.DishPrep
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class SqlDelightDishDataSource(
    private val db: HaviraDatabase
) : IDishDataSource {

    private val dishQueries = db.dishQueries
    private val dishPrepQueries = db.dishPrepQueries

    override suspend fun getAllDishes(): List<Dish> {
        return dishQueries.getAllDishes().executeAsList().map { it.toDish() }
    }

    override suspend fun getDishById(id: Long): Dish? {
        val dish = dishQueries.getDishById(id).executeAsOneOrNull()?.toDish() ?: return null
        val dishPreps = dishPrepQueries.getDishPrepsByDishId(id).executeAsList().map { it.toDishPrep() }
        dish.dishPreps = dishPreps.sortedByDescending { it.date }
        return dish
    }

    override suspend fun deleteDishById(id: Long) {
        db.transaction {
            dishQueries.deleteDishById(id)
            dishPrepQueries.deleteDishPrepsByDishId(id)
        }
    }

    override suspend fun insertDish(dish: Dish) {
        dishQueries.insertDish(
            id = dish.id,
            title = dish.title,
            description = dish.desc,
            rating = dish.rating.toDouble(),
            nof_ratings = dish.nofRatings.toLong(),
            last_made = dish.lastMade?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds(),
            created = dish.created.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }

    override suspend fun insertDishPrep(dishPrep: DishPrep, dish: Dish) : Long {
        val id : Long = dishPrepQueries.transactionWithResult {
            dishQueries.insertDish(
                id = dish.id,
                title = dish.title,
                description = dish.desc,
                rating = dish.rating.toDouble(),
                nof_ratings = dish.nofRatings.toLong(),
                last_made = dish.lastMade?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds(),
                created = dish.created.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            )
            dishPrepQueries.insertDishPrep(
                id = dishPrep.id,
                dish_id = dishPrep.dishId,
                rating = dishPrep.rating.toLong(),
                date = dishPrep.date
            )
            dishPrepQueries.getLastInsertedRowId().executeAsOne()
        }
        return id
    }

    override suspend fun updateDish(id: Long, title: String, description: String) {
        db.transaction {
            dishQueries.updateDish(title, description, id)
            val numberOfRowsAffected = dishQueries.selectChanges().executeAsOne()
            if(numberOfRowsAffected != 1L){
                throw IllegalStateException("Expected to update 1 dish, but updated $numberOfRowsAffected")
            }
        }
    }

}