package com.ui.home.destination

import com.data.model.destination.Location
import com.ui.core.IBasePresenter
import libraries.retrofit.RestError


interface DestinationFragmentContract {

    interface View {
        fun showDestinationList(locationList: List<Location>)
        fun getDestinationListFail(errorMessage: String?)
        fun setProgressBar(show: Boolean)
        fun processErrorWithToast(restError: RestError?)

    }

    interface Presenter : IBasePresenter<View> {

        fun getDestinationList()
        fun setDestination(location: Location)

    }
}