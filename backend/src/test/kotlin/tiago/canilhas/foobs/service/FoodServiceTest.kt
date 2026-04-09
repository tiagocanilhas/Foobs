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
import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.FoodDomain
import tiago.canilhas.foobs.domain.FoodUnitInfo
import tiago.canilhas.foobs.domain.Success
import tiago.canilhas.foobs.domain.Failure
import tiago.canilhas.foobs.repository.TransactionManager
import tiago.canilhas.foobs.repository.interfaces.IFoodRepository
import tiago.canilhas.foobs.repository.interfaces.ITransaction
import tiago.canilhas.foobs.service.FoodService.CreateError

class FoodServiceTest {
    private val transactionManager = mockk<TransactionManager>()
    private val foodDomain = FoodDomain()
    private val service = FoodService(transactionManager, foodDomain)

    private val transaction = mockk<ITransaction>()
    private val foodRepository = mockk<IFoodRepository>()

    @BeforeEach
    fun setup() {
        every { transaction.foodRepository } returns foodRepository

        every { transactionManager.run<Any>(any()) } answers {
            val block = firstArg<(ITransaction) -> Any>()
            block(transaction)
        }
    }

    /**
     *  Create Tests
     */

    @Test
    fun create_returnsSuccess_whenDataIsValid() {
        val foodName = "Oatmeal"
        val units = listOf(FoodUnitInfo("Gram", 1.0))
        val expectedFood = Food(1, foodName, "Quaker", 13.0, 68.0, 7.0, 10.0, 387.0)

        every { foodRepository.create(any(), any(), any(), any(), any(), any()) } returns expectedFood
        every { foodRepository.insertUnits(any(), any()) } just Runs

        val result = service.create(foodName, "Quaker", 13.0, 68.0, 7.0, 10.0, units)

        assertTrue(result is Success)
        assertEquals(expectedFood, (result as Success).value)

        verify(exactly = 1) { foodRepository.create(foodName, "Quaker", 13.0, 68.0, 7.0, 10.0) }
        verify(exactly = 1) { foodRepository.insertUnits(expectedFood.id, units) }
    }

    @Test
    fun create_returnsSuccess_whenBrandIsNull() {
        val foodName = "Apple"
        val expectedFood = Food(1, foodName, null, 0.3, 14.0, 0.2, 2.4, 52.0)

        every { foodRepository.create(foodName, null, 0.3, 14.0, 0.2, 2.4) } returns expectedFood

        val result = service.create(foodName, null, 0.3, 14.0, 0.2, 2.4, emptyList())

        assertTrue(result is Success)
        assertNull((result as Success).value.brand)
        verify(exactly = 1) { foodRepository.create(foodName, null, 0.3, 14.0, 0.2, 2.4) }
    }

    @Test
    fun create_returnsInvalidName_whenNameIsInvalid() {
        val name = "Ab"

        val result = service.create(name, null, 10.0, 10.0, 10.0, 10.0, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidName, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_returnsInvalidBrand_whenBrandIsInvalid() {
        val brand = "A"

        val result = service.create("Apple", brand, 10.0, 10.0, 10.0, 10.0, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidBrand, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_returnsInvalidProtein_whenValueIsInvalid() {
        val invalidValue = -1.0

        val result = service.create("Apple", null, invalidValue, 10.0, 10.0, 10.0, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidProtein, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_returnsInvalidCarbohydrate_whenValueIsInvalid() {
        val invalidValue = -1.0

        val result = service.create("Apple", null, 10.0, invalidValue, 10.0, 10.0, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidCarbohydrate, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_returnsInvalidFat_whenValueIsInvalid() {
        val invalidValue = -1.0

        val result = service.create("Apple", null, 10.0, 10.0, invalidValue, 10.0, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidFat, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_returnsInvalidFiber_whenValueIsInvalid() {
        val invalidValue = -1.0

        val result = service.create("Apple", null, 10.0, 10.0, 10.0, invalidValue, emptyList())

        assertTrue(result is Failure)
        assertEquals(CreateError.InvalidFiber, (result as Failure).value)
        verify { transactionManager wasNot Called }
    }

    @Test
    fun create_callsInsertUnits_onlyWhenListIsNotEmpty() {
        val expectedFood = Food(1, "Oats", null, 10.0, 10.0, 10.0, 10.0, 170.0)
        val units = listOf(FoodUnitInfo("Cup", 80.0))

        every { foodRepository.create(any(), any(), any(), any(), any(), any()) } returns expectedFood
        every { foodRepository.insertUnits(any(), any()) } just Runs

        service.create("Oats", null, 10.0, 10.0, 10.0, 10.0, units)

        verify(exactly = 1) { foodRepository.insertUnits(1, units) }
    }

    @Test
    fun create_skipsInsertUnits_whenListIsEmpty() {
        val expectedFood = Food(1, "Oats", null, 10.0, 10.0, 10.0, 10.0, 170.0)

        every { foodRepository.create(any(), any(), any(), any(), any(), any()) } returns expectedFood

        service.create("Oats", null, 10.0, 10.0, 10.0, 10.0, emptyList())

        verify(exactly = 0) { foodRepository.insertUnits(any(), any()) }
    }

    @Test
    fun create_skipsBrandValidation_whenBrandIsNull() {
        val expectedFood = Food(1, "Apple", null, 10.0, 10.0, 10.0, 10.0, 170.0)
        every { foodRepository.create(any(), any(), any(), any(), any(), any()) } returns expectedFood

        val result = service.create("Apple", null, 10.0, 10.0, 10.0, 10.0, emptyList())

        assertTrue(result is Success)
        verify(exactly = 1) { foodRepository.create(any(), any(), any(), any(), any(), any()) }
    }


    /**
     * Get Tests
     */

    @Test
    fun getMultiple_returnsSuccess_withMatchingFoods() {
        val food1 = Food(1, "Apple", null, 0.3, 14.0, 0.2, 2.4, 52.0)
        val food2 = Food(2, "Green Apple", null, 0.4, 15.0, 0.3, 2.5, 60.0)
        val food3 = Food(3, "Banana", null, 1.1, 23.0, 0.3, 2.6, 96.0)

        val name = "Apple"
        every { foodRepository.getMultiple(name) } returns listOf(food1, food2)

        val result = service.getMultiple(name)

        assertTrue(result is Success)
        assertEquals(listOf(food1, food2), (result as Success).value)
        verify(exactly = 1) { foodRepository.getMultiple("Apple") }
    }
}