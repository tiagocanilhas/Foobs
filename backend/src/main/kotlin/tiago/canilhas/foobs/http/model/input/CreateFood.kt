package tiago.canilhas.foobs.http.model.input

data class CreateFood (
    val name: String,
    val brand: String?,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val fiber: Double,
    val units: List<CreateFoodUnit>
)

data class CreateFoodUnit (
    val name: String,
    val weight: Double
)