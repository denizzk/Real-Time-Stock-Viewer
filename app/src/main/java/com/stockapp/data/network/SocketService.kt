package com.stockapp.data.network

import com.stockapp.data.model.Subscribe
import com.stockapp.data.model.TickerResponse
import com.stockapp.data.model.Unsubscribe
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

const val SOCKET_BASE_URL = "ws://159.89.15.214:8080/"

interface SocketService {
    @Send
    fun subscribe(action: Subscribe)

    @Send
    fun unsubscribe(action: Unsubscribe)

    @Receive
    fun observeTicker(): Flowable<TickerResponse>

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    companion object {

        private val backoffStrategy = ExponentialWithJitterBackoffStrategy(5000, 5000)

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val socketService = Scarlet.Builder()
            .webSocketFactory(
                okHttpClient.newWebSocketFactory(
                    SOCKET_BASE_URL
                ))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .backoffStrategy(backoffStrategy)
            .build()
            .create<SocketService>()
    }
}