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

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemDevicePropertyBinding
import com.data.model.device.DeviceOptionID
import com.data.model.device.DeviceProperty
import com.util.Utility
import com.util.bindWith


class DevicePropertyAdapter(private var devicePropertyOptionID: Int,
                            private val optionId: DeviceOptionID, private val chairPadPrice: Float) :
    ListAdapter<DeviceProperty, DevicePropertyAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((Int,String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_device_property))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.checkBoxDeviceProperty.isChecked =
            devicePropertyOptionID == item.devicePropertyOptionID

        holder.binding.checkBoxDeviceProperty.text = item.devicePropertyOption

        if(optionId == DeviceOptionID.CHAIR_PAD_REQUIREMENT) {
            holder.binding.checkBoxDeviceProperty.append("      $"+Utility.convertUpTo2Decimal(chairPadPrice))
        }

        holder.binding.checkBoxDeviceProperty.setOnClickListener {
            devicePropertyOptionID = item.devicePropertyOptionID
            actionCallback?.invoke(devicePropertyOptionID,item.devicePropertyOption)
            notifyDataSetChanged()
        }
    }

    class Holder(val binding: ItemDevicePropertyBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<DeviceProperty>() {

                override fun areItemsTheSame(
                    oldItem: DeviceProperty,
                    newItem: DeviceProperty
                ): Boolean {
                    return oldItem.devicePropertyID == newItem.devicePropertyID
                }

                override fun areContentsTheSame(
                    oldItem: DeviceProperty,
                    newItem: DeviceProperty
                ): Boolean {
                    return oldItem.devicePropertyID == newItem.devicePropertyID &&
                            oldItem.devicePropertyOption == newItem.devicePropertyOption &&
                            oldItem.devicePropertyOptionID == newItem.devicePropertyOptionID

                }
            }
    }
}