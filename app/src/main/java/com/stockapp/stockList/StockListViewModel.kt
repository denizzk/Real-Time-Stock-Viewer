package com.stockapp.stockList

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stockapp.data.model.StockISIN
import com.stockapp.data.model.Subscribe
import com.stockapp.data.model.Unsubscribe
import com.stockapp.data.network.SocketService.Companion.socketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.tinder.scarlet.WebSocket

const val TAG = "StockListViewModel"

class StockListViewModel : ViewModel(), Observable {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _stockList = MutableLiveData<List<Pair<String, Triple<String, Double, String>>>>()
    val stockList: LiveData<List<Pair<String, Triple<String, Double, String>>>> get() = _stockList

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
                    enumValues<StockISIN>().forEach { socketService.subscribe(
                        Subscribe(it)
                    ) }
                }, { error ->
                    Log.e(TAG, "Error while observing socket ${error.cause}")
                })

            var map = mutableMapOf<String, Triple<String, Double, String>>()
            var triple = Triple<String, Double, String>("", 0.0, "")

            socketService.observeTicker()   //Observe the stocks ticker
                .subscribe({
                    Log.d(TAG, "${it.isin} new price ${it.price}")
                    //Get the stock origin code
                    var stockOrigin = StockISIN.valueOf(it.isin).value.substring(0, 2)
                    //Calculate the trend of stocks
                    if (map[it.isin]?.second != null) {
                        if (map[it.isin]!!.second.compareTo(it.price) < 0)
                            triple =
                                triple.copy(first = "up", second = it.price, third = stockOrigin)
                        else triple =
                            triple.copy(first = "down", second = it.price, third = stockOrigin)
                    }
                    map[it.isin] = triple   //Collect the stocks data
                    _stockList.postValue(map.toSortedMap().toList())   //Update stockList observed by StockListFragment
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
