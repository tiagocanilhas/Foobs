package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin

fun Jdbi.addConfigurations(): Jdbi {
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())

    return this
}