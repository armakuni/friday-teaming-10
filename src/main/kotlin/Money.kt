data class Money(private val amount: Int) {
    override fun toString(): String = "$amount"
    fun toStringWithDirectionSymbol(): String {
        return if (amount > 0) {
            "+$amount"
        } else {
            "$amount"
        }
    }
    fun add(otherAmount: Money): Money = Money(amount + otherAmount.amount)
}
