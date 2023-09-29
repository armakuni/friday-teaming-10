class Transactions {
    private var transactions: List<Transaction> = mutableListOf()

    fun add(transaction: Transaction) {
        transactions += transaction
    }

    fun printStatement(statementPrinter: StatementPrinter): String = statementPrinter.print(transactions)
}
