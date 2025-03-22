package com.ui.home.videoTrainingRentalAgreementTermsAndConditions

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.DialogVideoRentalTermsConditionsBinding
import iCode.utility.PrefHelper


class VideoRentalTermsAndConditionsDialogFragment : DialogFragment() {

    private var fragments = mutableListOf<Fragment>()

    private lateinit var binding: DialogVideoRentalTermsConditionsBinding

    //private var deviceTrainingVideoDataList = listOf<DeviceTrainingVideoData>()

    private var currentIndex = 0
    private var size = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.IncidentCommentDialogStyle
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_video_rental_terms_conditions, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrefHelper.getInstance().getString("termsAndConditionData",null)?.let {
            val responseObj = GsonInterface.getInstance().gson.fromJson(
                it,
                GetDeviceTrainingVideoAndDataRs::class.java
            )
            size = responseObj.deviceTrainingVideoDataList.size
        }



        setupView()
        if(size >= 1) {
            setViewPager()
        } else {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
    private fun setupView() {


        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                if (position == 0) {
                    binding.textViewTitle.setText(R.string.device_training_video)
                } else if (position == 1) {
                    binding.textViewTitle.setText(R.string.rental_agreement)
                } else if (position == 2) {
                    binding.textViewTitle.setText(R.string.terms_and_conditions)
                } else {
                    binding.textViewTitle.setText(R.string.device_training_video)
                }
            }
        })

        binding.imageViewClose.setOnClickListener {
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
            dialog?.dismiss()
        }


    }

    private fun setViewPager() {

        fragments.clear()

        //val deviceTrainingVideoData = deviceTrainingVideoDataList[currentIndex]

        val deviceTrainingVideoFragment = DeviceTrainingVideoFragment.newInstance(currentIndex)
        deviceTrainingVideoFragment.agreeCallback = {

            when (it) {
                ActionButtonStatus.AGREE -> {
                    binding.viewpager.currentItem = binding.viewpager.currentItem + 1
                }
                ActionButtonStatus.CANCEL -> {
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    dialog?.dismiss()
                }
                else -> {}
            }


        }
        fragments.add(deviceTrainingVideoFragment)

        val rentalAgreementFragment = RentalAgreementFragment.newInstance(currentIndex)
        rentalAgreementFragment.agreeCallback = {


            when (it) {
                ActionButtonStatus.AGREE -> {
                    binding.viewpager.currentItem = binding.viewpager.currentItem + 1
                }
                ActionButtonStatus.CANCEL -> {
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    dialog?.dismiss()
                }
                ActionButtonStatus.BACK -> {
                    binding.viewpager.setCurrentItem(binding.viewpager.currentItem.minus(1), true)
                }
            }
        }
        fragments.add(rentalAgreementFragment)

        val termsAndConditionFragment = TermsAndConditionFragment.newInstance(currentIndex,size)
        termsAndConditionFragment.agreeCallback = {

            when (it) {
                ActionButtonStatus.AGREE -> {
                    if(currentIndex < size-1) {
                        currentIndex += 1
                        setViewPager()
                    } else {
                        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                        dialog?.dismiss()
                    }
                }
                ActionButtonStatus.CANCEL -> {
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    dialog?.dismiss()
                }
                ActionButtonStatus.BACK -> {
                    binding.viewpager.setCurrentItem(binding.viewpager.currentItem.minus(1), true)
                }
            }



        }
        fragments.add(termsAndConditionFragment)


        binding.viewpager.adapter = ViewPagerAdapter(childFragmentManager)
      //  binding.viewpager.offscreenPageLimit = 3


    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            Log.i("Nilam",""+(position))
            return fragments[position]
        }

        override fun getCount(): Int {
            Log.i("Nilam",""+(fragments.size))
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            Log.i("Nilam",""+(fragments[position].javaClass.name+","+currentIndex))
            return fragments[position].javaClass.name+","+currentIndex
        }
    }

    companion object {


        fun newInstance(): VideoRentalTermsAndConditionsDialogFragment {

          //  val bundle = Bundle()
           // bundle.putString("getDeviceTrainingVideoAndDataStrRs",getDeviceTrainingVideoAndDataStrRs)
          //  val videoRentalTermsAndConditionsDialogFragment = VideoRentalTermsAndConditionsDialogFragment()
          //  videoRentalTermsAndConditionsDialogFragment.arguments = bundle
            return VideoRentalTermsAndConditionsDialogFragment()
        }
    }
}