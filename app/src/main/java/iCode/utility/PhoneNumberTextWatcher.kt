package iCode.utility

import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import java.util.Locale

class PhoneNumberTextWatcher(val editText: AppCompatEditText): TextWatcher {

    private var oldValue = ""

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {


        if(p0.toString().isNotEmpty()) {

            if(p0.toString().length > 2) {
                val value = PhoneNumberUtils.formatNumber(p0.toString(),
                    Locale.US.country).replace(" ","")

                if(!oldValue.equals(value)) {
                    oldValue = value
                    editText.setText(value)
                    editText.setSelection(editText.text.toString().length)
                }
            }
        }
    }
}