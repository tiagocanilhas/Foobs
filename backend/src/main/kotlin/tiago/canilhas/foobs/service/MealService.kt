package tiago.canilhas.foobs.service

import org.springframework.stereotype.Component
import tiago.canilhas.foobs.domain.Either
import tiago.canilhas.foobs.domain.Meal
import tiago.canilhas.foobs.domain.MealDomain
import tiago.canilhas.foobs.domain.MealFoodInfo
import tiago.canilhas.foobs.domain.SortDirection
import tiago.canilhas.foobs.domain.SortValue
import tiago.canilhas.foobs.domain.failure
import tiago.canilhas.foobs.domain.success
import tiago.canilhas.foobs.repository.TransactionManager

@Component
class MealService(
    private val transactionManager: TransactionManager,
    private val mealDomain: MealDomain
) {

    sealed class CreateError {
        object InvalidName : CreateError()
    }
    typealias CreateResult = Either<CreateError, Meal>

    fun create(
        name: String,
        foods: List<MealFoodInfo>
    ): CreateResult {
        if (!mealDomain.isNameValid(name)) return failure(CreateError.InvalidName)

        return transactionManager.run {
            val mealRepository = it.mealRepository

            val meal = mealRepository.create(name)

            if (foods.isNotEmpty()) mealRepository.insertFoods(meal.id, foods)

            success(meal)
        }
    }



    sealed class GetError{
        data object NotFound:GetError()

    }
    typealias GetResult = Either<GetError, Meal>

    fun get(
        id: Int
    ): GetResult {
        return transactionManager.run {
            val mealRepository = it.mealRepository

            val meal = mealRepository.get(id) ?: return@run failure(GetError.NotFound)

            success(meal)
        }
    }



    sealed class GetMultipleError {
        object InvalidMinCalories: GetMultipleError()

    }
    typealias GetMultipleResult = Either<GetMultipleError, List<Meal>>

    fun getMultiple(
        name: String? = null,
        minCalories: Int? = null,
        maxCalories: Int? = null,
        sortValue: SortValue? = null,
        sortDirection: SortDirection? = null,
    ): GetMultipleResult {

        minCalories?.let {
            if (!mealDomain.isMinCaloriesValid(minCalories)) return failure(GetMultipleError.InvalidMinCalories)
        }

        return transactionManager.run {
            val mealRepository = it.mealRepository

            val meals = mealRepository.getMultiple(name, minCalories, maxCalories, sortValue, sortDirection)

            success(meals)
        }
    }



    sealed class UpdateError{

    }
    typealias UpdateResult = Either<UpdateError, Meal>

    fun update(
        id: Int,
    ): UpdateResult {
        return transactionManager.run {
            val mealRepository = it.mealRepository

            success(mealRepository.update(id))
        }
    }


    sealed class DeleteError{

    }
    typealias DeleteResult = Either<DeleteError, Unit>

    fun remove(id: Int): DeleteResult {
        return transactionManager.run {
            val mealRepository = it.mealRepository

            success(mealRepository.delete(id))
        }
    }
}