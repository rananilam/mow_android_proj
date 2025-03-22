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
package com.ui.home.checkOut

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.order.OrderData
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowOrderDetailBinding
import com.data.remote.request.order.SaveOrderRq
import com.util.Utility
import com.util.bindWith
import iCode.utility.DateFormatHelper
import java.util.*

class OrderDetailAdapter : ListAdapter<SaveOrderRq, OrderDetailAdapter.Holder>(DIFF_CALLBACK) {

    var visibleItemId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_order_detail))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        val indexNo = (position + 1)
        holder.binding.textViewName.text = String.format(holder.binding.textViewName.context.getString(R.string.item_name), indexNo, item.itemName)
        holder.binding.textViewTotal.text = String.format(holder.binding.textViewTotal.context.getString(R.string.sub_total), Utility.convertUpTo2Decimal((item.billingRegularPrice+item.priceAdjustment)))
        holder.binding.textViewRentalPeriod1.text = item.billingDescription

        if(item.chairpadID != 0) {
            holder.binding.textViewLblChairPadPrice.visibility = View.VISIBLE
            holder.binding.textViewChairPadPrice.visibility = View.VISIBLE

            holder.binding.textViewChairPadPrice.text = String.format(holder.binding.textViewChairPadPrice.context.getString(R.string.sub_total), Utility.convertUpTo2Decimal(item.chairPadPrice))
        } else {
            holder.binding.textViewLblChairPadPrice.visibility = View.GONE
            holder.binding.textViewChairPadPrice.visibility = View.GONE
        }


        if(item.operatorID != 0) {
            holder.binding.textViewOperatorName.visibility = View.VISIBLE
            holder.binding.textViewOperatorName.text = String.format(holder.binding.textViewOperatorName.context.getString(R.string.operator_name), item.operatorName)
        } else {
            holder.binding.textViewOperatorName.visibility = View.GONE
            holder.binding.textViewOperatorName.text = ""
        }

        if(item.occupantID != 0) {
            holder.binding.textViewOccupantName.visibility = View.VISIBLE
            holder.binding.textViewOccupantName.text = String.format(holder.binding.textViewOccupantName.context.getString(R.string.occupant_name), item.occupantName)
        } else {
            holder.binding.textViewOccupantName.visibility = View.GONE
            holder.binding.textViewOccupantName.text = ""
        }

        holder.binding.textViewComplimentary.text =
            String.format(holder.binding.textViewComplimentary.context.getString(R.string.complimentary_accessory),
                item.accessoryTypeName)

        holder.binding.textViewDestination.text = String.format(holder.binding.textViewDestination.context.getString(R.string.destination),
            item.locationName)


        holder.binding.textViewPickupLocation.text = String.format(holder.binding.textViewPickupLocation.context.getString(R.string.pickup_location),
            item.pickUpLocationName)

        if(item.isShippingAddress) {
            holder.binding.textViewOtherPickupLocation.visibility = View.VISIBLE
            holder.binding.textViewOtherPickupLocation.text = item.shippingAddressLine1+"\n"+item.shippingAddressLine2+", "+item.shippingCity+"\n"+item.shippingStateName+","+item.shippingZipcode+"\nNote: "+item.shippingDeliveryNote
        } else {
            holder.binding.textViewOtherPickupLocation.visibility = View.GONE
        }

        holder.binding.textViewRentalPeriod.text = String.format(holder.binding.textViewRentalPeriod.context.getString(R.string.rental_period),
            item.billingDescription)


        var arrivalDateAndTime = 0L
        var departureDateAndTime = 0L

        if(item.newDepartureDateAndTime != 0L) {
            arrivalDateAndTime = item.departureDateAndTime
            departureDateAndTime = item.newDepartureDateAndTime
        } else {
            arrivalDateAndTime = item.arrivalDateAndTime
            departureDateAndTime = item.departureDateAndTime
        }


        if(arrivalDateAndTime != 0L) {

            val arrivalDate = Calendar.getInstance()
            arrivalDate.timeInMillis = arrivalDateAndTime
            //val arrivalDate = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, it.arrivalDate)
            holder.binding.textViewArrivalDate.text = String.format(holder.binding.textViewArrivalDate.context.getString(R.string.arrival_date,
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S, arrivalDate)))
            holder.binding.textViewArrivalTime.text = String.format(holder.binding.textViewArrivalTime.context.getString(R.string.arrival_time,
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_S_HH_MM_A, arrivalDate).toUpperCase()))
        }


        if(departureDateAndTime != 0L) {


            val departureDate = Calendar.getInstance()
            departureDate.timeInMillis = departureDateAndTime

            holder.binding.textViewDepartureDate.text = String.format(holder.binding.textViewDepartureDate.context.getString(R.string.departure_date,
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S, departureDate)))
            holder.binding.textViewDepartureTime.text = String.format(holder.binding.textViewDepartureTime.context.getString(R.string.departure_time,
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_S_HH_MM_A, departureDate).toUpperCase()))
        }



        if(item.joystickID != 0) {
            holder.binding.textViewJoystickPosition.visibility = View.VISIBLE
            holder.binding.textViewJoystickPosition.text = String.format(holder.binding.textViewJoystickPosition.context.getString(R.string.joystick_position,item.joystickName))
        } else {
            holder.binding.textViewJoystickPosition.visibility = View.GONE
        }

        if(item.handControllerID != 0) {
            holder.binding.textViewHandController.visibility = View.VISIBLE
            holder.binding.textViewHandController.text = String.format(holder.binding.textViewHandController.context.getString(R.string.hand_controller,item.handControllerName))
        } else {
            holder.binding.textViewHandController.visibility = View.GONE
        }

        if(item.wheelchairSizeID != 0) {
            holder.binding.textViewWheelchairSize.visibility = View.VISIBLE
            holder.binding.textViewWheelchairSize.text = String.format(holder.binding.textViewWheelchairSize.context.getString(R.string.wheelchair_size,item.wheelchairSizeName))
        } else {
            holder.binding.textViewWheelchairSize.visibility = View.GONE
        }

        if(visibleItemId == item.localOrderId) {
            holder.binding.expandableLayout.visibility = View.VISIBLE
            holder.binding.imageViewExpandCollapse.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.binding.expandableLayout.visibility = View.GONE
            holder.binding.imageViewExpandCollapse.setImageResource(R.drawable.ic_down_arrow)
        }

        holder.binding.root.setOnClickListener {

            if(holder.binding.expandableLayout.visibility == View.VISIBLE) {
                visibleItemId = 0
            } else {
                visibleItemId = item.localOrderId
            }

            notifyDataSetChanged()
        }
    }

    class Holder(val binding: ItemMowOrderDetailBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SaveOrderRq>() {

            override fun areItemsTheSame(oldItem: SaveOrderRq, newItem: SaveOrderRq): Boolean {
                return oldItem.billingProfileID == newItem.billingProfileID
            }

            override fun areContentsTheSame(oldItem: SaveOrderRq, newItem: SaveOrderRq): Boolean {
                return oldItem.billingProfileID == newItem.billingProfileID
            }
        }
    }
}