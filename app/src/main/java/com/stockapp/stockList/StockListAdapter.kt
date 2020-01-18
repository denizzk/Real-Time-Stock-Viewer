package com.stockapp.stockList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stockapp.data.model.TickerResponse
import com.stockapp.databinding.StockItemLayoutBinding

class StockListAdapter : ListAdapter<TickerResponse, StockListAdapter.StockItemViewHolder>(
    ListStockDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockItemViewHolder {
        return StockItemViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: StockItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class StockItemViewHolder private constructor(val binding: StockItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: TickerResponse) {
            binding.stock = stock
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): StockItemViewHolder {
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = StockItemLayoutBinding.inflate(layoutInflate, parent, false)
                return StockItemViewHolder(binding)
            }
        }
    }
}

class ListStockDiffCallBack : DiffUtil.ItemCallback<TickerResponse>() {
    override fun areItemsTheSame(oldItem: TickerResponse, newItem: TickerResponse): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TickerResponse, newItem: TickerResponse): Boolean {
        return oldItem == newItem
    }
}