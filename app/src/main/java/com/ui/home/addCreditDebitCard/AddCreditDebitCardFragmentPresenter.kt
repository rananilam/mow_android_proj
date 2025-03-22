package com.ui.home.addCreditDebitCard

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.order.AuthorizePaymentRequestRs
import com.data.remote.response.order.CreditCardRs
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class AddCreditDebitCardFragmentPresenter(
    val addCreditDebitCardView: AddCreditDebitCardFragmentContract.View,
    private val dataRepository: DataRepository,
) : BasePresenter<AddCreditDebitCardFragmentContract.View>(),
    AddCreditDebitCardFragmentContract.Presenter {

    override fun authorizePaymentRequest(
        firstName: String,
        middleName: String,
        lastName: String,
        email: String,
        phone: String,
        city: String,
        zip: String,
        state: String,
        stateID: Int,
        country: String,
        cardTypeID: Int,
        creditCardNumber: String,
        csv: String,
        address1: String,
        address2: String,
        expYear: Int,
        expMonth: Int,
    ) {


        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (address1.trim().isNotEmpty()) {


                    if (city.trim().isNotEmpty()) {


                        if (stateID != 0) {


                            if (zip.trim().isNotEmpty()) {


                                if (phone.trim().isNotEmpty()) {

                                    if (email.trim().isNotEmpty()) {

                                        if (cardTypeID != 0) {

                                            if (creditCardNumber.trim().isNotEmpty()) {

                                                if (expMonth != 0) {

                                                    if (expYear != 0) {

                                                        if (csv.trim().isNotEmpty()) {

                                                            addCreditDebitCardView.setProgressBar(
                                                                true
                                                            )
                                                            dataRepository.authorizePaymentRequest(
                                                                dataRepository.user.id,
                                                                firstName,
                                                                middleName,
                                                                lastName,
                                                                email,
                                                                phone,
                                                                city,
                                                                zip,
                                                                state,
                                                                stateID,
                                                                country,
                                                                cardTypeID,
                                                                creditCardNumber.trim()
                                                                    .replace(" ", ""),
                                                                csv,
                                                                address1,
                                                                address2,
                                                                expYear,
                                                                expMonth,
                                                                object :
                                                                    CallbackSubscriber<AuthorizePaymentRequestRs>() {
                                                                    override fun onSuccess(response: AuthorizePaymentRequestRs?) {

                                                                        addCreditDebitCardView.setProgressBar(
                                                                            false
                                                                        )

                                                                        response?.result?.isStatus?.let { isSuccess ->
                                                                            if (isSuccess) {
                                                                                addCreditDebitCardView.authorizePaymentSuccessfully()

                                                                            } else {
                                                                                addCreditDebitCardView.authorizePaymentFail(
                                                                                    response.result.errorMessage
                                                                                )
                                                                            }
                                                                        }

                                                                    }

                                                                    override fun onFailure(restError: RestError?) {
                                                                        addCreditDebitCardView.setProgressBar(
                                                                            false
                                                                        )
                                                                        addCreditDebitCardView.processErrorWithToast(
                                                                            restError
                                                                        )
                                                                    }
                                                                })

                                                        } else {
                                                            addCreditDebitCardView.showToastMessage(
                                                                addCreditDebitCardView.contextt.getString(
                                                                    R.string.validation_please_enter_cvv
                                                                )
                                                            )
                                                        }

                                                    } else {
                                                        addCreditDebitCardView.showToastMessage(
                                                            addCreditDebitCardView.contextt.getString(
                                                                R.string.validation_please_select_expiry_year
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    addCreditDebitCardView.showToastMessage(
                                                        addCreditDebitCardView.contextt.getString(R.string.validation_please_select_expiry_month)
                                                    )
                                                }

                                            } else {
                                                addCreditDebitCardView.showToastMessage(
                                                    addCreditDebitCardView.contextt.getString(R.string.validation_please_enter_card_number)
                                                )
                                            }

                                        } else {
                                            addCreditDebitCardView.showToastMessage(
                                                addCreditDebitCardView.contextt.getString(R.string.validation_please_select_card_type)
                                            )
                                        }

                                    } else {
                                        addCreditDebitCardView.showToastMessage(
                                            addCreditDebitCardView.contextt.getString(R.string.validation_please_enter_email_address)
                                        )
                                    }

                                } else {
                                    addCreditDebitCardView.showToastMessage(
                                        addCreditDebitCardView.contextt.getString(
                                            R.string.validation_please_enter_mobile_cell_phone
                                        )
                                    )
                                }

                            } else {
                                addCreditDebitCardView.showToastMessage(
                                    addCreditDebitCardView.contextt.getString(
                                        R.string.validation_please_enter_zip_code
                                    )
                                )
                            }

                        } else {
                            addCreditDebitCardView.showToastMessage(
                                addCreditDebitCardView.contextt.getString(
                                    R.string.validation_please_select_state
                                )
                            )
                        }

                    } else {
                        addCreditDebitCardView.showToastMessage(
                            addCreditDebitCardView.contextt.getString(
                                R.string.validation_please_enter_town_city
                            )
                        )
                    }
                } else {
                    addCreditDebitCardView.showToastMessage(
                        addCreditDebitCardView.contextt.getString(
                            R.string.validation_please_enter_street_address
                        )
                    )
                }

            } else {
                addCreditDebitCardView.showToastMessage(addCreditDebitCardView.contextt.getString(R.string.validation_please_enter_last_name))
            }

        } else {
            addCreditDebitCardView.showToastMessage(addCreditDebitCardView.contextt.getString(R.string.validation_please_enter_first_name))
        }

    }

    override fun getAllState() {

        addCreditDebitCardView.setProgressBar(true)
        dataRepository.getAllState(
            object : CallbackSubscriber<GetAllStateRs>() {
                override fun onSuccess(response: GetAllStateRs?) {

                    addCreditDebitCardView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            addCreditDebitCardView.showStateList(response.stateList)

                        } else {
                            addCreditDebitCardView.getStateListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addCreditDebitCardView.setProgressBar(false)
                    addCreditDebitCardView.processErrorWithToast(restError)
                }
            })
    }

    override fun getCustomer() {
        addCreditDebitCardView.setProgressBar(true)
        dataRepository.getCustomerByID(dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    addCreditDebitCardView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.customer?.let {
                                addCreditDebitCardView.showCustomer(it)
                            }

                        } else {
                            addCreditDebitCardView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addCreditDebitCardView.setProgressBar(false)
                    addCreditDebitCardView.processErrorWithToast(restError)
                }
            })
    }

    override fun getCreditCardInfo(request: InputImage) {

        addCreditDebitCardView.setProgressBar(true)
        val response: CreditCardRs
        response = CreditCardRs()
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val expiryDateRegex = Regex("\\b(0[1-9]|1[0-2])[\\s/\\-]?(\\d{2}|\\d{4})\\b")
        val cardNumberRegex =
            Regex("\\b(?:\\d{4} \\d{4} \\d{4} \\d{4}|\\d{4} \\d{6} \\d{4}|\\d{4} \\d{6} \\d{5})\\b")
        val cardReg =
            Regex("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})\$")
        recognizer.process(request)
            .addOnSuccessListener { visionText ->
                val cardNumberBuilder = StringBuilder()
                Log.e("Nilam", visionText.text)
                val words = visionText.text.split("\n")
                for (i in 0..words.size - 4) {
                    val isValidCard = (0 until 4).all { offset ->
                        if (words[i + offset].contains("/")) {
                            false
                        } else {
                            val cleanLine = words[i + offset].replace(Regex("[^0-9]"), "").trim()
                            cleanLine.matches(Regex("\\d{4}")) && cleanLine.length == 4 // Check each of the 4 lines
                        }
                    }

                    if (isValidCard) {
                        // Combine the 4 lines into one card number
                        for (j in 0 until 4) {
                            cardNumberBuilder.append(words[i + j] + " ")
                        }
                    }
                }

                if (cardNumberBuilder.toString().isNotEmpty()) {
                    var carno = cardNumberBuilder.replace(Regex("[^0-9]"), "")
                    Log.e("cardNumberBuilder", "carno----->/////>>>" + carno.toString())
                    if (carno.length == 16) {//4712270018231787
                        // Insert spaces every 4 digits
                        carno = carno.chunked(4).joinToString(" ")
                        val cardNumber = cardNumberRegex.find(carno)?.value
                        if (cardNumber != null) {
                            response.card_number = cardNumber.toString()
                            val sanitizedCardNumber =
                                cardNumber.replace("\\D".toRegex(), "")  // Remove non-digits
                            val cardType = getCardType(sanitizedCardNumber)
                            Log.e(
                                "cardNumberBuilder",
                                "cardNumberBuilder----->>>>" + cardType.toString()
                            )
                            response.card_type = "$cardType"
                        }
                    }

                }


                for (word in words) {
                    // if(response.card_number.isEmpty()) {
                    if (response.card_number.isEmpty() || response.card_number == "null" || response.card_number == null) {
                        if (word.replace(" ", "").matches(cardReg)) {
                            val cardNumber = cardNumberRegex.find(visionText.text)?.value
                            Log.e(
                                "cardNumberBuilder",
                                "cardNumber----->>>>" + cardNumber.toString()
                            )
                            if (cardNumber != null) {
                                response.card_number = cardNumber.toString()
                                val sanitizedCardNumber =
                                    cardNumber.replace("\\D".toRegex(), "")  // Remove non-digits
                                val cardType = getCardType(sanitizedCardNumber)
                                response.card_type = "$cardType"
                            } else {
                                val cardN = word.trim().replace(Regex("\\s+"), "").chunked(4)
                                    .joinToString(" ")
                                Log.e("cardNumberBuilder", "cardN----->>>>" + cardN.toString())
                                response.card_number = cardN
                                val sanitizedCardNumber =
                                    cardN.replace("\\D".toRegex(), "")  // Remove non-digits
                                val cardType = getCardType(sanitizedCardNumber)
                                response.card_type = "$cardType"
                            }
                        }
                    }
                    //REGEX for detecting a credit card
                    if (word.contains("/")) {
                        val expDate = expiryDateRegex.find(word.trim())?.value
                        if (expDate != null) {
                            response.expiry_date = expDate.toString()
                            Log.e("cardNumberBuilder", "word----->" + word.toString())
                            Log.e("cardNumberBuilder", "word----->" + expDate.toString())

                        } else {
                            val regex = Regex("\\d{2}/\\d{2}")
                            val matchResult = (regex.find(word.trim()))?.value // 03/26
                            if (matchResult != null) {
                                response.expiry_date = matchResult.toString()
                                Log.e("cardNumberBuilder", "word----->" + word.toString())
                                Log.e(
                                    "cardNumberBuilder",
                                    "matchResult----->" + matchResult.toString()
                                )
                            } else {
                                val regex = Regex("^OB/(\\d+)$")
                                val conv_val =
                                    regex.replace(word.trim().toString()) { matchResult ->
                                        "08/${matchResult.groupValues[1]}" // Replace "OB/" with "08/" and keep the digits
                                    }
                                response.expiry_date = conv_val.toString()
                                Log.e("cardNumberBuilder", "conv_val----->" + conv_val.toString())
                            }
                        }
                    }
                }


                if (response.card_number.isEmpty() || response.card_number == "null" || response.card_number == null) {
                    val cardNumber = cardNumberRegex.find(visionText.text)?.value
                    if (cardNumber != null) {
                        val sanitizedCardNumber =
                            cardNumber.replace("\\D".toRegex(), "")  // Remove non-digits
                        val cardType = (sanitizedCardNumber)
                        if (response.card_type.isEmpty()) {
                            response.card_type = cardType
                        }
                        if (response.card_number.isEmpty()) {
                            response.card_number = "$cardNumber"
                        }
                    }
                }

                if (response.expiry_date.isEmpty() || response.expiry_date == "null" || response.expiry_date == null) {
                    val expiryDate = expiryDateRegex.find(visionText.text)?.value
                    if (response.expiry_date.isEmpty() && expiryDate.toString().contains("/")) {
                        response.expiry_date = "$expiryDate"
                    }
                }

                response.let {
                    addCreditDebitCardView.setProgressBar(false)
                    if (it.card_number.isEmpty() || it.card_number == "null" || it.card_number == null) {
                        val welcomeMessage =
                            addCreditDebitCardView.contextt.getString(R.string.credit_card_number_not_found)
                        addCreditDebitCardView.getDocumentFail(welcomeMessage)
                    } else {
                        if (it.expiry_date.isEmpty() || it.expiry_date == "null" || it.expiry_date == null) {
                            val welcomeMessage =
                                addCreditDebitCardView.contextt.getString(R.string.exp_date_not_found)
                            addCreditDebitCardView.getDocumentFail(welcomeMessage)
                        } else {
                            addCreditDebitCardView.showDocument(it)
                        }
                    }


                }

            }
            .addOnFailureListener { e ->
                addCreditDebitCardView.setProgressBar(false)
                addCreditDebitCardView.getDocumentFail(e.toString())
                e.printStackTrace() // Handle the error
            }
    }


    override fun getCardType(cardNumber: String): String {

            return when {
                // Visa
                cardNumber.startsWith("4") -> "Visa"

                // MasterCard
                cardNumber.startsWith("5") -> "MasterCard"

                // American Express
                cardNumber.startsWith("3") || (cardNumber.startsWith("34") || cardNumber.startsWith(
                    "37"
                )) -> "American Express"

                // discover
                cardNumber.startsWith("6") -> "Discover"
                else -> "Unknown Card Type"
            }


    }


}


