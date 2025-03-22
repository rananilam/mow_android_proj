package com.ui.home.shopOnline

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.data.model.order.OrderData
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragmentMowShopOnlineBinding
import com.ui.home.HomeActivity
import iCode.android.BaseFragment
import im.delight.android.webview.AdvancedWebView


class ShopOnlineFragment : BaseFragment(), AdvancedWebView.Listener{

    private val binding: FragmentMowShopOnlineBinding by bindingLazy()
    override val layoutResId = R.layout.fragment_mow_shop_online

    private lateinit var homeActivity: HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }

        binding.webViewContent.setListener(homeActivity, this)
        binding.webViewContent.loadUrl("https://www.mobilityonwheels.com/shop/")
    }


    override fun onResume() {
        super.onResume()
        binding.webViewContent.onResume()
        //binding.textViewLocationCategory.text = dataRepository.locationCategory

        if(OrderData.isCartEmpty()) {
            binding.frameLayoutCart.visibility = View.GONE
        } else {
            binding.frameLayoutCart.visibility = View.VISIBLE
            binding.textViewNumberOfItems.text = OrderData.getCartSize().toString()
        }
    }






    override fun onPause() {
        binding.webViewContent.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.webViewContent.onDestroy()
        super.onDestroyView()
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        //setProgressBar(true)
    }

    override fun onPageFinished(url: String?) {
       // setProgressBar(false)
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    companion object {
        fun newInstance() = ShopOnlineFragment()
    }
}