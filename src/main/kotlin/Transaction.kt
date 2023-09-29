import java.util.*

enum class Direction {
    Deposit,
    Withdrawal
}

data class Transaction(val amount: Money, val date: Date, val direction: Direction) {
    override fun toString(): String {
        return "{date} {symbol}{amount}"
    }
}
