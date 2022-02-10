// SPDX-FileCopyrightText: 2021 City of Oulu
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import type { Config } from '@jest/types'

const config: Config.InitialOptions = {
  displayName: 'e2e-playwright',
  preset: 'ts-jest',
  testEnvironment: '../../../evaka/frontend/src/e2e-test/jest-environment',
  testRunner: 'jest-circus/runner',
  moduleNameMapper: {
    '^e2e/(.*)$': '<rootDir>/$1',
    '^@evaka/customizations/(.*)': '<rootDir>/../../oulu/$1',
    '^Icons$': '<rootDir>/../../../evaka/frontend/src/lib-icons/free-icons',
    '\.(jpg|jpeg|png|svg)$': '<rootDir>/../../../evaka/frontend/assetsTransformer.js'  
  }
}
export default config
