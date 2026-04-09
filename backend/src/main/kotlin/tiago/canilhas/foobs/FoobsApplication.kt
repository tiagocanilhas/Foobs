package tiago.canilhas.foobs

import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import tiago.canilhas.foobs.repository.addConfigurations
import kotlin.apply

@SpringBootApplication
class FoobsApplication {
	@Bean
	fun jdbi() = Jdbi
		.create(PGSimpleDataSource().apply { setUrl(Environment.DB_URL) })
		.addConfigurations()
}

fun main(args: Array<String>) {
	runApplication<FoobsApplication>(*args)
}


