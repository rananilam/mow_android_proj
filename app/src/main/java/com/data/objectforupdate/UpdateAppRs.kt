package com.data.objectforupdate

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.data.DataRepository
import com.data.Injection
import com.data.remote.response.BaseRs
import com.mow.cash.android.R


class UpdateAppRs : BaseRs() {
    var androidAppVersionMessage = ""
    var androidAppVersionRequired = ""
    var iosAppVersionMessage = ""
    var iosAppVersionRequired = ""
}





object DialogUtils {
  private  lateinit var dataRepository: DataRepository
    fun showAlertDialog(context: Context,activity: Activity ) {


        dataRepository = Injection.provideDataRepository()

        val builder = AlertDialog.Builder(context,R.style.CustomAlertDialog)
            .create()
        val view = activity.layoutInflater.inflate(R.layout.dialog_update_view,null)
        val  button = view.findViewById<TextView>(R.id.btnOk)
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
            var packageNamee = activity.packageName
            Log.e("nilam rana",activity.packageName)
            try {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageNamee")))
                Log.e("nilam rana","market://details?id=$packageNamee")

            } catch (e: ActivityNotFoundException) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageNamee")))
                Log.e("nilam rana ----->","https://play.google.com/store/apps/details?id=$packageNamee")

            }

            dataRepository.expiredToken()
            activity.finish()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.setCancelable(false)
        builder.show()


    }
}

