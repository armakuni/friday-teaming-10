class Account {
    private var transactions = Transactions()

    fun formatStatement(): String = // primitive String return required by the API
        transactions.printStatement(StringStatementPrinter())

    fun deposit(amount: Int) = transactions.add(Money(amount))
    fun withdraw(amount: Int) = transactions.add(Money(-amount))
}
