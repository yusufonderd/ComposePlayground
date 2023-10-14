package com.yonder.mutexsample.ui.home

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

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> get() = _uiState

    private val successState: HomeUiState.Success
        get() = uiState.value as? HomeUiState.Success ?: error("must access in Success state")

    private val mutex = Mutex()

    init {
        fetchProducts()
    }

    @VisibleForTesting
    fun fetchProducts() {
        _uiState.update { HomeUiState.Loading }
        viewModelScope.launch(dispatcher) {
            val products = getProductsFromApi()
            _uiState.update {
                HomeUiState.Success(
                    products = products,
                    shouldShowProgress = false
                )
            }
        }
    }

    fun addToCart() {
        if (mutex.isLocked) return
        setLoading()
        viewModelScope.launch(dispatcher) {
            mutex.withLock {
                val productId = makeApiCall()
                val newProducts = successState.products
                newProducts.add(Product(productId))
                _uiState.update {
                    HomeUiState.Success(
                        products = newProducts,
                        shouldShowProgress = false
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
                val newProducts = successState.products
                newProducts.removeLastOrNull()
                _uiState.update {
                    HomeUiState.Success(
                        products = newProducts,
                        shouldShowProgress = false
                    )
                }
            }
        }
    }

    private fun setLoading() {
        _uiState.update {
            HomeUiState.Success(
                products = successState.products,
                shouldShowProgress = true
            )
        }
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

    private suspend fun getProductsFromApi(productCount: Int = 3): MutableList<Product> {
        delay(1.seconds)
        return (1..productCount).map { Product(id = generateRandomProductId()) }.toMutableList()
    }

    private fun log(message: String) = Log.i(tag, message)

    private fun loge(message: String) = Log.e(tag, message)

}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val products: MutableList<Product>,
        val shouldShowProgress: Boolean
    ) : HomeUiState {
        val isRemoveFromCartButtonEnabled: Boolean get() = products.isNotEmpty() && shouldShowProgress.not()
        val isAddToCartButtonEnabled: Boolean get() = shouldShowProgress.not()
        val productSize: Int get() = products.size
    }
}

data class Product(val id: String)

