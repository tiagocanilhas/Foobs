package tiago.canilhas.foobs.repository.interfaces

import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.domain.MealFoodInfo

interface IMealRepository {

    fun create(
        name: String,
    ): Meal

    fun insertFoods(
        id: Int,
        foods: List<MealFoodInfo>
    )

    fun get(id: Int): Meal?

    fun getMultiple(): List<Meal>

    fun update(
        id: Int,
    ): Meal

    fun delete(id: Int)
}