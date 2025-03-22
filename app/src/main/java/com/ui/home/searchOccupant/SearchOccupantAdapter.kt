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
package com.ui.home.searchOccupant

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.user.User
import com.util.bindWith
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowSearchOccupantBinding

class SearchOccupantAdapter() :
    ListAdapter<User, SearchOccupantAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_search_occupant))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.textViewOccupantName.text = item.occupantName
        holder.binding.textViewOperatorName.text = item.operatorName

        holder.binding.radioButtonSelect.setOnClickListener {
            actionCallback?.invoke(item)
        }
    }

    class Holder(val binding: ItemMowSearchOccupantBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.firstName == newItem.firstName &&
                        oldItem.lastName == newItem.lastName
            }

        }
    }
}