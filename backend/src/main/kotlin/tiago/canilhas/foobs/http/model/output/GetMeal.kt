package tiago.canilhas.foobs.http.model.output

data class GetMeal(
    val id: Int,
    val name: String,
    val url: String?,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val fiber: Double,
    val calories: Double,
    val foods: List<GetMealFood>
)
