package com.ui.home.destination

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.data.Injection
import com.data.model.destination.Location
import com.google.android.material.snackbar.Snackbar
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowDestinationBinding
import com.ui.home.HomeActivity
import com.ui.login.LoginActivity
import iCode.view.ProgressDialogFragment
import libraries.retrofit.RestError
import libraries.retrofit.enums.RestErrorType


@Suppress("DEPRECATION")
class DestinationFragment : DialogFragment(), DestinationFragmentContract.View{

    private lateinit var destinationCategoryAdapter: DestinationCategoryAdapter
    private lateinit var homeActivity: HomeActivity

    private lateinit var binding: FragMowDestinationBinding
    private lateinit var presenter: DestinationFragmentContract.Presenter

    private var progressDialogFragment: ProgressDialogFragment? = null
    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL,
            R.style.IncidentCommentDialogStyle)

        homeActivity = activity as HomeActivity
        presenter = DestinationFragmentPresenter(this, Injection.provideDataRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.frag_mow_destination, container, false)

        return binding.root

        //return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        destinationCategoryAdapter = DestinationCategoryAdapter(Injection.provideDataRepository().locationId)
        binding.recycleViewItems.adapter = destinationCategoryAdapter

        destinationCategoryAdapter.actionCallback = {
            presenter.setDestination(it)
            dismiss()
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
            //homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.imageViewMenu.setOnClickListener {
            dismiss()

            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
        }

        presenter.getDestinationList()
    }

    override fun showDestinationList(locationList: List<Location>) {

        val groupByDestinationList = locationList.groupBy { it.locationCategoryName }
        destinationCategoryAdapter.setLocationList(groupByDestinationList)
        destinationCategoryAdapter.submitList(groupByDestinationList.keys.toList())
    }

    override fun getDestinationListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun setProgressBar(show: Boolean) {
        if (progressDialogFragment != null) {
            progressDialogFragment!!.dismissAllowingStateLoss()
            progressDialogFragment = null
        }

        if (show) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.add(progressDialogFragment!!, "ProgressDialogFragment")
            transaction.commitAllowingStateLoss()
        }
    }

    override fun processErrorWithToast(restError: RestError?) {

        restError?.restErrorType?.let {

            when (it) {

                RestErrorType.TOKEN_EXPIRED -> {
                    val dataRepository = Injection.provideDataRepository()
                    dataRepository.expiredToken()

                    activity?.finish()
                    LoginActivity.start(requireContext())
                }
                RestErrorType.INTERNET_NOT_AVAILABLE -> {
                    showToastMessage(context?.getString(R.string.check_your_connection_and_try_again))
                }
                RestErrorType.NETWORK_NOT_AVAILABLE -> {
                    showToastMessage(context?.getString(R.string.check_your_connection_and_try_again))
                }
                RestErrorType.REST_API_ERROR -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }
                RestErrorType.UNKNOWN -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }

                RestErrorType.NONE -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }
            }
        }

    }

    private fun showToastMessage(message: String?) {

        if(isAdded) {
            snackbar?.let {
                if(it.isShown)
                    return
            }

            message?.let {
                snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar?.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorRed))
                snackbar?.show()
            }
        }

    }
    companion object {
        fun newInstance() = DestinationFragment()
    }


}