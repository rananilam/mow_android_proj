package com.data.remote.response.order

import com.data.remote.response.BaseRs

class GetProcessingFeeRs : BaseRs() {

    var fee_figure = 0F
    var fee_type = 0
    var is_card_processsing_fee_required = true
    var is_eCheck_processsing_fee_required = false
    var note = ""
    var processing_fee_amount = 0F


}
