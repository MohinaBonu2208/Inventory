package com.bignerdranch.android.inventory


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.inventory.data.Item
import com.bignerdranch.android.inventory.data.getFormattedPrice
import com.bignerdranch.android.inventory.databinding.ItemViewBinding

class ItemListAdapter(val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemListViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val view = ItemListViewHolder(
            ItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return view
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(current) }
        holder.bind(current)
    }


    class ItemListViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                name.text = item.itemName
                price.text = item.getFormattedPrice()
                quantity.text = item.quantity.toString()
            }

        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }


        }
    }
}

