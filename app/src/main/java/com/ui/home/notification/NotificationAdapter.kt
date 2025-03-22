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
package com.ui.home.notification

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.notification.Notification
import com.util.bindWith
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemNotificationBinding
import iCode.utility.DateFormatHelper

class NotificationAdapter : ListAdapter<Notification, NotificationAdapter.Holder>(DIFF_CALLBACK) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_notification))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewTitle.text = item.title
        holder.binding.textViewMessage.text = item.message

        if(item.createdDate.isNotEmpty()) {
            holder.binding.textViewDateAndTime.text =
                DateFormatHelper.getFormattedDate(item.createdDate,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_A)
        }
    }

    class Holder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notification>() {

            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.title == newItem.title &&
                        oldItem.message == newItem.message &&
                        oldItem.createdDate == newItem.createdDate &&
                        oldItem.isRead == newItem.isRead
            }
        }
    }
}