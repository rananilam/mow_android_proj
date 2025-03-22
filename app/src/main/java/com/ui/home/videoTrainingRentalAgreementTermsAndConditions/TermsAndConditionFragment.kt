package com.ui.home.videoTrainingRentalAgreementTermsAndConditions

import android.os.Bundle
import android.view.View
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowTermsAndConditionsBinding
import iCode.android.BaseFragment
import iCode.utility.PrefHelper


class TermsAndConditionFragment : BaseFragment() {

    private val binding: FragMowTermsAndConditionsBinding by bindingLazy()
    override val layoutResId: Int
        get() = R.layout.frag_mow_terms_and_conditions

    private var termsAndConditionText = ""
    private var currentIndex_: Int = 0
    private var size_: Int = 0

    var agreeCallback: ((ActionButtonStatus) -> Unit)? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt("currentIndex")?.let { currentIndex ->
            currentIndex_ = currentIndex
            PrefHelper.getInstance().getString("termsAndConditionData", null)?.let {
                val responseObj = GsonInterface.getInstance().gson.fromJson(
                    it,
                    GetDeviceTrainingVideoAndDataRs::class.java
                )
                val deviceTrainingVideoData = responseObj.deviceTrainingVideoDataList[currentIndex]
                termsAndConditionText = deviceTrainingVideoData.termsAndConditionText
            }
        }

        arguments?.getInt("size")?.let { size ->
            size_ = size
        }

        if(currentIndex_ < size_-1) {
            binding.texViewAgree.text = getString(R.string.i_agree)
        } else {
            binding.texViewAgree.text = getString(R.string.i_agree_place_order)
        }



        binding.webViewContent.loadDataWithBaseURL(
            null,
            ("<style>img{display: inline;height: auto;max-width: 100%;}</style>" + termsAndConditionText),
            "text/html",
            "utf-8",
            null
        )


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

    companion object {

        fun newInstance(currentIndex: Int, size: Int): TermsAndConditionFragment {

            val bundle = Bundle()
            bundle.putInt("currentIndex", currentIndex)
            bundle.putInt("size", size)
            val termsAndConditionFragment = TermsAndConditionFragment()
            termsAndConditionFragment.arguments = bundle
            return termsAndConditionFragment
        }
    }
}