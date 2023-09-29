import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun statementIsEmptyByDefault() {

        val account = Account()
        val statement = account.formatStatement()

        assertEquals("Date Amount Balance", statement)
    }

    @Test
    fun statementHasOneTransactionAfterDeposit() {

        val account = Account()
        account.deposit(500)

        val statement = account.formatStatement()
        assertEquals(
            """
            Date Amount Balance
            24/12/2015 +500 500
            """.trimIndent(), statement
        )
    }

    @Test
    fun statementHasTwoTransactionsAfterTwoDeposits() {
        val account = Account()
        account.deposit(500)
        account.deposit(100)

        val statement = account.formatStatement()
        assertEquals(
            """
            Date Amount Balance
            24/12/2015 +500 500
            24/12/2015 +100 600
            """.trimIndent(), statement
        )
    }

    @Test
    fun statementHasTransactionsAfterTwoDepositsAndOneWithdrawal() {
        val account = Account()
        account.deposit(500)
        account.deposit(100)
        account.withdraw(200)

        val statement = account.formatStatement()
        assertEquals(
            """
            Date Amount Balance
            24/12/2015 +500 500
            24/12/2015 +100 600
            24/12/2015 -200 400
            """.trimIndent(), statement
        )
    }
}
