/*
 *
 *  Licensed under the Apache License, Version 2.0 (the “License”);
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an “AS IS” BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package com.ui.home.orderHistory

//import org.jetbrains.anko.textColor
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.Injection
import com.data.model.order.OrderH
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowOrderHistoryBinding
import com.util.bindWith
import iCode.utility.DateFormatHelper

class OrderHistoryAdapter(private val context: Context, private val offset: Long) : ListAdapter<OrderH, OrderHistoryAdapter.Holder>(DIFF_CALLBACK) {


    var orderActionInterface: OrderActionInterface? = null

    interface OrderActionInterface {
        fun onExtendClicked(order: OrderH)
        fun onRefundTypeClicked(order: OrderH)
        fun onResendAttestationClicked(order: OrderH)
        fun onCompleteAttestationOneClicked(order: OrderH)
        fun onCompleteAttestationTwoClicked(order: OrderH)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_order_history))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.textViewOrderNumber.text = item.formatedOrderID


        if(item.createdDate.isNotEmpty())
            holder.binding.textViewOrderDate.text = DateFormatHelper.getFormattedDate(item.createdDate, DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, DateFormatHelper.FORMAT_MMM_DD_YYYY)
        else
            holder.binding.textViewOrderDate.text = "-"


        if(item.arrivalDate.isNotEmpty())
            holder.binding.textViewArrival.text = DateFormatHelper.getFormattedDate(item.arrivalDate,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, DateFormatHelper.FORMAT_MMM_DD_YYYY)
        else
            holder.binding.textViewArrival.text = "-"

        if(item.departureDate.isNotEmpty())
            holder.binding.textViewDeparture.text = DateFormatHelper.getFormattedDate(item.departureDate,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, DateFormatHelper.FORMAT_MMM_DD_YYYY)
        else
            holder.binding.textViewDeparture.text = "-"


        holder.binding.textViewEquipmentType.text = item.deviceTypeName
        holder.binding.textViewOperatorName.text = item.operatorName
        holder.binding.textViewDestination.text = item.locationName
        holder.binding.textViewCardNumber.text = item.cardLastFourDigit
        holder.binding.textViewCardType.text = "-"

        if(item.isCheckBilled)
        {
            holder.binding.textViewCardNumber.text = item.accountLastFourDigit
            holder.binding.textViewCardType.text = "eCheck"

            holder.binding.textViewExpDate.visibility = View.GONE
            holder.binding.textViewLblExpDate.visibility = View.GONE
        }
        else{
            holder.binding.textViewCardNumber.text = item.cardLastFourDigit
            holder.binding.textViewLblExpDate.visibility = View.VISIBLE
            holder.binding.textViewExpDate.visibility = View.VISIBLE

        }


        if(item.cardExpDate.isNotEmpty())
            holder.binding.textViewExpDate.text = DateFormatHelper.getFormattedDate(item.cardExpDate,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, DateFormatHelper.FORMAT_MMM_YYYY)
        else
            holder.binding.textViewExpDate.text = "-"


        if(item.cardTypeName.isNotEmpty()) {
            val cardTypeNameSplit = item.cardTypeName.split(".")
            if(cardTypeNameSplit.isNotEmpty())
                holder.binding.textViewCardType.text = cardTypeNameSplit[0]
        }


        holder.binding.textViewResendAttestation.text = "-"
        holder.binding.textViewResendAttestation.isClickable = false
        holder.binding.textViewResendAttestation.isEnabled = false
//        holder.binding.textViewResendAttestation.textColor = ContextCompat.getColor(context, R.color.colorGraytext)
        holder.binding.textViewResendAttestation.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.colorGraytext))




        holder.binding.textViewResendAttestation.text = item.resendAttestationTo

        if(!item.isResendAttestationDisable) {
            holder.binding.textViewResendAttestation.isClickable = true
            holder.binding.textViewResendAttestation.isEnabled = true
//            holder.binding.textViewResendAttestation.textColor = ContextCompat.getColor(context, R.color.dark_blue)
            holder.binding.textViewResendAttestation.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.dark_blue))


        }

        holder.binding.textViewResendAttestation.setOnClickListener {
            orderActionInterface?.onResendAttestationClicked(item)
        }



//        holder.binding.textViewForOrderStatus.textColor = ContextCompat.getColor(context, R.color.colorGraytext)
        holder.binding.textViewForOrderStatus.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.colorGraytext))
        holder.binding.textViewForOrderStatus.isClickable = false
        holder.binding.textViewForOrderStatus.isEnabled = false
        holder.binding.textViewForOrderStatus.text = "-"

        holder.binding.textViewRefund.visibility = View.GONE

        val orderStatusList = item.orderStatus.split("|")
        if(orderStatusList.isNotEmpty()) {
            val orderStatus = orderStatusList[0].trim()

            holder.binding.textViewForOrderStatus.text = orderStatus

            if(orderStatus == "Extend") {
//                holder.binding.textViewForOrderStatus.textColor = ContextCompat.getColor(context, R.color.dark_blue)
                holder.binding.textViewForOrderStatus.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.dark_blue))
                holder.binding.textViewForOrderStatus.isClickable = true
                holder.binding.textViewForOrderStatus.isEnabled = true
            }

            if(orderStatusList.size >= 2) {
                val refundType = orderStatusList[1].trim()
                holder.binding.textViewRefund.visibility = View.VISIBLE
                holder.binding.textViewRefund.text = refundType
            }

        }



        holder.binding.textViewForOrderStatus.setOnClickListener {
            orderActionInterface?.onExtendClicked(item)
        }

        holder.binding.textViewRefund.setOnClickListener {
            orderActionInterface?.onRefundTypeClicked(item)
        }


     //   System.out.println("operatorEleAttestationUrl=>"+item.orderID +item.operatorEleAttestationUrl)
     //   System.out.println("payorEleAttestationUrl=>"+item.orderID +item.payorEleAttestationUrl)

        val dataRepository = Injection.provideDataRepository()
        holder.binding.textViewCompleteAttestationOne.setText(item.operatorName)
        holder.binding.textViewCompleteAttestationTwo.setText(dataRepository.user.firstName + " " + dataRepository.user.lastName)

        if(item.operatorEleAttestationUrl.isNotEmpty()){
            holder.binding.textViewCompleteAttestationOne.visibility = View.VISIBLE
        }else{
            holder.binding.textViewCompleteAttestationOne.visibility = View.GONE
        }


        if(item.payorEleAttestationUrl.isNotEmpty()){
            holder.binding.textViewCompleteAttestationTwo.visibility = View.VISIBLE
        }else{
            holder.binding.textViewCompleteAttestationTwo.visibility = View.GONE
        }



//        holder.binding.textViewCompleteAttestationTwo.setOnClickListener {
//
//        }

        holder.binding.textViewCompleteAttestationOne.setOnClickListener {
            orderActionInterface?.onCompleteAttestationOneClicked(item)
        }

        holder.binding.textViewCompleteAttestationTwo.setOnClickListener {
            orderActionInterface?.onCompleteAttestationTwoClicked(item)
        }


    }

    class Holder(val binding: ItemMowOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderH>() {

            override fun areItemsTheSame(oldItem: OrderH, newItem: OrderH): Boolean {
                return oldItem.eaId == newItem.eaId
            }

            override fun areContentsTheSame(oldItem: OrderH, newItem: OrderH): Boolean {


                return oldItem.eaId == newItem.eaId &&
                        oldItem.payorStatus == newItem.payorStatus &&
                        oldItem.isRefundCredit == newItem.isRefundCredit &&
                        oldItem.isDeclined == newItem.isDeclined &&
                        oldItem.isReturned == newItem.isReturned &&
                        oldItem.isReturnImageUploaded == newItem.isReturnImageUploaded &&
                        oldItem.isPrimaryOrder == newItem.isPrimaryOrder
            }

        }
    }
}