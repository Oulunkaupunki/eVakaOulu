{
  /*
SPDX-FileCopyrightText: 2021 City of Oulu

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { FeatureFlags } from 'lib-customizations/types'

import { env, Env } from './env'

type Features = {
  default: FeatureFlags
} & {
  [k in Env]: FeatureFlags
}

const features: Features = {
  default: {
    citizenShiftCareAbsence: false,
    assistanceActionOther: true,
    financeDecisionHandlerSelect: false,
    feeDecisionPreschoolClubFilter: true,
    daycareApplication: {
      dailyTimes: true
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: false,
      serviceNeedOption: false
    },
    decisionDraftMultipleUnits: false,
    preschool: true,
    urgencyAttachments: true,
    preparatory: true,
    childDiscussion: true,
    experimental: {
      leops: true,
      voucherUnitPayments: true,
      assistanceNeedDecisions: true,
      assistanceNeedDecisionsLanguageSelect: false,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      childDocuments: true,
      intermittentShiftCare: false,
      assistanceNeedPreschoolDecisions: true
    }
  },
  staging: {
    citizenShiftCareAbsence: false,
    assistanceActionOther: true,
    daycareApplication: {
      dailyTimes: true
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: false,
      serviceNeedOption: false
    },
    decisionDraftMultipleUnits: false,
    preschool: true,
    urgencyAttachments: true,
    preparatory: true,
    financeDecisionHandlerSelect: false,
    feeDecisionPreschoolClubFilter: true,
    childDiscussion: true,
    experimental: {
      leops: true,
      voucherUnitPayments: true,
      assistanceNeedDecisions: true,
      assistanceNeedDecisionsLanguageSelect: false,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      childDocuments: true,
      intermittentShiftCare: false,
      assistanceNeedPreschoolDecisions: true
    }
  },
  prod: {
    citizenShiftCareAbsence: false,
    assistanceActionOther: true,
    daycareApplication: {
      dailyTimes: true
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: false,
      serviceNeedOption: false
    },
    decisionDraftMultipleUnits: false,
    preschool: true,
    urgencyAttachments: true,
    preparatory: true,
    financeDecisionHandlerSelect: false,
    feeDecisionPreschoolClubFilter: true,
    childDiscussion: true,
    experimental: {
      leops: true,
      voucherUnitPayments: true,
      assistanceNeedDecisions: true,
      assistanceNeedDecisionsLanguageSelect: false,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      childDocuments: true,
      intermittentShiftCare: false,
      assistanceNeedPreschoolDecisions: false
    }
  }
}

const featureFlags = features[env()]

export default featureFlags
