package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.Handle
import tiago.canilhas.foobs.repository.interfaces.IFoodRepository
import tiago.canilhas.foobs.repository.interfaces.IMealRepository
import tiago.canilhas.foobs.repository.interfaces.ITransaction

class Transaction(
    handle: Handle,
) : ITransaction {
    override val foodRepository: IFoodRepository = FoodRepository(handle)
    override val mealRepository: IMealRepository = MealRepository(handle)
}