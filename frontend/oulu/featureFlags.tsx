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
    environmentLabel: 'Kehitys',
    citizenShiftCareAbsence: false,
    assistanceActionOther: false,
    financeDecisionHandlerSelect: false,
    feeDecisionPreschoolClubFilter: true,
    daycareApplication: {
      dailyTimes: true,
      serviceNeedOption: true
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: false,
      serviceNeedOption: false
    },
    decisionDraftMultipleUnits: false,
    preschool: true,
    urgencyAttachments: true,
    preparatory: true,
    placementGuarantee: false,
    voucherUnitPayments: true,
    assistanceNeedDecisionsLanguageSelect: false,
    staffAttendanceTypes: true,
    extendedPreschoolTerm: false,
    intermittentShiftCare: false,
    personDuplicate: false,
    citizenAttendanceSummary: false,
    noAbsenceType: false,
    discussionReservations: true,
    forceUnpublishDocumentTemplate: true,
    serviceApplications: false,
    titaniaErrorsReport: true,
    calendarMonthView: true,
  },
  staging: {
    environmentLabel: 'Staging',
    citizenShiftCareAbsence: false,
    assistanceActionOther: false,
    daycareApplication: {
      dailyTimes: true,
      serviceNeedOption: true
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
    placementGuarantee: false,
    voucherUnitPayments: true,
    assistanceNeedDecisionsLanguageSelect: false,
    staffAttendanceTypes: true,
    extendedPreschoolTerm: false,
    intermittentShiftCare: false,
    personDuplicate: false,
    citizenAttendanceSummary: false,
    noAbsenceType: false,
    discussionReservations: true,
    forceUnpublishDocumentTemplate: true,
    serviceApplications: false,
    titaniaErrorsReport: true,
    calendarMonthView: true,
  },
  prod: {
    environmentLabel: null,
    citizenShiftCareAbsence: false,
    assistanceActionOther: false,
    daycareApplication: {
      dailyTimes: true,
      serviceNeedOption: true
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
    placementGuarantee: false,
    voucherUnitPayments: true,
    assistanceNeedDecisionsLanguageSelect: false,
    staffAttendanceTypes: true,
    extendedPreschoolTerm: false,
    intermittentShiftCare: false,
    personDuplicate: false,
    citizenAttendanceSummary: false,
    noAbsenceType: false,
    discussionReservations: false,
    forceUnpublishDocumentTemplate: false,
    serviceApplications: false,
    titaniaErrorsReport: true,
    calendarMonthView: true,
  }
}

const featureFlags = features[env()]

export default featureFlags
