package tiago.canilhas.foobs.http.model.output

data class GetFood (
    val id: Int,
    val name: String,
    val brand : String?,
    val protein: Double,
    val carbohydrate: Double,
    val fat: Double,
    val calories: Double,
    val units: List<GetUnit>
)
