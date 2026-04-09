package tiago.canilhas.foobs.http.model.input

data class CreateMeal(
    val name: String,
    val foods: List<CreateMealFood>
)

data class CreateMealFood(
    val id: Int,
    val quantity: Double,
    val unitId: Int
)