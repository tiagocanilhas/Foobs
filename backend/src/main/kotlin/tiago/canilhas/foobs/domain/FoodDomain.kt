package tiago.canilhas.foobs.domain

import org.springframework.stereotype.Component

@Component
class FoodDomain {

    fun isNameValid(name: String): Boolean {
        return name.length > 2
    }

    fun isBrandValid(brand: String): Boolean {
        return brand.length > 1
    }

    fun isNutritionValueValid(nutrition: Double): Boolean {
        return nutrition >= 0
    }
}