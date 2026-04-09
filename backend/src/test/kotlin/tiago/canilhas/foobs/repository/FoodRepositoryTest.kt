package tiago.canilhas.foobs.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tiago.canilhas.foobs.domain.FoodUnitInfo

class FoodRepositoryTest {
    @Test
    fun create_returnsCreatedFood() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val food = repository.create(
                name = "Test Food",
                brand = "Test Brand",
                protein = 10.0,
                carbohydrate = 20.0,
                fat = 5.0,
                fiber = 3.0
            )

            assertNotNull(food)
            assertEquals("Test Food", food.name)
            assertEquals("Test Brand", food.brand)
            assertEquals(10.0, food.protein)
            assertEquals(20.0, food.carbohydrate)
            assertEquals(5.0, food.fat)
            assertEquals(3.0, food.fiber)
            // Check if calories calculation (10*4 + 20*4 + 5*9 = 165) matches
            assertEquals(165.0, food.calories)
        }
    }

    @Test
    fun create_worksWithNullBrand() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val food = repository.create(
                name = "Generic Food",
                brand = null,
                protein = 10.0,
                carbohydrate = 10.0,
                fat = 1.0,
                fiber = 0.0
            )

            assertNotNull(food)
            assertNull(food.brand)
        }
    }

    @Test
    fun insertUnits_savesUnitsSuccessfully() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val food = repository.create(
                "Rice",
                "Brand X",
                2.0,
                25.0,
                0.0,
                1.0
            )

            repository.insertUnits(
                id = food.id,
                units = listOf(
                    FoodUnitInfo(name = "Small Bowl", weight = 150.0),
                    FoodUnitInfo(name = "Large Bowl", weight = 300.0)
                )
            )

            val fetchedFood = repository.get(food.id)

            assertNotNull(fetchedFood)
            throw AssertionError("Unit fetching not implemented yet")
        }
    }

    @Test
    fun get_returnsCorrectFoodById() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)
            val id = 1

            val food = repository.get(id)

            assertNotNull(food)
            assertEquals(1, food?.id)
            assertEquals("Whole Wheat Bread", food?.name)
            assertEquals(null, food?.brand)
            assertEquals(9.0, food?.protein)
            assertEquals(45.0, food?.carbohydrate)
            assertEquals(4.0, food?.fat)
            assertEquals(6.0, food?.fiber)
            assertEquals(252.0, food?.calories)

        }
    }

    @Test
    fun get_returnsNullForNonExistentId() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)
            val food = repository.get(9999)
            assertNull(food)
        }
    }

    @Test
    fun getMultiple_returnsAllFoodsInDatabase() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val foods = repository.getMultiple(null)

            assertTrue(foods.size >= 4)
            assertTrue(foods.any { it.name == "Egg" })
        }
    }

    @Test
    fun getMultiple_returnsFoodsAccordingToName() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val name = "Egg"
            val foods = repository.getMultiple(name)

            assertEquals(1, foods.size)
        }
    }

    @Test
    fun getMultiple_foundsNone() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            val name = "Non Existent Food"
            val foods = repository.getMultiple(name)

            assertTrue(foods.isEmpty())
        }
    }

    @Test
    fun update_modifiesExistingFoodData() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            throw AssertionError("Update method not implemented yet")

//            // Action: Update ID 2 (Egg) from your insert.sql
//            val updated = repository.update(
//                id = 2,
//                name = "Large Bio Egg",
//                brand = "Farm Fresh",
//                protein = 15.0,
//                carbohydrate = 1.0,
//                fat = 11.0,
//                fiber = 0.0
//            )
//
//            // Verification
//            assertNotNull(updated)
//            assertEquals("Large Bio Egg", updated?.name)
//            assertEquals("Farm Fresh", updated?.brand)
//            // Calories: (15*4) + (1*4) + (11*9) = 60 + 4 + 99 = 163
//            assertEquals(163.0, updated?.calories)
        }
    }

    @Test
    fun delete_removesFoodFromDatabase() {
        testWithHandleAndRollback { handle ->
            val repository = FoodRepository(handle)

            throw AssertionError("Delete method not implemented yet")

//            val food = repository.create("To Delete", null, 0.0, 0.0, 0.0, 0.0)
//
//            val wasDeleted = repository.delete(food.id)
//
//            assertTrue(wasDeleted)
//            assertNull(repository.get(food.id))
        }
    }

}