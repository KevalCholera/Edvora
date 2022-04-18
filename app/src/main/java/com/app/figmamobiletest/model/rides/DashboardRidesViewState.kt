package com.app.figmamobiletest.model.rides

import com.app.figmamobiletest.base.BaseViewState
import com.app.figmamobiletest.base.Status

class DashboardRidesViewState(
    val status: Status,
    val error: String? = null,
    val response: DashboardRidesResponse? = null
) : BaseViewState(status, error)