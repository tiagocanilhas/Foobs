package tiago.canilhas.foobs.http.model.output

data class GetMealFood(
    val name: String,
    val quantity: Double,
    val unitName: String
)