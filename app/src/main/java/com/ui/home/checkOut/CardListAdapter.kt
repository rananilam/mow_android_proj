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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.order.Card
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowCreditDebitCardBinding
import com.util.bindWith

class CardListAdapter : ListAdapter<Card, CardListAdapter.Holder>(DIFF_CALLBACK) {

    var actionCallback: ((Card) -> Unit)? = null
    var actionDeleteCallback: ((Card) -> Unit)? = null
    var selectedCard: Card? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.bindWith(R.layout.item_mow_credit_debit_card))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position) ?: return

        if(selectedCard != null && selectedCard?.id == item.id) {

            holder.binding.radioButtonIsSelected.isChecked = true
            holder.binding.viewBorder.visibility = View.VISIBLE
            holder.binding.imageViewDelete.visibility = View.VISIBLE

        } else {
            holder.binding.radioButtonIsSelected.isChecked = false
            holder.binding.viewBorder.visibility = View.INVISIBLE
            holder.binding.imageViewDelete.visibility = View.VISIBLE
        }

        holder.binding.imageViewCardType.setImageResource(item.cardType.resId)
        holder.binding.textViewCardName.text =item.cardType.cardName +" - "+item.extDate
        holder.binding.textViewCardNumber.text = item.cardNumber

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

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Card>() {

            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}