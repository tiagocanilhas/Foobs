package tiago.canilhas.foobs.domain

import org.springframework.stereotype.Component

@Component
class MealDomain {

    fun isNameValid(name: String): Boolean {
        return name.length > 2
    }

    fun isMinCaloriesValid(minCalories: Int): Boolean {
        return minCalories >= 1
    }
}