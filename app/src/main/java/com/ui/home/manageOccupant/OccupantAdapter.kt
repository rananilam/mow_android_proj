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
package com.ui.home.manageOccupant

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.BaseApplication
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowOccupantBinding
import com.data.model.user.User
import com.util.bindWith

class OccupantAdapter() : ListAdapter<User, OccupantAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallbackEditOccupant: ((User) -> Unit)? = null
    var actionCallbackDeleteOccupant: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_occupant))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewOperatorName.text = String.format(holder.binding.textViewOperatorName.context.getString(R.string.operator_name), item.operatorName)
        holder.binding.textViewName.text =  String.format(holder.binding.textViewName.context.getString(R.string.full_name), item.firstName, item.lastName)


        holder.binding.imageViewEdit.setOnClickListener {
            actionCallbackEditOccupant?.invoke(item)
        }

        holder.binding.imageViewDelete.setOnClickListener {
            actionCallbackDeleteOccupant?.invoke(item)
        }

        if(item.isDefault) {
            holder.binding.constraintLayoutRoot.setBackgroundColor(ContextCompat.getColor(
                BaseApplication.appContext, R.color.colorGreen10))

            holder.binding.imageViewEdit.visibility = View.GONE
            holder.binding.imageViewDelete.visibility = View.GONE
        } else {
            holder.binding.constraintLayoutRoot.setBackgroundColor(ContextCompat.getColor(BaseApplication.appContext, R.color.colorWhite))
            holder.binding.imageViewEdit.visibility = View.VISIBLE
            holder.binding.imageViewDelete.visibility = View.VISIBLE
        }
    }

    class Holder(val binding: ItemMowOccupantBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.firstName == newItem.firstName &&
                        oldItem.middleName == newItem.middleName &&
                        oldItem.lastName == newItem.lastName &&
                        oldItem.weight == newItem.weight &&
                        oldItem.heightFeet == newItem.heightFeet &&
                        oldItem.heightInch == newItem.heightInch &&
                        oldItem.isDefault == newItem.isDefault &&
                        oldItem.isDefaultOccupant == newItem.isDefaultOccupant
            }
        }
    }
}