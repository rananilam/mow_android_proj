package com.ui.home.help

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragmentMowWebBinding
import iCode.android.BaseFragment
import im.delight.android.webview.AdvancedWebView


class WebFragment : BaseFragment(), AdvancedWebView.Listener{

    private val binding: FragmentMowWebBinding by bindingLazy()
    override val layoutResId = R.layout.fragment_mow_web

    private lateinit var appCompatActivity: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appCompatActivity = activity as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.textViewBack.setOnClickListener {
            appCompatActivity.onBackPressed()
        }



        val url = arguments?.getString("url","")
        binding.webViewContent.setListener(appCompatActivity, this)

        if(!url.isNullOrEmpty())
            binding.webViewContent.loadUrl(url)
    }


    override fun onResume() {
        super.onResume()
        binding.webViewContent.onResume()
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

        fun newInstance(url: String): WebFragment {

            val bundle = Bundle()
            bundle.putString("url", url)

            val webFragment = WebFragment()
            webFragment.arguments = bundle
            return webFragment
        }
    }
}