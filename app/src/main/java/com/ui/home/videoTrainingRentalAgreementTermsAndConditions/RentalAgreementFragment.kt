package com.ui.home.videoTrainingRentalAgreementTermsAndConditions

import android.os.Bundle
import android.util.Base64
import android.view.View
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowRentalAgreementBinding
import iCode.android.BaseFragment
import iCode.utility.PrefHelper

class RentalAgreementFragment : BaseFragment() {

    private val binding : FragMowRentalAgreementBinding by bindingLazy()
    override val layoutResId: Int
        get() = R.layout.frag_mow_rental_agreement

    private var rentalAgreementText = ""

    var agreeCallback: ((ActionButtonStatus) -> Unit)? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        arguments?.getInt("currentIndex")?.let {currentIndex->

            PrefHelper.getInstance().getString("termsAndConditionData",null)?.let {
                val responseObj = GsonInterface.getInstance().gson.fromJson(
                    it,
                    GetDeviceTrainingVideoAndDataRs::class.java
                )
                val deviceTrainingVideoData = responseObj.deviceTrainingVideoDataList[currentIndex]
                rentalAgreementText = deviceTrainingVideoData.rentalAgreementText
            }
        }

        binding.webViewContent.loadDataWithBaseURL(null,("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + rentalAgreementText),"text/html", "utf-8", null)
        /*binding.webViewContent.loadData(
            Base64.encodeToString(
                ("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + rentalAgreementText).toByteArray(), Base64.NO_PADDING
            ), "text/html", "base64"
        )*/

        binding.texViewCancel.setOnClickListener {
            agreeCallback?.invoke(ActionButtonStatus.CANCEL)
        }
        binding.texViewBack.setOnClickListener {
            agreeCallback?.invoke(ActionButtonStatus.BACK)
        }
        binding.texViewAgree.setOnClickListener {
            agreeCallback?.invoke(ActionButtonStatus.AGREE)
        }
    }

    companion object{


        fun newInstance(currentIndex: Int): RentalAgreementFragment {

            val bundle = Bundle()
            bundle.putInt("currentIndex", currentIndex)
            val rentalAgreementFragment = RentalAgreementFragment()
            rentalAgreementFragment.arguments = bundle
            return rentalAgreementFragment
        }
    }
}