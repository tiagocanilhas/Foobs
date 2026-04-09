package tiago.canilhas.foobs.repository.interfaces

import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.FoodUnitInfo

interface IFoodRepository {

    fun create(
        name: String,
        brand: String?,
        protein: Double,
        carbohydrate: Double,
        fat: Double,
        fiber: Double
    ): Food

    fun insertUnits(
        id: Int,
        units: List<FoodUnitInfo>
    )

    fun get(id: Int): Food?

    fun getMultiple(
        name: String?
    ): List<Food>

    fun update(
        id: Int,
    ): Food

    fun delete(id: Int)
}