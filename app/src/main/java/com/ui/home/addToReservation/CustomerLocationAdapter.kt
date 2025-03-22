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
package com.ui.home.addToReservation

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowPickUpLocationBinding
import com.util.bindWith


class CustomerLocationAdapter() :
    ListAdapter<com.data.model.destination.Location, CustomerLocationAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((com.data.model.destination.Location) -> Unit)? = null
    var actionLocationInstruction: ((com.data.model.destination.Location) -> Unit)? = null

    private var customerPickupLocationID = 0

    public fun setPickupLocationID(pickupLocationID: Int) {
        this.customerPickupLocationID = pickupLocationID
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_pick_up_location))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return


        holder.binding.checkBoxPickLocation.text = item.customerPickuplocationName
        holder.binding.checkBoxPickLocation.setOnClickListener {
            actionCallback?.invoke(item)
            customerPickupLocationID = item.customerPickupLocationID
        }

        holder.binding.checkBoxPickLocation.isChecked =
            customerPickupLocationID == item.customerPickupLocationID


        holder.binding.imageViewInfo.visibility =
            if (item.isAvailableOrNot) View.VISIBLE else View.GONE

        holder.binding.imageViewInfo.setOnClickListener {
            actionLocationInstruction?.invoke(item)
        }
    }

    class Holder(val binding: ItemMowPickUpLocationBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<com.data.model.destination.Location>() {

                override fun areItemsTheSame(
                    oldItem: com.data.model.destination.Location,
                    newItem: com.data.model.destination.Location
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: com.data.model.destination.Location,
                    newItem: com.data.model.destination.Location
                ): Boolean {
                    return oldItem.id == newItem.id &&
                            oldItem.locationName == newItem.locationName

                }
            }
    }
}