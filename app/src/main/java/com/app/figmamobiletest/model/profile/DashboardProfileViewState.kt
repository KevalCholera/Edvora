package com.app.figmamobiletest.model.profile

import com.app.figmamobiletest.base.BaseViewState
import com.app.figmamobiletest.base.Status

class DashboardProfileViewState(
    val status: Status,
    val error: String? = null,
    val response: DashboardProfileResponse? = null
) : BaseViewState(status, error)