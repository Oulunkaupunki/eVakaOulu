package fi.ouka.evakaoulu.payment.service

import fi.ouka.evakaoulu.util.FinanceDateProvider
import org.junit.jupiter.api.Test

class ProEPaymentGeneratorTest {
    @Test
    fun `should not generate payment rows from payments with a negative amount`() {
        val proEPaymentGenerator = ProEPaymentGenerator(PaymentChecker(), FinanceDateProvider(), BicMapper())

        val validPayment = validPayment()
        val otherPaymentUnit = validPaymentUnit().copy(providerId = "OTHERPROVIDERID")
        val negativePayment = validPayment().copy(unit = otherPaymentUnit, amount = -10000)

        val result = proEPaymentGenerator.generatePayments(listOf(validPayment, negativePayment))

        assert(!result.paymentString.contains("OTHERPROVIDERID"))
    }

    @Test
    fun `should not generate payment rows from payments with a zero amount`() {
        val proEPaymentGenerator = ProEPaymentGenerator(PaymentChecker(), FinanceDateProvider(), BicMapper())

        val validPayment = validPayment()
        val otherPaymentUnit = validPaymentUnit().copy(providerId = "OTHERPROVIDERID")
        val negativePayment = validPayment().copy(unit = otherPaymentUnit, amount = 0)

        val result = proEPaymentGenerator.generatePayments(listOf(validPayment, negativePayment))

        assert(!result.paymentString.contains("OTHERPROVIDERID"))
    }

    @Test
    fun `should include payments with negative amounts in the success list`() {
        val proEPaymentGenerator = ProEPaymentGenerator(PaymentChecker(), FinanceDateProvider(), BicMapper())

        val validPayment = validPayment()
        val otherPaymentUnit = validPaymentUnit().copy(providerId = "OTHERPROVIDERID")
        val negativePayment = validPayment().copy(unit = otherPaymentUnit, amount = -10000)

        val result = proEPaymentGenerator.generatePayments(listOf(validPayment, negativePayment))

        assert(result.sendResult.succeeded.containsAll(listOf(validPayment, negativePayment)))
    }

    @Test
    fun `should include payments with zero amounts in the success list`() {
        val proEPaymentGenerator = ProEPaymentGenerator(PaymentChecker(), FinanceDateProvider(), BicMapper())

        val validPayment = validPayment()
        val otherPaymentUnit = validPaymentUnit().copy(providerId = "OTHERPROVIDERID")
        val negativePayment = validPayment().copy(unit = otherPaymentUnit, amount = 0)

        val result = proEPaymentGenerator.generatePayments(listOf(validPayment, negativePayment))

        assert(result.sendResult.succeeded.containsAll(listOf(validPayment, negativePayment)))
    }
}
