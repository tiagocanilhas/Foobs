package tiago.canilhas.foobs.domain

data class Meal (
    val id: Int,
    val name: String,
    val url: String? = null,
    val protein: Double = 0.0,
    val carbohydrate: Double= 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0,
    val calories: Double = 0.0,
    val foods: List<MealFood> = emptyList()
)