package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.kotlin.mapTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tiago.canilhas.foobs.domain.MealFoodInfo
import tiago.canilhas.foobs.domain.SortValue

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
    fun getMultiple_defaultValues() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val meals = mealRepository.getMultiple()

            assertNotNull(meals)
            assertEquals(3, meals.size)
        }
    }

    @Test
    fun getMultiple_withName() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val name = "egg"
            val meals = mealRepository.getMultiple(name = name)

            assertNotNull(meals)
            assertEquals(1, meals.size)
            assertEquals("Bread with Egg", meals[0].name)
        }
    }

    @Test
    fun getMultiple_withMinCalories() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val minCalories = 500
            val meals = mealRepository.getMultiple(minCalories = minCalories)

            assertNotNull(meals)
            assertEquals(1, meals.size)
            assertEquals("Salmon with Broccoli", meals[0].name)
        }
    }

    @Test
    fun getMultiple_withMaxCalories() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val maxCalories = 400
            val meals = mealRepository.getMultiple(maxCalories = maxCalories)

            assertNotNull(meals)
            assertEquals(1, meals.size)
            assertEquals("Bread with Egg", meals[0].name)
        }
    }

    @Test
    fun getMultiple_withSortManagementName(){
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.NAME
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertEquals("Bread with Egg", meals[0].name)
            assertEquals("Chicken with Rice", meals[1].name)
            assertEquals("Salmon with Broccoli", meals[2].name)
        }
    }

    @Test
    fun getMultiple_withSortManagementCalories() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.CALORIES
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertTrue(meals[0].calories < meals[1].calories)
            assertTrue(meals[1].calories < meals[2].calories)
        }
    }

    @Test
    fun getMultiple_withSortManagementProtein() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.PROTEIN
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertTrue(meals[0].protein < meals[1].protein)
            assertTrue(meals[1].protein < meals[2].protein)
        }
    }

    @Test
    fun getMultiple_withSortManagementCarbohydrate() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.CARBOHYDRATE
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertTrue(meals[0].carbohydrate < meals[1].carbohydrate)
            assertTrue(meals[1].carbohydrate < meals[2].carbohydrate)
        }
    }

    @Test
    fun getMultiple_withSortManagementFat() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.FAT
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertTrue(meals[0].fat < meals[1].fat)
            assertTrue(meals[1].fat < meals[2].fat)
        }
    }

    @Test
    fun getMultiple_withSortManagementFiber() {
        testWithHandleAndRollback { handle ->
            val mealRepository = MealRepository(handle)

            val sortValue = SortValue.FIBER
            val meals = mealRepository.getMultiple(sortValue = sortValue)

            assertNotNull(meals)
            assertEquals(3, meals.size)
            assertTrue(meals[0].fiber < meals[1].fiber)
            assertTrue(meals[1].fiber < meals[2].fiber)
        }
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

}