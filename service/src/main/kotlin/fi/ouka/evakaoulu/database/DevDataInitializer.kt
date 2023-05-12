// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ouka.evakaoulu.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript
import io.opentracing.noop.NoopTracerFactory
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DevDataInitializer(jdbi: Jdbi) {

    init {
        Database(jdbi, NoopTracerFactory.create()).connect { db ->
            db.transaction { tx ->
                tx.runDevScript("reset-oulu-database-for-e2e-tests.sql")
                tx.ensureOuluDevData()
            }
        }
    }
}
