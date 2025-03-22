package com.ui.home.destination

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.destination.Location
import com.data.remote.response.destination.GetDestinationListRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class DestinationFragmentPresenter(
    val destinationView: DestinationFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<DestinationFragmentContract.View>(), DestinationFragmentContract.Presenter {




    override fun getDestinationList() {


        destinationView.setProgressBar(true)


        dataRepository.getDestinationList(object : CallbackSubscriber<GetDestinationListRs>() {
            override fun onSuccess(response: GetDestinationListRs?) {

                destinationView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        if(response.destinationList.isNotEmpty()) {
                            destinationView.showDestinationList(response.destinationList)
                        } else {
                            destinationView.getDestinationListFail(response.result.message)
                        }
                    } else {
                        destinationView.getDestinationListFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                destinationView.setProgressBar(false)
                destinationView.processErrorWithToast(restError)
            }
        })


    }

    override fun setDestination(location: Location) {
        dataRepository.locationId = location.id
        dataRepository.locationName = location.locationName
        dataRepository.locationCategory = location.locationCategoryName
    }
}


