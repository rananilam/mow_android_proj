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
package com.ui.login.register.operator

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.BaseApplication
import com.data.model.user.User
import com.util.bindWith
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowOperatorBinding

class OperatorAdapter(private val isFromProfileScreen: Boolean) :
    ListAdapter<User, OperatorAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallbackEditUser: ((User) -> Unit)? = null
    var actionCallbackManageOccupant: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_operator))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        if (isFromProfileScreen) {
            holder.binding.imageViewSetting.visibility = View.VISIBLE
        } else {
            holder.binding.imageViewSetting.visibility = View.INVISIBLE
        }

        if(item.email.isNotEmpty())
            holder.binding.textViewEmail.text = item.email
        else
            holder.binding.textViewEmail.text = holder.binding.textViewEmail.context.getString(R.string.email)


        holder.binding.textViewName.text = String.format(holder.binding.textViewName.context.getString(R.string.full_name), item.firstName, item.lastName)



        holder.binding.textViewContactNumber.text =
            String.format(holder.binding.textViewContactNumber.context.getString(R.string.cell_home),
                if(item.cellNumber.isNotEmpty()) item.cellNumber else "-",
                if(item.homeNumber.isNotEmpty()) item.homeNumber else "-")


        holder.binding.imageViewEdit.setOnClickListener {
            actionCallbackEditUser?.invoke(item)
        }

        holder.binding.imageViewSetting.setOnClickListener {
            actionCallbackManageOccupant?.invoke(item)
        }


        if(item.isDefault) {
            holder.binding.relativeLayoutRoot.setBackgroundColor(ContextCompat.getColor(BaseApplication.appContext, R.color.colorGreen10))
        } else {
            holder.binding.relativeLayoutRoot.setBackgroundColor(ContextCompat.getColor(BaseApplication.appContext, R.color.colorWhite))
        }
    }

    class Holder(val binding: ItemMowOperatorBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.operatorID == newItem.operatorID
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.email == newItem.email &&
                        oldItem.operatorID == newItem.operatorID &&
                        oldItem.firstName == newItem.firstName &&
                        oldItem.lastName == newItem.lastName &&
                        oldItem.cellNumber == newItem.cellNumber &&
                        oldItem.homeNumber == newItem.homeNumber &&
                        oldItem.isDefault == newItem.isDefault
            }
        }
    }
}