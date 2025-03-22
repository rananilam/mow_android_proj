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
package com.ui.home.checkOut

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.order.ECheckCard
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowCreditDebitCardBinding
import com.util.bindWith


class ECheckCardListAdapter : ListAdapter<ECheckCard, ECheckCardListAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((ECheckCard) -> Unit)? = null
    var actionDeleteCallback: ((ECheckCard) -> Unit)? = null
    var selectedCard: ECheckCard? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_credit_debit_card))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        if (selectedCard != null && selectedCard?.id == item.id) {

            holder.binding.radioButtonIsSelected.isChecked = true
            holder.binding.viewBorder.visibility = View.VISIBLE
            holder.binding.imageViewDelete.visibility = View.VISIBLE

        } else {
            holder.binding.radioButtonIsSelected.isChecked = false
            holder.binding.viewBorder.visibility = View.INVISIBLE
            holder.binding.imageViewDelete.visibility = View.VISIBLE
        }

        // holder.binding.imageViewCardType.setBackgroundResource(R.drawable.ic_bank_icon)
        holder.binding.imageViewCardType.setImageDrawable(
            ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.ic_echeck_icon
            )
        );

        // hide below line for removing bank name
        // holder.binding.textViewCardName.text =item.bankName/*.cardName +" - "+item.extDate*/
        holder.binding.textViewCardName.visibility = View.GONE
        holder.binding.textViewCardNumber.text = item.ddlItem

        holder.binding.root.setOnClickListener {

            selectedCard = item
            actionCallback?.invoke(item)

            notifyDataSetChanged()
        }

        holder.binding.radioButtonIsSelected.setOnClickListener {

            selectedCard = item
            actionCallback?.invoke(item)

            notifyDataSetChanged()
        }

        holder.binding.imageViewDelete.setOnClickListener {
            actionDeleteCallback?.invoke(item)
            notifyDataSetChanged()

        }
    }

    class Holder(val binding: ItemMowCreditDebitCardBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ECheckCard>() {

            override fun areItemsTheSame(oldItem: ECheckCard, newItem: ECheckCard): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ECheckCard, newItem: ECheckCard): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}