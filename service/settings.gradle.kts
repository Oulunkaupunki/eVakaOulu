// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

rootProject.name = "evakaoulu-service"
includeBuild("../evaka/service")
includeBuild("../evaka/evaka-bom")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../evaka/service/gradle/libs.versions.toml"))
        }
    }
}
