package com.ui.login.verification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.i("SMSTEST", "onReceive")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status: Status = extras!![SmsRetriever.EXTRA_STATUS] as Status
            when (status.getStatusCode()) {
                CommonStatusCodes.SUCCESS -> {         // Get SMS message contents
                    var message: String? = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?


                    message?.let {
                        Log.i("SMSTEST", "original message: "+it)
                        val onlyNumbers = it.filter { it.isDigit() }

                        if(onlyNumbers.length >= 4) {
                            val code = onlyNumbers.substring(0,4)
                            Log.i("SMSTEST", "message: "+code)

                            context?.let { context ->
                                val intent = Intent("codeEvent")
                                intent.putExtra("code", code)
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                            }

                        }

                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.i("SMSTEST", "TIMEOUT: ")
                }
            }
        }
    }
}