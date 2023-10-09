package com.yonder.mutexsample.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds

class MainViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private val currentUiState: UiState get() = uiState.value

    private val mutex = Mutex()

    init {
        startParallelRequests()
    }

    fun addToCart() {
        if (mutex.isLocked) return
        setLoading()
        viewModelScope.launch(dispatcher) {
            mutex.withLock {
                val productId = makeApiCall()
                _uiState.update { state ->
                    state.products.add(productId)
                    state.copy(
                        products = state.products,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun removeFromCart() {
        if (mutex.isLocked) return
        setLoading()
        viewModelScope.launch(dispatcher) {
            mutex.withLock {
                delay(1.seconds)
                currentUiState.products.removeLastOrNull()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun setLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun startParallelRequests() {
        viewModelScope.launch(dispatcher) {
            val time1 = measureTimeMillis {
                val result1 = async { makeApiCall(4) }
                val result2 = async { makeApiCall(3) }
                val res1 = result1.await()
                val res2 = result2.await()
                Log.i("tag", "result1 => $res1")
                Log.i("tag", "result2 => $res2")
                Log.i("tag", "result await together => ${res1 + res2} ")
            }
            Log.i("tag", "after await time => $time1")  // time1 takes 4 seconds

            val time2 = measureTimeMillis {
                val result3 = makeApiCall(4)
                val result4 = makeApiCall(3)
                Log.i("tag", "result3 => $result3")
                Log.i("tag", "result4 => $result4")
                Log.i("tag", "result normal together => ${result3 + result4} ")
            }
            Log.i("tag", "after normal time => $time2")  // time2 takes 7 seconds
        }
    }

    private suspend fun makeApiCall(second: Int = 1): String {
        delay(second.seconds)
        return UUID.randomUUID().toString()
    }

    data class UiState(
        val products: MutableList<String> = mutableListOf(),
        val isLoading: Boolean = false
    )

}

