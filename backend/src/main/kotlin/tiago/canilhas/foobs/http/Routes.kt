package tiago.canilhas.foobs.http

import org.springframework.web.util.UriTemplate
import java.net.URI

object Routes {
    private const val BASE_URL = "/api"

    object Food {
        private const val URL = "$BASE_URL/food"

        const val CREATE = URL                  // POST
        const val GET = "$URL/{id}"             // GET
        const val GET_MULTIPLE = URL            // GET
        const val UPDATE = "$URL/{id}"          // PUT
        const val DELETE = "$URL/{id}"          // DELETE

        fun byId(id: Int): URI = UriTemplate(GET).expand(id)
    }

    object Meal {
        private const val URL = "$BASE_URL/meal"

        const val CREATE = URL                  // POST
        const val GET = "$URL/{id}"             // GET
        const val GET_MULTIPLE = URL            // GET
        const val UPDATE = "$URL/{id}"          // PUT
        const val DELETE = "$URL/{id}"          // DELETE

        fun byId(id: Int): URI = UriTemplate(Food.GET).expand(id)
    }
}