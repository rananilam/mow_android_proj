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
package com.ui.home.activeOrder

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.BaseApplication
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowActiveOrderBinding
import com.data.model.order.OrderH
import com.data.remote.PestService
import com.squareup.picasso.Picasso
import com.util.Utility
import com.util.bindWith
import com.util.showHtmlText
import iCode.utility.DateFormatHelper
import iCode.utility.ScreenUtility
//import org.jetbrains.anko.textColorResource
import java.util.*

class ActiveOrderAdapter : ListAdapter<OrderH, ActiveOrderAdapter.Holder>(DIFF_CALLBACK) {

    private var orderListener: OrderListener? = null

    interface OrderListener {
        fun onReturnClick(order: OrderH)
        fun onExtendClick(order: OrderH)
        fun onEquipmentClick(orderId: Int)
    }

    fun setOrderListener(orderListener: OrderListener?) {
        this.orderListener = orderListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_active_order))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return


        if (item.deviceImagePath.isNotEmpty()) {
            Picasso.get().load(PestService.getBaseURLForImage() + item.deviceImagePath)
                .resize(
                    ScreenUtility.dpToPx(56, BaseApplication.appContext),
                    ScreenUtility.dpToPx(56, BaseApplication.appContext)
                )
                .centerInside()
                .into(holder.binding.imageViewProduct)
        }

        else {
            Picasso.get().load(R.drawable.bg_mow_rounded_corner_rectangle_white)
                .into(holder.binding.imageViewProduct)
        }

        if(item.arrivalDate.isNotEmpty() && item.departureDate.isNotEmpty()) {

            val currentDateCal = Calendar.getInstance()
            val arrivalDateCal = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A_, item.arrivalDate)
            val departureDateCal = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A_, item.departureDate)

            if(arrivalDateCal.timeInMillis > currentDateCal.timeInMillis) {

                holder.binding.textViewTimeRemaining.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.colorBlack));
//                holder.binding.textViewTimeRemaining.textColorResource = R.color.colorBlack
                holder.binding.textViewReturnMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_gray)
                holder.binding.textView12.text = "Future Order Starting in:"
                holder.binding.textViewReturnMyRental.setOnClickListener {
                }
            }
            else if(departureDateCal.timeInMillis > currentDateCal.timeInMillis) {

                holder.binding.textViewTimeRemaining.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.colorBlack));
//                holder.binding.textViewTimeRemaining.textColorResource = R.color.colorBlack
                holder.binding.textViewReturnMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_blue)
                holder.binding.textView12.text = "Return Order in:"
                holder.binding.textViewReturnMyRental.setOnClickListener {
                    orderListener?.onReturnClick(item)
                }
            } else if(currentDateCal.timeInMillis > departureDateCal.timeInMillis){

                holder.binding.textViewTimeRemaining.setTextColor(ContextCompat.getColor(holder.binding.root.context,R.color.colorRed));
//                holder.binding.textViewTimeRemaining.textColorResource = R.color.red
                holder.binding.textViewReturnMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_blue)
                holder.binding.textView12.text = "Order past due:"
                holder.binding.textViewReturnMyRental.setOnClickListener {
                    orderListener?.onReturnClick(item)
                }
            }


            holder.binding.textViewExtendMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_green)
            holder.binding.textViewExtendMyRental.setOnClickListener {
                orderListener?.onExtendClick(item)
            }


            /*if(departureDateCal.timeInMillis > currentDateCal.timeInMillis) {
                holder.binding.textViewExtendMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_green)
                holder.binding.textViewExtendMyRental.setOnClickListener {
                    orderListener?.onExtendClick(item)
                }
            } else {
                holder.binding.textViewExtendMyRental.setBackgroundResource(R.drawable.bg_mow_one_side_rounded_gray_)
                holder.binding.textViewExtendMyRental.setOnClickListener {

                }
            }*/

        }
        holder.binding.textViewTimeRemaining.text = item.remainingTime
        holder.binding.textViewName.text = item.deviceTypeName

        if (item.arrivalDate.isNotEmpty() && item.departureDate.isNotEmpty()) {

            holder.binding.textViewDate.text =
            DateFormatHelper.getFormattedDate(item.arrivalDate,DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,DateFormatHelper.FORMAT_MM_DD_YYYY_S) + " to " +
                    DateFormatHelper.getFormattedDate(item.departureDate,DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,DateFormatHelper.FORMAT_MM_DD_YYYY_S)

        }


        holder.binding.textViewOrderNo.text = String.format(holder.binding.textViewOrderNo.context.getString(R.string.order_number), item.formatedOrderID)
        holder.binding.textViewLocation.text = item.locationName
        holder.binding.textViewPrice.text = String.format(holder.binding.textViewPrice.context.getString(R.string.price), Utility.convertUpTo2Decimal(item.profilePrice))
        holder.binding.textViewDays.text = item.rateSelected


        if(item.isGPSEnabled) {

            holder.binding.textView14.visibility = View.VISIBLE
            holder.binding.imageView.visibility = View.VISIBLE
            holder.binding.textView13.visibility = View.VISIBLE

            holder.binding.textView14.text = "0%"
            holder.binding.imageView.setImageResource(R.drawable.ic_battery_empty)
            if(item.batteryLevel.isNotEmpty()) {

                holder.binding.textView14.text = item.batteryLevel
                val batteryNumberOnly = item.batteryLevel.filter { it.isDigit() }


                if(batteryNumberOnly.isNotEmpty()) {

                    val batteryResId = when(batteryNumberOnly.toInt()){
                        0 -> R.drawable.ic_battery_empty
                        in 1..25 -> R.drawable.ic_battery_low
                        in 25..50 ->R.drawable.ic_battery_medium
                        in 50..75 -> R.drawable.ic_battery_half
                        in 75..100 -> R.drawable.ic_battery_full
                        else -> R.drawable.ic_battery_empty
                    }

                    holder.binding.imageView.setImageResource(batteryResId)

                }

            }

        } else {

            holder.binding.textView13.visibility = View.GONE
            holder.binding.textView14.visibility = View.GONE
            holder.binding.imageView.visibility = View.GONE
        }

        if(item.emvid.isNotEmpty()) {
            holder.binding.textView19.text = String.format(holder.binding.textView19.context.getString(R.string.equipment_id), if(item.emvid.isEmpty()) "-" else item.emvid)

            holder.binding.textView19.setTextColor(ContextCompat.getColor(holder.binding.textView19.context,
                R.color.colorBlack))

        } else {
            holder.binding.textView19.showHtmlText(holder.binding.textView19.context.getString(R.string.enter_your_equipment_id))
            holder.binding.textView19.setTextColor(ContextCompat.getColor(holder.binding.textView19.context,
                R.color.dark_blue))
        }


        holder.binding.textView19.setOnClickListener {
            orderListener?.onEquipmentClick(item.orderID)
        }

    }

    class Holder(val binding: ItemMowActiveOrderBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderH>() {

            override fun areItemsTheSame(oldItem: OrderH, newItem: OrderH): Boolean {
                return oldItem.formatedOrderID == newItem.formatedOrderID && oldItem.remainingTime == newItem.remainingTime
            }

            override fun areContentsTheSame(oldItem: OrderH, newItem: OrderH): Boolean {
                return oldItem.formatedOrderID == newItem.formatedOrderID &&
                        oldItem.arrivalDate == newItem.arrivalDate &&
                        oldItem.departureDate == newItem.departureDate &&
                        oldItem.deviceTypeName == newItem.deviceTypeName &&
                        oldItem.remainingTime == newItem.remainingTime
            }
        }
    }
}