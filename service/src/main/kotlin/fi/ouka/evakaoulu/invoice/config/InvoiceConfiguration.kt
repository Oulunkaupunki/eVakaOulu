// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ouka.evakaoulu.invoice.config

import fi.espoo.evaka.invoicing.domain.FeeAlteration
import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.*
import fi.espoo.evaka.placement.PlacementType
import fi.ouka.evakaoulu.EvakaOuluProperties
import fi.ouka.evakaoulu.invoice.service.EVakaOuluInvoiceClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("evakaoulu")
@Configuration
class InvoiceConfiguration {
    @Primary
    @Bean(name = ["evakaOuluInvoiceIntegrationClient"])
    fun invoiceIntegrationClient(
        properties: EvakaOuluProperties
    ): InvoiceIntegrationClient = EVakaOuluInvoiceClient(properties.intime)

    @Bean
    fun incomeTypesProvider(): IncomeTypesProvider = OuluIncomeTypesProvider()

    @Bean
    fun invoiceProductProvider(): InvoiceProductProvider = OuluInvoiceProductProvider()
}

class OuluIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        return linkedMapOf(
            "MAIN_INCOME" to IncomeType("Palkkatulo", 1, false, false),
            "HOLIDAY_BONUS" to IncomeType("Lomaraha", 1, false, false),
            "PERKS" to IncomeType("Luontaisetu", 1, false, false),
            "DAILY_ALLOWANCE" to IncomeType("Päiväraha", 1, true, false),
            "HOME_CARE_ALLOWANCE" to IncomeType("Kotihoidontuki", 1, false, false),
            "PENSION" to IncomeType("Eläke", 1, false, false),
            "RELATIVE_CARE_SUPPORT" to IncomeType("Omaishoidontuki", 1, false, false),
            "STUDENT_INCOME" to IncomeType("Opiskelijan tulot", 1, false, false),
            "GRANT" to IncomeType("Apuraha", 1, false, false),
            "STARTUP_GRANT" to IncomeType("Starttiraha", 1, true, false),
            "BUSINESS_INCOME" to IncomeType("Yritystoiminnan tulo", 1, false, false),
            "CAPITAL_INCOME" to IncomeType("Pääomatulo", 1, false, false),
            "RENTAL_INCOME" to IncomeType("Vuokratulot", 1, false, false),
            "PAID_ALIMONY" to IncomeType("Maksetut elatusavut", -1, false, false),
            "ALIMONY" to IncomeType("Saadut elatusavut", 1, false, false),
            "OTHER_INCOME" to IncomeType("Muu tulo", 1, true, false),
            "ADJUSTED_DAILY_ALLOWANCE" to IncomeType("Soviteltu päiväraha", 1, true, false),
        )
    }
}

class OuluInvoiceProductProvider : InvoiceProductProvider {

    override val products = Product.values().map { ProductWithName(it.key, it.nameFi) }
    override val dailyRefund = Product.FREE_OF_CHARGE.key
    override val partMonthSickLeave = Product.SICK_LEAVE_50.key
    override val fullMonthSickLeave = Product.SICK_LEAVE_100.key
    override val fullMonthAbsence = Product.ABSENCE.key

    override fun mapToProduct(placementType: PlacementType): ProductKey {
        val product = when (placementType) {
            PlacementType.DAYCARE, PlacementType.DAYCARE_PART_TIME, PlacementType.DAYCARE_FIVE_YEAR_OLDS, PlacementType.DAYCARE_PART_TIME_FIVE_YEAR_OLDS -> Product.DAYCARE
            PlacementType.PRESCHOOL_DAYCARE -> Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.PREPARATORY_DAYCARE -> Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.TEMPORARY_DAYCARE, PlacementType.TEMPORARY_DAYCARE_PART_DAY -> Product.TEMPORARY_CARE
            PlacementType.SCHOOL_SHIFT_CARE -> Product.SCHOOL_SHIFT_CARE
            PlacementType.PRESCHOOL, PlacementType.PREPARATORY, PlacementType.CLUB -> error("No product mapping found for placement type $placementType")
        }
        return product.key
    }

    override fun mapToFeeAlterationProduct(productKey: ProductKey, feeAlterationType: FeeAlteration.Type): ProductKey {
        val product = when (findProduct(productKey) to feeAlterationType) {
            Product.DAYCARE to FeeAlteration.Type.DISCOUNT, Product.DAYCARE to FeeAlteration.Type.RELIEF, Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.DISCOUNT, Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.RELIEF -> Product.DAYCARE_DISCOUNT
            Product.DAYCARE to FeeAlteration.Type.INCREASE, Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.INCREASE -> Product.CORRECTION
            else -> error("No product mapping found for product + fee alteration type combo ($productKey + $feeAlterationType)")
        }
        return product.key
    }

}

fun findProduct(key: ProductKey) = Product.values().find { it.key == key } ?: error("Product with key $key not found")

enum class Product(val nameFi: String, val code: String) {
    DAYCARE("Varhaiskasvatus", "500218"), DAYCARE_DISCOUNT(
        "Alennus", "500687"
    ),
    PRESCHOOL_WITH_DAYCARE(
        "Esiopetusta täydentävä varhaiskasvatus", "500220"
    ),
    TEMPORARY_CARE("Tilapäinen varhaiskasvatus", "500576"), SCHOOL_SHIFT_CARE(
        "Koululaisen vuorohoito", "500949"
    ),
    SICK_LEAVE_50("Laskuun vaikuttava poissaolo 50%", "500283"), SICK_LEAVE_100(
        "Laskuun vaikuttava poissaolo 100%", "500248"
    ),
    ABSENCE("Poissaolovähennys 50%", "500210"), FREE_OF_CHARGE("Maksuton päivä", "503696"), CORRECTION(
        "Oikaisu", "500177"
    ),
    FREE_MONTH("Maksuton kuukausi", "500156"), OVER_CONTRACT(
        "Sopimuksen ylitys", "500538"
    ),
    UNANNOUNCED_ABSENCE("Ilmoittamaton päivystysajan poissaolo", "507292");

    val key = ProductKey(this.name)
}
