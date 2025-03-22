package com.util

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputLayout
import com.mow.cash.android.R
import im.delight.android.webview.AdvancedWebView

/**
 * Inflates and creates binding for a new view with [layoutId] view is attached to current [ViewGroup]
 */
fun <T : ViewDataBinding> ViewGroup.bindWith(@LayoutRes layoutId: Int): T {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return DataBindingUtil.inflate(inflater, layoutId, this, false)
}


/**
 * Change statusBarColor according to theme
 */
fun Window.changeStatusBarColor(color: Int, light: Boolean) {

    if(light) {
        statusBarColor = color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        statusBarColor = color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = 0
        }
    }
}

fun AppCompatTextView.showHtmlText(htmlText: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(htmlText)
    }
}

fun AdvancedWebView.showHtmlText(text: String) {

    setBackgroundColor(Color.TRANSPARENT)
    settings.textZoom = 80
    loadData(Base64.encodeToString(text.toByteArray(), Base64.NO_PADDING), "text/html", "base64")
}


fun TextInputLayout.setHintMsg(hintMsg: String) {



    editText?.hint =
        HtmlCompat.fromHtml(
            hintMsg,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


    editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

            if(editText?.text.toString().trim().isNotEmpty()) {

                editText?.hint = ""
                hint =
                    HtmlCompat.fromHtml(
                        hintMsg,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )

            } else {

                hint = ""
                editText?.hint =
                    HtmlCompat.fromHtml(
                        hintMsg,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
            }
        }

    })

}


inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
inline fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
inline fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}