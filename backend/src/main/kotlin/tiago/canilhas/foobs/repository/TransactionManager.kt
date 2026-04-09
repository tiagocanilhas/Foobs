package tiago.canilhas.foobs.repository

import tiago.canilhas.foobs.repository.interfaces.ITransaction
import tiago.canilhas.foobs.repository.interfaces.ITransactionManager

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class TransactionManager (
    private val jdbi: Jdbi,
): ITransactionManager {
    override fun <R> run(block: (ITransaction) -> R): R =
        jdbi.inTransaction <R, Exception> { handle ->
            val transaction = Transaction(handle)
            block(transaction)
        }
}