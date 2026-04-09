package tiago.canilhas.foobs.http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class Problem(
    @Suppress("unused") message: String,
) {
    companion object {
        const val MEDIA_TYPE = "application/problem+json"

        fun res(
            status: HttpStatus,
            problem: Problem,
        ) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body<Any>(problem)

        /**
         *  Food related problems
         */

        val foodInvalidName = Problem("Food has an invalid name")
        val foodInvalidBrand = Problem("Food has an invalid brand")
        val foodInvalidProtein = Problem("Food has an invalid protein value")
        val foodInvalidCarbohydrate = Problem("Food has an invalid carbohydrate value")
        val foodInvalidFat = Problem("Food has an invalid fat value")
        val foodInvalidFiber = Problem("Food has an invalid fiber value")
        val foodNotFound = Problem("Food does not exist")


        /**
         * Meal related problemsk
         */

        val mealInvalidName = Problem("Meal has an invalid name")
    }

}