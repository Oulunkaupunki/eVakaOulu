// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ouka.evakaoulu.person.config

import com.github.kittinunf.fuel.core.FuelManager
import fi.ouka.evakaoulu.EvakaOuluProperties
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ws.transport.WebServiceMessageSender
import org.springframework.ws.transport.http.HttpComponentsMessageSender
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor

const val HTTP_CLIENT_PERSON = "httpClientPerson"

@Configuration
class PersonConfiguration {

    /**
     * Custom [WebServiceMessageSender] for [fi.espoo.evaka.vtjclient.config.XroadSoapClientConfig.wsTemplate].
     */
    @Bean
    fun webServiceMessageSender(@Qualifier(HTTP_CLIENT_PERSON) httpClient: HttpClient): WebServiceMessageSender {
        return HttpComponentsMessageSender(httpClient)
    }

    @Bean(HTTP_CLIENT_PERSON)
    fun httpClient(properties: EvakaOuluProperties) = HttpClientBuilder.create()
        .addInterceptorFirst(RemoveSoapHeadersInterceptor())
//        .addInterceptorFirst(basicAuthInterceptor(properties.ipaas.username, properties.ipaas.password))
        .build()

    /**
     * Custom [FuelManager] for [fi.espoo.evaka.dvv.DvvModificationsServiceClient].
     */
    @Bean
    fun fuelManager(properties: EvakaOuluProperties) = FuelManager()
}
