package tiago.canilhas.foobs.domain

data class Food (
    val id: Int,
    val name: String,
    val brand: String?,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val fiber: Double,
    val calories: Double,
    val units: List<Unit> = emptyList(),
)