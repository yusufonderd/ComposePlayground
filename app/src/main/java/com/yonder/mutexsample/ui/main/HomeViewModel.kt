package com.yonder.mutexsample.ui.main

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.seconds

class HomeViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel() {

    private val tag = HomeViewModel::class.java.simpleName

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private val currentUiState: UiState get() = uiState.value

    private val mutex = Mutex()

    init {
        fetchProducts()
    }

    @VisibleForTesting
    fun fetchProducts() {
        setLoading()
        viewModelScope.launch(dispatcher) {
            val products = getProductsFromApi()
            _uiState.update { it.copy(products = products.toMutableList(), isLoading = false) }
        }
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
            loge("starting parallel request")
            val time1 = measureTimeMillis {
                val result1 = async { makeApiCall(4) }
                val result2 = async { makeApiCall(3) }
                val res1 = result1.await()
                val res2 = result2.await()
                log("result1 => $res1")
                log("result2 => $res2")
                log("result parallel together => ${res1 + res2}")
            }
            loge("parallel requests time => $time1")

            loge("starting sequential request")
            val time2 = measureTimeMillis {
                val result3 = makeApiCall(4)
                val result4 = makeApiCall(3)
                log("result3 => $result3")
                log("result4 => $result4")
                log("result sequential together => ${result3 + result4} ")
            }
            loge("sequential request time => $time2")
        }
    }

    private suspend fun makeApiCall(second: Int = 1) = run {
        delay(second.seconds)
        generateRandomProductId()
    }

    private fun generateRandomProductId() = UUID.randomUUID().toString().take(n = 8)

    private suspend fun getProductsFromApi(productCount: Int = 3): List<String> {
        delay(1.seconds)
        return (1..productCount).map { generateRandomProductId() }
    }

    data class UiState(
        val products: MutableList<String> = mutableListOf(),
        val isLoading: Boolean = false
    )

    private fun log(message: String) = Log.i(tag, message)

    private fun loge(message: String) = Log.e(tag, message)

}

