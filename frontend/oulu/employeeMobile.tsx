{
  /*
SPDX-FileCopyrightText: 2021 City of Oulu

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { EmployeeMobileCustomizations } from 'lib-customizations/types'

import featureFlags from './featureFlags'

const customizations: EmployeeMobileCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      mobile: {
        landerText1: 'Tervetuloa käyttämään eVaka Oulu -mobiilisovellusta!'
      }
    }
  },
  featureFlags
}

export default customizations
