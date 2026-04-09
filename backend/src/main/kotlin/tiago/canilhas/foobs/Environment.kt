package tiago.canilhas.foobs

object Environment {
    private const val KEY_DB_URL = "DB_URL"

    val DB_URL: String
        get() = System.getenv(KEY_DB_URL) ?: throw Exception("Missing env var $KEY_DB_URL")
}