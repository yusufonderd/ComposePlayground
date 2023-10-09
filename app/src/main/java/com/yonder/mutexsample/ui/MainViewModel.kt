package com.yonder.mutexsample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class MainViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private val currentUiState: UiState get() = uiState.value

    private val mutex = Mutex()

    fun addToCart() {
        if (mutex.isLocked) return
        setLoading()
        viewModelScope.launch(dispatcher) {
            mutex.withLock {
                val currentProducts = currentUiState.products.toMutableList()
                val productId = makeApiCall()
                currentProducts.add(productId)

                _uiState.update { state ->
                    state.copy(
                        products = currentProducts,
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

    private suspend fun makeApiCall(): String {
        delay(1.seconds)
        return UUID.randomUUID().toString()
    }

    data class UiState(
        val products: MutableList<String> = mutableListOf(),
        val isLoading: Boolean = false
    )

}

