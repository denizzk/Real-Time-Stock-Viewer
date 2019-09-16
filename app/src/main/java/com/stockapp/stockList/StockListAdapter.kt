package com.stockapp.stockList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stockapp.databinding.StockItemLayoutBinding

class StockListAdapter : ListAdapter<Pair<String, Triple<String, Double, String>>, StockListAdapter.StockItemViewHolder>(
    ListStockDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockItemViewHolder {
        return StockItemViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: StockItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.first, item.second.first, item.second.second, item.second.third)
    }

    class StockItemViewHolder private constructor(val binding: StockItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stockName: String, stockTrend: String, stockPrice: Double, stockOrigin: String) {
            binding.stockName = stockName
            binding.stockTrend = stockTrend
            binding.stockPrice = stockPrice
            binding.stockOrigin = stockOrigin
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

class ListStockDiffCallBack : DiffUtil.ItemCallback<Pair<String, Triple<String, Double, String>>>() {
    override fun areItemsTheSame(oldItem: Pair<String, Triple<String, Double, String>>, newItem: Pair<String, Triple<String, Double, String>>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pair<String, Triple<String, Double, String>>, newItem: Pair<String, Triple<String, Double, String>>): Boolean {
        return oldItem == newItem
    }
}