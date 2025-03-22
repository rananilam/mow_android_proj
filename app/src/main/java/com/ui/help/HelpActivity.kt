package com.ui.help

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowHelpBinding
import iCode.android.BaseActivity
import im.delight.android.webview.AdvancedWebView


class HelpActivity : BaseActivity(), AdvancedWebView.Listener {


    private lateinit var binding: FragMowHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.frag_mow_help)
        binding = bindContentView(R.layout.frag_mow_help)

        binding.textViewBack.setOnClickListener{
            finish()
        }

        binding.webViewContent.setListener(this, this)
        binding.webViewContent.loadUrl("https://www.mobilityonwheels.com/help-logging-into-mymobility-app/")

    }


    override fun onPause() {
        binding.webViewContent.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.webViewContent.onDestroy()
        super.onDestroy()
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

        fun start(context: Context) {
            val intent = Intent(context, HelpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}