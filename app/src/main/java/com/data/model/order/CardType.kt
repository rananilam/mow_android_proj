package com.data.model.order

import com.mow.cash.android.R


enum class CardType(val cardName: String, val resId: Int) {

    VISA("Visa", R.drawable.ic_card_type_visa),
    AMERICANEXPRESS("American Express", R.drawable.ic_card_type_american_express),
    MASTERCARD("MasterCard", R.drawable.ic_card_type_master_card),
    DISCOVER("Discover", R.drawable.ic_card_type_discover),
    NONE("None", 0);

    companion object {
        fun findByName(cardName: String): CardType {
            for (v in values()) {
                if (v.cardName.equals(cardName)) {
                    return v
                }
            }
            return NONE
        }
    }

}