package com.codingchallenge.stockList

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.stockapp.data.model.StockISIN
import com.stockapp.data.model.Subscribe
import com.stockapp.data.network.SocketService.Companion.socketService
import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class StockListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("Main thread")
    private var viewModelJob = Job()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun subscribeList() {
        CoroutineScope(Dispatchers.Main + viewModelJob).launch {
            socketService.observeWebSocketEvent() // Observe the WebSocket connection
                .filter { it is WebSocket.Event.OnConnectionOpened<*> } // If connection is open
                .subscribe {
                    // Subscribe to stock ticker
                    socketService.subscribe(Subscribe(StockISIN.AMAZON))
                }

            socketService.observeTicker()   //Observe the stock ticker
                .subscribe {
                    Assert.assertEquals(StockISIN.AMAZON, it.isin)
                    Assert.assertNotNull(it.price)
                }
        }
    }
}