package tiago.canilhas.foobs.domain

enum class SortValue {
    NAME,
    CALORIES,
    PROTEIN,
    CARBOHYDRATE,
    FAT,
    FIBER;

    override fun toString(): String = name.lowercase()
}

enum class SortDirection {
    ASC, DESC
}