class StringStatementPrinter : StatementPrinter {
    override fun print(transactions: List<Transaction>): String {
        var statement = "Date Amount Balance"
        var runningTotal = Money(0)
        for (transaction in transactions) {
            runningTotal = runningTotal.add(transaction)
            statement += "\n24/12/2015 ${transaction} $runningTotal"
        }
        return statement
    }
}
