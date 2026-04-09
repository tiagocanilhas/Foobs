package tiago.canilhas.foobs.service

import org.springframework.stereotype.Component
import tiago.canilhas.foobs.domain.Either
import tiago.canilhas.foobs.domain.Food
import tiago.canilhas.foobs.domain.FoodDomain
import tiago.canilhas.foobs.domain.FoodUnitInfo
import tiago.canilhas.foobs.domain.failure
import tiago.canilhas.foobs.domain.success
import tiago.canilhas.foobs.repository.TransactionManager

@Component
class FoodService (
    private val transactionManager: TransactionManager,
    private val foodDomain: FoodDomain
) {

    sealed class CreateError {
        object InvalidName : CreateError()
        object InvalidBrand : CreateError()
        object InvalidProtein : CreateError()
        object InvalidCarbohydrate : CreateError()
        object InvalidFat : CreateError()
        object InvalidFiber : CreateError()
    }
    private typealias CreateResult = Either<CreateError, Food>

    fun create(
        name: String,
        brand: String?,
        protein: Double,
        carbohydrate: Double,
        fat: Double,
        fiber: Double,
        units: List<FoodUnitInfo>
    ): CreateResult {
        if (!foodDomain.isNameValid(name)) return failure(CreateError.InvalidName)
        if (brand != null && !foodDomain.isBrandValid(brand)) return failure(CreateError.InvalidBrand)

        listOf(
            protein to CreateError.InvalidProtein,
            carbohydrate to CreateError.InvalidCarbohydrate,
            fat to CreateError.InvalidFat,
            fiber to CreateError.InvalidFiber
        ).forEach { (v, e) ->
            if (!foodDomain.isNutritionValueValid(v)) return failure(e)
        }

        return transactionManager.run {
            val foodRepository = it.foodRepository

            val food = foodRepository.create(
                name, brand, protein, carbohydrate, fat, fiber
            )

            if (units.isNotEmpty()) foodRepository.insertUnits(food.id, units)

            success(food)
        }
    }

    sealed class GetError{
        data object NotFound:GetError()

    }
   private typealias GetResult = Either<GetError, Food>

    fun get(id: Int): GetResult {
        return transactionManager.run {
            val foodRepository = it.foodRepository

            val food = foodRepository.get(id) ?: return@run failure(GetError.NotFound)

            success(food)
        }
    }



    sealed class GetMultipleError {

    }
    private typealias GetMultipleResult = Either<GetMultipleError, List<Food>>

    fun getMultiple(
        name: String?
    ): GetMultipleResult {
        return transactionManager.run {
            val foodRepository = it.foodRepository

            val foods = foodRepository.getMultiple(name)

            success(foods)
        }
    }



    sealed class UpdateError{

    }
    private typealias UpdateResult = Either<UpdateError, Food>

    fun update(
        id: Int,
    ) : UpdateResult {
        return transactionManager.run {
            val foodRepository = it.foodRepository

            success(foodRepository.update(id))
        }
    }



    sealed class RemoveError{

    }
    private typealias RemoveResult = Either<RemoveError, Unit>

    fun remove(id: Int) : RemoveResult {
        return transactionManager.run {
            val foodRepository = it.foodRepository

            success(foodRepository.delete(id))
        }
    }
}