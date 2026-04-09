package tiago.canilhas.foobs.repository

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.postgresql.ds.PGSimpleDataSource

private val jdbi =
    Jdbi
        .create( PGSimpleDataSource().apply { setURL("jdbc:postgresql://localhost:5433/db?user=dbuser&password=changeit") } )
        .apply {
            installPlugin(KotlinPlugin())
            installPlugin(PostgresPlugin())
        }

fun testWithHandleAndRollback(block: (Handle) -> Unit) =
    jdbi.useTransaction<Exception> { handle ->
        block(handle)
        handle.rollback()
    }

