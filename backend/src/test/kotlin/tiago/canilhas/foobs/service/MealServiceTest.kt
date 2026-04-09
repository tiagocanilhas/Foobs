package tiago.canilhas.foobs.service

import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tiago.canilhas.foobs.domain.Failure
import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.domain.MealDomain
import tiago.canilhas.foobs.domain.MealFoodInfo
import tiago.canilhas.foobs.domain.Success
import tiago.canilhas.foobs.repository.MealRepository
import tiago.canilhas.foobs.repository.TransactionManager
import tiago.canilhas.foobs.repository.interfaces.ITransaction
import tiago.canilhas.foobs.service.MealService.CreateError


class MealServiceTest {

    private val transactionManager = mockk<TransactionManager>()
    private val mealDomain = MealDomain()
    private val service = MealService(transactionManager, mealDomain)

    private val transaction = mockk<ITransaction>()
    private val mealRepository = mockk<MealRepository>()

    @BeforeEach
    fun setup() {
        every { transaction.mealRepository } returns mealRepository

        every { transactionManager.run<Any>(any()) } answers {
            val block = firstArg<(ITransaction) -> Any>()
            block(transaction)
        }
    }

    @Test
    fun create_returnsSuccess_whenDataIsValid() {
        val name = "Lunch"
        val foods = listOf(MealFoodInfo(id = 1, unitId = 1, quantity = 200.0))
        val expectedMeal = Meal(id = 1, name = name)

        every { mealRepository.create(name) } returns expectedMeal
        every { mealRepository.insertFoods(any(), any()) } just Runs

        val result = service.create(name, foods)

        assertTrue(result is Success)
        assertEquals(expectedMeal, (result as Success).value)

        verify(exactly = 1) { mealRepository.create(name) }
        verify(exactly = 1) { mealRepository.insertFoods(expectedMeal.id, foods) }
    }

    @Test
    fun create_returnsSuccess_whenFoodsIsEmpty() {
        val name = "Snack"
        val expectedMeal = Meal(id = 2, name = name)

        every { mealRepository.create(name) } returns expectedMeal

        val result = service.create(name, emptyList())

        assertTrue(result is Success)
        assertEquals(expectedMeal, (result as Success).value)
        verify(exactly = 1) { mealRepository.create(name) }
        verify(exactly = 0) { mealRepository.insertFoods(any(), any()) }
    }

    @Test
    fun create_returnsInvalidName_whenNameIsInvalid() {
        val name = ""

        val result = service.create(name, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidName, (result as Failure).value)
        verify { transactionManager wasNot Called }
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
    fun remove() {
    }

}