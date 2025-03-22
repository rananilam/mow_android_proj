package com.ui.drawer


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.util.bindWith
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemLeftSubMenuBinding

class SubMenuAdapter : ListAdapter<SubMenu, SubMenuAdapter.Holder>(
    DIFF_CALLBACK
) {

    var actionCallback: ((SubMenu) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            parent.bindWith(R.layout.item_left_sub_menu)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewSubMenu.text = item.name
        holder.binding.textViewSubMenu.setOnClickListener{
            actionCallback?.invoke(item)
        }
    }

    class Holder(val binding: ItemLeftSubMenuBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SubMenu>() {

            override fun areItemsTheSame(oldItem: SubMenu, newItem: SubMenu): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: SubMenu, newItem: SubMenu): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}