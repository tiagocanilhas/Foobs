package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.kotlin.mapTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tiago.canilhas.foobs.domain.MealFoodInfo

class MealRepositoryTest {
    @Test
    fun create_returnsCreatedMealWithId() {
        testWithHandleAndRollback { handle ->
            val repository = MealRepository(handle)
            val name = "name"

            val meal = repository.create(name)

            assertNotNull(meal)
            assertTrue(meal.id > 0)
            assertEquals(name, meal.name)
        }
    }

    @Test
    fun insertFoods_savesBatchSuccessfully() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)
            val name = "name"

            val meal = mealRepository.create(name)

            val foodsToInsert = listOf(
                MealFoodInfo(id = 1, unitId = 1, quantity = 150.0),
                MealFoodInfo(id = 2, unitId = 2, quantity = 1.0)
            )

            mealRepository.insertFoods(meal.id, foodsToInsert)

            val count = handle.createQuery("SELECT COUNT(*) FROM foobs.MealFood  WHERE meal_id = :mealId")
                .bind("mealId", meal.id)
                .mapTo<Int>()
                .single()

            assertEquals(2, count)
        }
    }

    @Test
    fun insertFoods_worksWithEmptyList() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)
            val meal = mealRepository.create("Refeição Vazia")

            assertDoesNotThrow {
                mealRepository.insertFoods(meal.id, emptyList())
            }
        }
    }

    @Test
    fun get() {
    }

    @Test
    fun getMultiple() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

}