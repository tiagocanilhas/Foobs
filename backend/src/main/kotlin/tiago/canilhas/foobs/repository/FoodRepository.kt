package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.FoodUnitInfo
import tiago.canilhas.foobs.domain.Unit
import tiago.canilhas.foobs.repository.interfaces.IFoodRepository

class FoodRepository(
    private val handle: Handle
): IFoodRepository {
    override fun create(
        name: String,
        brand: String?,
        protein: Double,
        carbohydrate: Double,
        fat: Double,
        fiber: Double
    ): Food =
        handle.createUpdate("""
            INSERT INTO foobs.Food (name, brand, protein, carbohydrate, fat, fiber)
            VALUES (:name, :brand, :protein, :carbohydrate, :fat, :fiber)
        """.trimIndent())
            .bind("name", name)
            .bind("brand", brand)
            .bind("protein", protein)
            .bind("carbohydrate", carbohydrate)
            .bind("fat", fat)
            .bind("fiber", fiber)
            .executeAndReturnGeneratedKeys()
            .mapTo<Food>()
            .single()

    override fun insertUnits(id: Int, units: List<FoodUnitInfo>) {
        handle.prepareBatch("""
            INSERT INTO foobs.FoodUnit (food_id, name, weight)
            VALUES (:food_id, :name, :weight)
        """.trimIndent())
            .use { batch ->
                units.forEach { unit ->
                    batch
                        .bind("food_id", id)
                        .bind("name", unit.name)
                        .bind("weight", unit.weight)
                        .add()
                }

                batch.execute()
            }
    }

    override fun get(id: Int): Food? =
        handle.createQuery("""
            SELECT * FROM foobs.Food
            WHERE food.id = :id
        """.trimIndent())
            .bind("id", id)
            .mapTo<Food>()
            .singleOrNull()


    override fun getMultiple(
        name: String?,
    ): List<Food> {
        val query = StringBuilder("""
            SELECT f.*, u.id as unit_id, u.name as unit_name, u.weight as unit_weight FROM foobs.Food f
            LEFT JOIN foobs.FoodUnit u ON u.food_id = f.id
            WHERE 1=1
        """.trimIndent())

        name?.let { query.append(" AND f.name ILIKE :name") }

        query.append(" ORDER BY f.name, unit_id ASC")

        return handle.createQuery(query.toString())
            .apply { name?.let { bind("name", "%$it%") } }
            .reduceRows(LinkedHashMap<Int, Food>()) { map, rowView ->

                val foodId = rowView.getColumn("id", Int::class.javaObjectType)
                val food = map.computeIfAbsent(foodId) { _ ->
                    rowView.getRow(Food::class.java).copy(units = mutableListOf())
                }

                val unitId = rowView.getColumn("unit_id", Int::class.javaObjectType)
                if (unitId != null) {
                    (food.units as MutableList).add(
                        Unit(
                            id = unitId,
                            name = rowView.getColumn("unit_name", String::class.java),
                            weight = rowView.getColumn("unit_weight", Double::class.javaObjectType)
                        )
                    )
                }

                map
            }
                .values
                .toList()
    }


    override fun update(id: Int): Food {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }
}

