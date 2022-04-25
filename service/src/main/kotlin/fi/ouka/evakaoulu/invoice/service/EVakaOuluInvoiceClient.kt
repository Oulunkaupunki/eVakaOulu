// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ouka.evakaoulu.invoice.service

import com.jcraft.jsch.SftpException
import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient


class EVakaOuluInvoiceClient(
    private val invoiceSender: InvoiceSender,
    private val invoiceGenerator: ProEInvoiceGenerator
) : InvoiceIntegrationClient {
    override fun send(invoices: List<InvoiceDetailed>): InvoiceIntegrationClient.SendResult {
        val successList = mutableListOf<InvoiceDetailed>()
        val failedList = mutableListOf<InvoiceDetailed>()
        val manuallySentList = mutableListOf<InvoiceDetailed>()
        var proEinvoices = ""

        val (withSSN, withoutSSN) = invoices.partition { invoice -> invoice.headOfFamily.ssn != null }

        withSSN.forEach {
            proEinvoices += invoiceGenerator.generateInvoice(it)
            successList.add(it)
        }

        try {
            invoiceSender.send(proEinvoices)
            manuallySentList.addAll(withoutSSN)
        } catch (e: SftpException){
            failedList.addAll(successList)
            failedList.addAll(withoutSSN)
            successList.clear()
        }

        return InvoiceIntegrationClient.SendResult(successList, failedList, manuallySentList)
    }

}

interface StringInvoiceGenerator {
    fun generateInvoice(invoice: InvoiceDetailed): String

}