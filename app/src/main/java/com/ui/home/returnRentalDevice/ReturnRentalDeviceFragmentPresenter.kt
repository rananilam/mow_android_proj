package com.ui.home.returnRentalDevice

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.request.order.ImageUploadedForReturnDeviceRq
import com.data.remote.response.order.ImageUploadedForReturnDeviceRs
import com.ui.core.BasePresenter
import com.util.Utility
import libraries.image.helper.models.MediaResult
import libraries.image.helper.utils.StorageUtility
import libraries.retrofit.RestError

class ReturnRentalDeviceFragmentPresenter(
    val returnRentalDeviceView: ReturnRentalDeviceFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<ReturnRentalDeviceFragmentContract.View>(),
    ReturnRentalDeviceFragmentContract.Presenter {

    override fun imageUploadedForReturnDevice(orderId: Int, mediaResult: MediaResult) {

        val request = ImageUploadedForReturnDeviceRq(orderId,
            Utility.convertImageIntoBase64(mediaResult.path),
            mediaResult.mimeType,
            StorageUtility.getInstance().getFileNameWithExtenstion(mediaResult.path)
        )
        returnRentalDeviceView.setProgressBar(true)
        dataRepository.imageUploadedForReturnDevice(request, object : CallbackSubscriber<ImageUploadedForReturnDeviceRs>() {
            override fun onSuccess(response: ImageUploadedForReturnDeviceRs?) {

                returnRentalDeviceView.setProgressBar(false)



                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {
                        returnRentalDeviceView.imageUploadedForReturnDeviceSuccessfully()
                    } else {
                        returnRentalDeviceView.imageUploadedForReturnDeviceFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                returnRentalDeviceView.setProgressBar(false)
                returnRentalDeviceView.processErrorWithToast(restError)
            }
        })
    }


}


