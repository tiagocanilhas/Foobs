package tiago.canilhas.foobs.http

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
import tiago.canilhas.foobs.domain.Success
import tiago.canilhas.foobs.domain.FoodUnitInfo
import tiago.canilhas.foobs.http.model.input.CreateFood
import tiago.canilhas.foobs.http.model.output.GetFood
import tiago.canilhas.foobs.http.model.output.GetFoodList
import tiago.canilhas.foobs.http.model.output.GetUnit
import tiago.canilhas.foobs.service.FoodService
import tiago.canilhas.foobs.service.FoodService.CreateError
import tiago.canilhas.foobs.service.FoodService.GetError
import tiago.canilhas.foobs.service.FoodService.GetMultipleError
import tiago.canilhas.foobs.service.FoodService.UpdateError
import tiago.canilhas.foobs.service.FoodService.RemoveError

@RestController
class FoodController(
    private val foodService: FoodService
) {
    @PostMapping(Routes.Food.CREATE)
    fun create(
        @RequestBody food: CreateFood
    ): ResponseEntity<*> {
        val res = foodService.create(
            food.name,
            food.brand,
            food.protein,
            food.carbohydrate,
            food.fat,
            food.fiber,
            food.units.map { FoodUnitInfo(it.name, it.weight) }
        )

        return when (res) {
            is Failure -> when (res.value){
                CreateError.InvalidName -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidName)
                CreateError.InvalidBrand -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidBrand)
                CreateError.InvalidProtein -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidProtein)
                CreateError.InvalidCarbohydrate -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidCarbohydrate)
                CreateError.InvalidFat -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidFat)
                CreateError.InvalidFiber -> Problem.res(HttpStatus.BAD_REQUEST, Problem.foodInvalidFiber)
            }
            is Success -> ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", Routes.Food.byId(res.value.id).toString())
                .build<Unit>()
        }
    }

    @GetMapping(Routes.Food.GET)
    fun get(
        @PathVariable id: String
    ): ResponseEntity<*> {
        val res = foodService.get(id.toInt())

        return when (res) {
            is Failure -> when (res.value){
                GetError.NotFound -> Problem.res(HttpStatus.NOT_FOUND, Problem.foodNotFound)
            }
            is Success -> ResponseEntity
                .status(HttpStatus.OK)
                .body(res.value)
        }
    }

    @GetMapping(Routes.Food.GET_MULTIPLE)
    fun getMultiple(
        @RequestParam name: String?
    ): ResponseEntity<*> {
        val res = foodService.getMultiple(name)

        return when (res) {
            is Failure ->  throw IllegalStateException("This should not happen!")

            is Success -> ResponseEntity
                .status(HttpStatus.OK)
                .body(GetFoodList(
                    res.value.map { food ->
                        GetFood(
                            id = food.id,
                            name = food.name,
                            brand = food.brand,
                            protein = food.protein,
                            carbohydrate = food.carbohydrate,
                            fat = food.fat,
                            calories = food.calories,
                            units = food.units.map { unit ->
                                GetUnit(
                                    id = unit.id,
                                    name = unit.name,
                                    weight = unit.weight
                                )
                            }
                        )
                    }

                ))
        }
    }

    @PutMapping(Routes.Food.UPDATE)
    fun update(
        @PathVariable id: String
    ) : ResponseEntity<*> {
        val res = foodService.update(id.toInt())

        return when (res) {
            is Failure -> when (res.value){
                else ->  throw IllegalStateException("Expected to have an error here, but it was not defined yet!")
            }

            is Success -> ResponseEntity
                .status(HttpStatus.OK)
                .body(res.value)
        }
    }

    @DeleteMapping(Routes.Food.DELETE)
    fun delete(
        @PathVariable id: String
    ) : ResponseEntity<*> {
        val res = foodService.remove(id.toInt())

        return when (res) {
            is Failure -> when (res.value){
                else ->  throw IllegalStateException("Expected to have an error here, but it was not defined yet!")
            }

            is Success -> ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build<Unit>()
        }
    }
}