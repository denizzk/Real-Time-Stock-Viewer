package com.stockapp.stockList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.stockapp.R
import com.stockapp.databinding.FragmentStockListBinding

class StockListFragment : Fragment() {

    private lateinit var viewModel: StockListViewModel

    private lateinit var binding: FragmentStockListBinding

    private var adapter = StockListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_list, container, false)

        viewModel = ViewModelProviders.of(this).get(StockListViewModel::class.java)

        binding.stockListViewModel = viewModel
        binding.lifecycleOwner = this

        binding.stockRcView.adapter = adapter
        binding.stockRcView.itemAnimator = null

        //Observe stockList if updated submit new list to the adapter, find diff with the old one and update
        viewModel.stockList.observe(this, Observer {
            adapter.submitList(it)
        })

        //Observe if any error occurs show a message
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, "OOPS!, Something went wrong!", Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }
}
