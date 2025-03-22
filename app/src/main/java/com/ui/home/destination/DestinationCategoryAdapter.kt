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
package com.ui.home.destination

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowDestinationCategoryBinding
import com.data.model.destination.Location
import com.util.bindWith


class DestinationCategoryAdapter(private val selectedLocationId: Int) : ListAdapter<String, DestinationCategoryAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((Location) -> Unit)? = null
    var locationList: Map<String, List<Location>> = mutableMapOf()

    @JvmName("setLocationList1")
    fun setLocationList(locationList: Map<String, List<Location>>) {
        this.locationList = locationList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_destination_category))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewCateogryName.text = item
        val destinationAdapter = DestinationAdapter(selectedLocationId)
        holder.binding.recycleViewDestinations.adapter = destinationAdapter
        destinationAdapter.submitList(locationList.get(item))
        destinationAdapter.actionCallback = {
            actionCallback?.invoke(it)
        }
    }

    class Holder(val binding: ItemMowDestinationCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem

            }
        }
    }
}