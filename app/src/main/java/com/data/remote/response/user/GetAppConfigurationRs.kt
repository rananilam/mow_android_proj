package com.data.remote.response.user

import com.data.model.user.Config
import com.data.remote.response.BaseRs

class GetAppConfigurationRs : BaseRs() {
    var config: Config? = null
}