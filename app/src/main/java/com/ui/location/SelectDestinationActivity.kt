package com.ui.location

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowDestinationBinding
import com.ui.home.destination.DestinationAdapter
import iCode.android.BaseActivity

class SelectDestinationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: FragMowDestinationBinding
    private val destinationAdapter: DestinationAdapter by lazy { DestinationAdapter(1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindContentView(R.layout.frag_mow_destination)

        binding.recycleViewItems.adapter = destinationAdapter
        //  destinationAdapter.submitList(createDummyLocationData())

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        binding.imageViewMenu.setOnClickListener(this)


    }



    companion object {

        fun start(context: Context) =
            context.startActivity(Intent(context, SelectDestinationActivity::class.java))
    }

    override fun onClick(v: View?) {
        if (v!!.equals(binding.imageViewMenu)){
            finish()
        }
    }


}