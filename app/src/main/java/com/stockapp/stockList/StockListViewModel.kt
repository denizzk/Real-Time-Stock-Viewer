package com.stockapp.stockList

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stockapp.data.model.StockISIN
import com.stockapp.data.model.Subscribe
import com.stockapp.data.model.TickerResponse
import com.stockapp.data.network.SocketService.Companion.socketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.tinder.scarlet.WebSocket
import kotlin.math.absoluteValue

const val TAG = "StockListViewModel"

class StockListViewModel : ViewModel(), Observable {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _stockList = MutableLiveData<List<TickerResponse>>()
    val stockList: LiveData<List<TickerResponse>> get() = _stockList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        subscribeStockList()
    }

    fun subscribeStockList() {
        uiScope.launch {
            socketService.observeWebSocketEvent() // Observe the WebSocket connection
                .filter { it is WebSocket.Event.OnConnectionOpened<*> } // If connection is open
                .subscribe({
                    // Subscribe to each stocks ticker
                    enumValues<StockISIN>().forEach {
                        socketService.subscribe(
                            Subscribe(it)
                        )
                    }
                }, { error ->
                    Log.e(TAG, "Error while observing socket ${error.cause}")
                })

            var stocksMap = mutableMapOf<String, TickerResponse>()

            socketService.observeTicker()   //Observe the stocks ticker
                .subscribe({
//                    Log.d(TAG, "${it.isin} new price ${it.price}")
                    //Get the stock origin code
                    it.origin = StockISIN.valueOf(it.isin).value.substring(0, 2)
                    //Calculate the trend of stocks
                    it.trend = "flat"
                    if (stocksMap[it.isin] != null) {
                        val prevPrice = stocksMap[it.isin]!!.price
                        val curPrice = it.price
                        val priceChange = curPrice - prevPrice
                        it.trendRatio = priceChange / prevPrice
                        it.trend = if (priceChange > 0) "up" else "down"

                    }

                    stocksMap[it.isin] = it //Collect the stocks data
                    //Update stockList observed by StockListFragment
                    _stockList.postValue(stocksMap.toSortedMap().values.toList())

                }, { error ->
                    Log.e(TAG, "Error while observing ticker ${error.cause}")
                    _errorMessage.value = error.message
                })
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

//    fun unsubscribeStockList() {
//        uiScope.launch {
//            // Unsubscribe from each subscribed stocks ticker
//            enumValues<StockISIN>().forEach { socketService.unsubscribe(Unsubscribe(it)) }
//
//        }
//    }
}
