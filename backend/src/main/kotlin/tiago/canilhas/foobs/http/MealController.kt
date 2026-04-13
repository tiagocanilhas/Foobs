package tiago.canilhas.foobs.http

import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tiago.canilhas.foobs.domain.Failure
import tiago.canilhas.foobs.domain.MealFoodInfo
import tiago.canilhas.foobs.domain.SortDirection
import tiago.canilhas.foobs.domain.SortValue
import tiago.canilhas.foobs.domain.Success
import tiago.canilhas.foobs.http.model.input.CreateMeal
import tiago.canilhas.foobs.http.model.output.GetFood
import tiago.canilhas.foobs.http.model.output.GetFoodList
import tiago.canilhas.foobs.http.model.output.GetMeal
import tiago.canilhas.foobs.http.model.output.GetMealFood
import tiago.canilhas.foobs.http.model.output.GetMealList
import tiago.canilhas.foobs.http.model.output.GetUnit
import tiago.canilhas.foobs.service.MealService

@RestController
class MealController(
    private val mealService: MealService
) {

    @PostMapping(Routes.Meal.CREATE)
    fun create(
        @RequestBody meal: CreateMeal
    ): ResponseEntity<*> {
        val res = mealService.create(
            meal.name,
            meal.foods.map { MealFoodInfo(it.id, it.quantity, it.unitId) }
        )

        return when (res) {
            is Failure -> when (res.value){
                MealService.CreateError.InvalidName -> Problem.res(HttpStatus.BAD_REQUEST, Problem.mealInvalidName)
            }
            is Success -> ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", Routes.Meal.byId(res.value.id).toString())
                .build<Unit>()
        }
    }

    @GetMapping(Routes.Meal.GET)
    fun get(
        @PathVariable id: String
    ): ResponseEntity<*> {
        val res = mealService.get(id.toInt())

        return ResponseEntity
            .ok(res)
    }

    @GetMapping(Routes.Meal.GET_MULTIPLE)
    fun getMultiple(
        @RequestParam name: String?,
        @RequestParam minCalories: Int?,
        @RequestParam maxCalories: Int?,
        @RequestParam sortValue: SortValue?,
        @RequestParam sortDirection: SortDirection?
    ): ResponseEntity<*> {
        val res = mealService.getMultiple(
            name,
            minCalories,
            maxCalories,
            sortValue,
            sortDirection
        )

        return when (res) {
            is Failure ->  when (res.value) {
                MealService.GetMultipleError.InvalidMinCalories -> Problem.res(HttpStatus.BAD_REQUEST, Problem.mealInvalidMinCalories)
            }

            is Success -> ResponseEntity
                .status(HttpStatus.OK)
                .body(GetMealList(
                    res.value.map { meal ->
                        GetMeal(
                            meal.id,
                            meal.name,
                            null,
                            meal.protein,
                            meal.carbohydrate,
                            meal.fat,
                            meal.fiber,
                            meal.calories,
                            meal.foods.map { mealFood ->
                                GetMealFood(
                                    mealFood.name,
                                    mealFood.quantity,
                                    mealFood.unitName
                                )
                             }

                        )
                    }
                ))
        }
    }

    @PutMapping(Routes.Meal.UPDATE)
    fun update(
        @PathVariable id: String
    ) : ResponseEntity<*> {

        return ResponseEntity
            .ok() as ResponseEntity<*>
    }

    @DeleteMapping(Routes.Meal.DELETE)
    fun delete(
        @PathVariable id: String
    ) : ResponseEntity<*> {

        return ResponseEntity
            .ok() as ResponseEntity<*>
    }
}