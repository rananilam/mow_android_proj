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
package com.ui.home.reportBug

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowFileNameCardBinding
import com.util.bindWith
import libraries.image.helper.models.MediaResult

class FileNameListAdapter : ListAdapter<MediaResult, FileNameListAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((MediaResult) -> Unit)? = null
    var actionDeleteCallback: ((MediaResult) -> Unit)? = null
    var selectedFile: MediaResult? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_file_name_card))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

     

        holder.binding.imageViewFileName.setImageResource(item.resId)
        holder.binding.textViewFileName.text =item.name.toString()

        holder.binding.root.setOnClickListener {

            selectedFile = item
            actionCallback?.invoke(item)
            notifyDataSetChanged()
        }


        holder.binding.imageViewDelete.setOnClickListener {
            actionDeleteCallback?.invoke(item)
            notifyDataSetChanged()

        }
    }

    class Holder(val binding: ItemMowFileNameCardBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaResult>() {

            override fun areItemsTheSame(oldItem: MediaResult, newItem: MediaResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MediaResult, newItem: MediaResult): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}