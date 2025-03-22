package com.ui.main.chats

import android.os.Bundle
import android.view.View
import com.mow.cash.android.R
import com.mow.cash.android.databinding.*
import com.ui.home.deviceList.DeviceAdapter

import iCode.android.BaseFragment

class HelpFragment : BaseFragment(), View.OnClickListener {

    private val binding: FragMowHelpBinding by bindingLazy()
    private val scheduledAppointmentAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    override val layoutResId = R.layout.frag_mow_help

    //private lateinit var homeActivity: HomeActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // homeActivity = activity as HomeActivity


        binding.textViewBack.setOnClickListener(this)

       // scheduledAppointmentAdapter.submitList(createDummyData())
        scheduledAppointmentAdapter.actionCallback = {

           // productDetailsActivity.showProductDetailsActivity(requireContext())
            /*var fragment: Fragment = ChatDetailsFragment.newInstance()

            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack(fragment.javaClass.name)
                .replace(R.id.fragment_content, fragment)
                .commit()*/

        }
    }

    companion object {
        fun newInstance() = HelpFragment()
    }


    override fun onClick(v: View?) {

         if(v!!.equals(binding.textViewBack)){
//            fragmentManager!!.popBackStack()
            parentFragmentManager.popBackStack()
        }

      /*  if (v!!.equals(binding.textViewCancel)) {

            getActivity()?.supportFragmentManager?.popBackStack()
                activity?.finish()

        }*/
    }
}