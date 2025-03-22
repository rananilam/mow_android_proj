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
package com.ui.home.deviceList

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.BaseApplication
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemDeviceBinding
import com.data.remote.PestService
import com.data.model.device.Device
import com.squareup.picasso.Picasso
import com.util.bindWith
import iCode.utility.ScreenUtility
import libraries.image.loader.trasformations.RoundedCornersTransformation

class DeviceAdapter : ListAdapter<Device, DeviceAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((Device) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_device))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        Log.d("Nilam","adapter printed --- >"+item.toString());


        if(item.itemImagePath.isNotEmpty()) {


            Picasso.get().load(PestService.getBaseURLForImage()+item.itemImagePath)
                    .placeholder(R.drawable.bg_mow_rounded_corner_rectangle_white)
                    .resize(ScreenUtility.dpToPx(256,BaseApplication.appContext),
                            ScreenUtility.dpToPx(256,BaseApplication.appContext))
                    .centerCrop()
                    .transform(RoundedCornersTransformation(ScreenUtility.dpToPx(6,BaseApplication.appContext),0))
                    .into(holder.binding.imageViewProduct)

        } else {
            Picasso.get().load(R.drawable.bg_mow_rounded_corner_rectangle_white)
                    .into(holder.binding.imageViewProduct)
        }

        holder.binding.textViewProductName.text = item.itemName
        holder.binding.textViewItemPriceDescription.text = item.regularPriceDescription
        //holder.binding.textViewProperty.text = item.title
        //holder.binding.textviewPrice.text = item.price
        //holder.binding.textViewDays.text = item.days
        //holder.binding.imageviewProduct.setImageResource(item.image)



        holder.binding.root.setOnClickListener {
            actionCallback?.invoke(item)
        }
    }

    class Holder(val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Device>() {

            override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
                return oldItem.compPriceDescription == newItem.compPriceDescription &&
                        oldItem.deviceTypeId == newItem.deviceTypeId &&
                        oldItem.inventoryId == newItem.inventoryId &&
                        oldItem.itemImagePath == newItem.itemImagePath &&
                        oldItem.itemName == newItem.itemName &&
                        oldItem.regularPriceDescription == newItem.regularPriceDescription
            }


            override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
                return oldItem.compPriceDescription == newItem.compPriceDescription &&
                        oldItem.deviceTypeId == newItem.deviceTypeId &&
                        oldItem.inventoryId == newItem.inventoryId &&
                        oldItem.itemImagePath == newItem.itemImagePath &&
                        oldItem.itemName == newItem.itemName &&
                        oldItem.regularPriceDescription == newItem.regularPriceDescription
            }
        }
    }
}