package tiago.canilhas.foobs.repository.interfaces

interface ITransaction {
    val foodRepository : IFoodRepository
    val mealRepository : IMealRepository
}