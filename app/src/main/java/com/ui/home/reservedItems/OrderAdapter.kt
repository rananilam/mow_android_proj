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
package com.ui.home.reservedItems

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.BaseApplication
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowReservedItemBinding
import com.data.remote.PestService
import com.data.remote.request.order.SaveOrderRq
import com.squareup.picasso.Picasso
import com.util.Utility
import com.util.bindWith
import iCode.utility.DateFormatHelper
import iCode.utility.ScreenUtility
import libraries.image.loader.trasformations.RoundedCornersTransformation
import java.util.*

class OrderAdapter(private val isViewOrder: Boolean) : ListAdapter<SaveOrderRq, OrderAdapter.Holder>(
    DIFF_CALLBACK
) {

    var actionDeleteCallback: ((SaveOrderRq) -> Unit)? = null
    var actionEditCallback: ((SaveOrderRq) -> Unit)? = null

    var visibleItemId = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            parent.bindWith(
                R.layout.item_mow_reserved_item
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        //holder.binding.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.binding.textViewProductName.text = item.itemName
        holder.binding.textViewPrice.text = String.format(holder.binding.textViewPrice.context.getString(R.string.price_), Utility.convertUpTo2Decimal((item.billingRegularPrice+item.priceAdjustment)))

        if(item.chairpadID != 0) {
            holder.binding.textViewChairPadPrice.visibility = View.VISIBLE
            holder.binding.textViewChairPadPrice.text = String.format(holder.binding.textViewChairPadPrice.context.getString(R.string.chair_pad_requirement_), Utility.convertUpTo2Decimal(item.chairPadPrice))
        } else {
            holder.binding.textViewChairPadPrice.visibility = View.GONE
        }

        holder.binding.textViewTotal.text = String.format(holder.binding.textViewTotal.context.getString(R.string.total__), Utility.convertUpTo2Decimal(item.billingRegularPrice+item.chairPadPrice+item.priceAdjustment))


        if(item.itemImagePath.isNotEmpty()) {
            Picasso.get().load(PestService.getBaseURLForImage()+item.itemImagePath)
                .placeholder(R.drawable.bg_mow_rounded_corner_rectangle_white)
                .resize(ScreenUtility.dpToPx(256, BaseApplication.appContext),
                    ScreenUtility.dpToPx(256, BaseApplication.appContext))
                .centerCrop()
                .transform(RoundedCornersTransformation(ScreenUtility.dpToPx(6, BaseApplication.appContext),0))
                .into(holder.binding.imageViewProduct)
        }
        holder.binding.textViewOperatorName.text = String.format(holder.binding.textViewOperatorName.context.getString(R.string.operator_name), item.operatorName)

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
        //09/16/2021 12:00:00 pm

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
            holder.binding.textViewArrivalDate.text = String.format(holder.binding.textViewArrivalDate.context.getString(R.string.arrival_date,DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S, arrivalDate)))
            holder.binding.textViewArrivalTime.text = String.format(holder.binding.textViewArrivalTime.context.getString(R.string.arrival_time,DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_S_HH_MM_A, arrivalDate).toUpperCase()))
        }


        if(departureDateAndTime != 0L) {


            val departureDate = Calendar.getInstance()
            departureDate.timeInMillis = departureDateAndTime

            holder.binding.textViewDepartureDate.text = String.format(holder.binding.textViewDepartureDate.context.getString(R.string.departure_date,DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S, departureDate)))
            holder.binding.textViewDepartureTime.text = String.format(holder.binding.textViewDepartureTime.context.getString(R.string.departure_time,DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_S_HH_MM_A, departureDate).toUpperCase()))
        }

        //holder.binding.textViewArrivalDate.text = "Arrival Date: "+item.orderBillingProfile.

        if(isViewOrder) {
            holder.binding.imageViewDelete.visibility = View.GONE
            holder.binding.textViewEditReservation.visibility = View.GONE
        } else {
            holder.binding.textViewEditReservation.visibility = View.VISIBLE
        }
        
        holder.binding.imageViewDelete.setOnClickListener {
            actionDeleteCallback?.invoke(item)
        }


        holder.binding.textViewEditReservation.setOnClickListener {
            actionEditCallback?.invoke(item)
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

        holder.binding.expandableLayout.visibility = if(visibleItemId == item.localOrderId) View.VISIBLE else View.GONE

        holder.binding.root.setOnClickListener {

            if(holder.binding.expandableLayout.visibility == View.VISIBLE) {
                visibleItemId = 0
            } else {
                visibleItemId = item.localOrderId
            }

            notifyDataSetChanged()
        }
    }

    class Holder(val binding: ItemMowReservedItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SaveOrderRq>() {

            override fun areItemsTheSame(oldItem: SaveOrderRq, newItem: SaveOrderRq): Boolean {
                return oldItem.billingProfileID == newItem.billingProfileID
            }

            override fun areContentsTheSame(oldItem: SaveOrderRq, newItem: SaveOrderRq): Boolean {
                return oldItem.billingProfileID == newItem.billingProfileID &&
                        oldItem.locationID == newItem.locationID &&
                        oldItem.pickUpLocationID == newItem.pickUpLocationID &&
                        oldItem.accessoryID == newItem.accessoryID &&
                        oldItem.operatorID == newItem.operatorID &&
                        oldItem.occupantID == newItem.occupantID &&
                        oldItem.joystickID == newItem.joystickID &&
                        oldItem.wheelchairSizeID == newItem.wheelchairSizeID &&
                        oldItem.chairpadID == newItem.chairpadID &&
                        oldItem.handControllerID == newItem.handControllerID
            }
        }
    }
}