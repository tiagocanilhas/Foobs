package tiago.canilhas.foobs.repository.interfaces

import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.domain.MealFoodInfo
import tiago.canilhas.foobs.domain.SortDirection
import tiago.canilhas.foobs.domain.SortValue

interface IMealRepository {

    fun create(
        name: String,
    ): Meal

    fun insertFoods(
        id: Int,
        foods: List<MealFoodInfo>
    )

    fun get(id: Int): Meal?

    fun getMultiple(
        name: String? = null,
        minCalories: Int? = null,
        maxCalories: Int? = null,
        sortValue: SortValue? = null,
        sortDirection: SortDirection? = null,
    ): List<Meal>

    fun update(
        id: Int,
    ): Meal

    fun delete(id: Int)
}