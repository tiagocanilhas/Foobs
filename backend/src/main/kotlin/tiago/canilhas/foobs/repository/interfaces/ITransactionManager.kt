package tiago.canilhas.foobs.repository.interfaces

interface ITransactionManager {
    fun <R> run(block: (ITransaction) -> R): R
}