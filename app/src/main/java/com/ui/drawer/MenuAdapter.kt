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
package com.ui.drawer

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemLeftMenuBinding


import com.util.bindWith

class MenuAdapter : ListAdapter<Menu, MenuAdapter.Holder>(
    DIFF_CALLBACK
) {

    interface IOption {
        fun onMenuClicked(menu: Menu)
        fun onSubMenuClicked(subMenu: SubMenu)
    }
    private var iOption: IOption? = null

    fun setIOption(iOption: IOption) {
        this.iOption = iOption
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            parent.bindWith(R.layout.item_left_menu)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewMenu.text = "${position+1}. ${item.name}"
        if(item.subMenu.isNotEmpty()) {
            val subMenuAdapter = SubMenuAdapter()
            holder.binding.recycleViewSubMenuOptions.adapter = subMenuAdapter
            subMenuAdapter.submitList(item.subMenu)

            subMenuAdapter.actionCallback = {
                iOption?.onSubMenuClicked(it)
            }
        }

    }

    class Holder(val binding: ItemLeftMenuBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Menu>() {

            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.resId == newItem.resId
            }
        }
    }
}