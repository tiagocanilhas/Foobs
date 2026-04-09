package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.Handle
import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.repository.interfaces.IMealRepository
import org.jdbi.v3.core.kotlin.mapTo
import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.MealFood
import tiago.canilhas.foobs.domain.MealFoodInfo
import kotlin.collections.MutableList

class MealRepository(
    private val handle: Handle,
): IMealRepository {

    override fun create(
        name: String,
    ): Meal =
        handle.createUpdate("""
            INSERT INTO foobs.Meal (name)
            VALUES (:name)
        """.trimIndent())
            .bind("name", name)
            .executeAndReturnGeneratedKeys()
            .mapTo<Meal>()
            .single()

    override fun insertFoods(
        id: Int,
        foods: List<MealFoodInfo>
    ) {
        handle.prepareBatch("""
            INSERT INTO foobs.MealFood (meal_id, food_id, unit_id, quantity)
            VALUES (:meal_id, :food_id, :unit_id, :quantity)
        """.trimIndent())
            .use { batch ->
                foods.forEach { food ->
                    batch
                        .bind("meal_id", id)
                        .bind("food_id", food.id)
                        .bind("unit_id", food.unitId)
                        .bind("quantity", food.quantity)
                        .add()
                }

                batch.execute()
            }
    }


    override fun get(id: Int): Meal? {
        TODO("Not yet implemented")
    }

    override fun getMultiple(): List<Meal> {
        val query = StringBuilder("""
            SELECT md.*, f.name as food_name, mf.quantity as quantity, fu.name as unit_name FROM foobs.MealDetails md
            JOIN foobs.MealFood mf ON mf.meal_id = md.id 
            JOIN foobs.Food f ON f.id = mf.food_id
            JOIN foobs.FoodUnit fu ON fu.id = mf.unit_id
            WHERE 1=1
        """.trimIndent())

        query.append(" ORDER BY md.id ASC")

        return handle.createQuery(query.toString())
            .reduceRows(LinkedHashMap<Int, Meal>()) { map, rowView ->

                val mealId = rowView.getColumn("id", Int::class.javaObjectType)
                val meal = map.computeIfAbsent(mealId) {
                    rowView.getRow(Meal::class.java).copy(foods = mutableListOf())
                }

                (meal.foods as MutableList).add(
                    MealFood(
                        name = rowView.getColumn("food_name", String::class.java),
                        quantity = rowView.getColumn("quantity", Double::class.javaObjectType),
                        unitName = rowView.getColumn("unit_name", String::class.java)
                    )
                )

                map
            }
                .values
                .toList()

    }

    override fun update(id: Int): Meal {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }
}