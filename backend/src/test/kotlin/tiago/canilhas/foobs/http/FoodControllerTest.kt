package tiago.canilhas.foobs.http

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.success
import tiago.canilhas.foobs.http.model.input.CreateFood
import tiago.canilhas.foobs.http.model.input.CreateFoodUnit
import tiago.canilhas.foobs.service.FoodService
import tiago.canilhas.foobs.service.FoodService.CreateError
import tiago.canilhas.foobs.service.FoodService.GetError
import tiago.canilhas.foobs.service.FoodService.GetMultipleError
import tiago.canilhas.foobs.service.FoodService.UpdateError
import tiago.canilhas.foobs.service.FoodService.RemoveError
import tools.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import tiago.canilhas.foobs.domain.failure

@WebMvcTest(FoodController::class)
class FoodControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var jdbi: Jdbi

    @MockkBean
    private lateinit var foodService: FoodService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    /**
     *  Create Tests
     */

    @Test
    fun create_returnsCreated_whenDataIsValid() {
        val request = CreateFood(
            name = "Oatmeal",
            brand = "Quaker",
            protein = 13.0,
            carbohydrate = 68.0,
            fat = 7.0,
            fiber = 10.0,
            units = listOf(CreateFoodUnit("Gram", 1.0))
        )

        val food = Food(1, "Oatmeal", "Quaker", 13.0, 68.0, 7.0, 10.0, 387.0)
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns success(food)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", Routes.Food.byId(1).toString()))
    }

    @Test
    fun create_returnsBadRequest_whenNameIsInvalid() {
        val request = CreateFood("Ab", null, 10.0, 10.0, 10.0, 10.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidName)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun create_returnsBadRequest_whenBrandIsInvalid() {
        val request = CreateFood("Apple", "A", 10.0, 10.0, 10.0, 10.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidBrand)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun create_returnsBadRequest_whenProteinIsInvalid() {
        val request = CreateFood("Apple", null, -1.0, 10.0, 10.0, 10.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidProtein)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun create_returnsBadRequest_whenCarbohydrateIsInvalid() {
        val request = CreateFood("Apple", null, 10.0, -1.0, 10.0, 10.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidCarbohydrate)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun create_returnsBadRequest_whenFatIsInvalid() {
        val request = CreateFood("Apple", null, 10.0, 10.0, -1.0, 10.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidFat)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun create_returnsBadRequest_whenFiberIsInvalid() {
        val request = CreateFood("Apple", null, 10.0, 10.0, 10.0, -1.0, emptyList())
        every { foodService.create(any(), any(), any(), any(), any(), any(), any()) } returns failure(CreateError.InvalidFiber)

        mockMvc.perform(
            post(Routes.Food.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }



    /**
     * Get Tests
     */

    @Test
    fun get_returnsOk_whenFoodExists() {
        val id = 1
        val food = Food(id, "Oatmeal", "Quaker", 13.0, 68.0, 7.0, 10.0, 387.0)

        every { foodService.get(id) } returns success(food)

        mockMvc.perform(
            get(Routes.Food.byId(id).toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("Oatmeal"))
            .andExpect(jsonPath("$.brand").value("Quaker"))
    }

    @Test
    fun get_returnsNotFound_whenFoodDoesNotExist() {
        val id = 999
        every { foodService.get(id) } returns failure(GetError.NotFound)

        mockMvc.perform(
            get(Routes.Food.byId(id).toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    /**
     *  Get Multiple Tests
     */

    @Test
    fun getMultiple_returnsOk_whenFoodExists() {
        val food1 = Food(1, "Apple", null, 0.3, 14.0, 0.2, 2.4, 52.0)
        val food2 = Food(2, "Green Apple", null, 0.4, 15.0, 0.3, 2.5, 60.0)
        val food3 = Food(3, "Banana", null, 1.1, 23.0, 0.3, 2.6, 96.0)

        val mockFoods = listOf(food1, food2, food3)

        every { foodService.getMultiple(null) } returns success(mockFoods)

        mockMvc.perform(
            get(Routes.Food.GET_MULTIPLE)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.foods.length()").value(3))
            .andExpect(jsonPath("$.foods[0].name").value("Apple"))
            .andExpect(jsonPath("$.foods[1].name").value("Green Apple"))
            .andExpect(jsonPath("$.foods[2].name").value("Banana"))
    }

    @Test
    fun getMultiple_returnsOk_whenSearchingByName() {
        val food1 = Food(1, "Apple", null, 0.3, 14.0, 0.2, 2.4, 52.0)
        val food2 = Food(2, "Green Apple", null, 0.4, 15.0, 0.3, 2.5, 60.0)

        val mockFoods = listOf(food1, food2)
        val name = "Apple"

        every { foodService.getMultiple(name) } returns success(mockFoods)

        mockMvc.perform(
            get(Routes.Food.GET_MULTIPLE)
                .param("name", name)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.foods.length()").value(2))
            .andExpect(jsonPath("$.foods[0].name").value("Apple"))
            .andExpect(jsonPath("$.foods[1].name").value("Green Apple"))
    }


    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }

}