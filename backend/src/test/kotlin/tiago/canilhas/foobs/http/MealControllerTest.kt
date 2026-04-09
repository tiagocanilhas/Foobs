package tiago.canilhas.foobs.http

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.domain.success
import tiago.canilhas.foobs.http.model.input.CreateMeal
import tiago.canilhas.foobs.http.model.input.CreateMealFood
import tiago.canilhas.foobs.service.MealService
import tools.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import tiago.canilhas.foobs.domain.failure

@WebMvcTest(MealController::class)
class MealControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var jdbi: Jdbi

    @MockkBean
    private lateinit var mealService: MealService

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    /**
     * Create Tests
     */

    @Test
    fun create_returnsCreated_whenDataIsValid() {
        val request = CreateMeal(
            name = "Post Workout",
            foods = listOf(
                CreateMealFood(id = 1, quantity = 100.0, unitId = 1)
            )
        )

        val meal = Meal(id = 1, name = "Post Workout")

        every { mealService.create(any(), any()) } returns success(meal)

        mockMvc.perform(
            post(Routes.Meal.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", Routes.Meal.byId(meal.id).toString()))
    }

    @Test
    fun create_returnsBadRequest_whenNameIsInvalid() {
        val request = CreateMeal(name = "", foods = emptyList())

        every { mealService.create(any(), any()) } returns failure(MealService.CreateError.InvalidName)

        mockMvc.perform(
            post(Routes.Meal.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
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