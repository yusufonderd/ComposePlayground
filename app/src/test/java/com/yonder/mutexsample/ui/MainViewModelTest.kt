package com.yonder.mutexsample.ui

import com.yonder.mutexsample.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val ADD_TIMES  = 10
    private val REMOVE_TIMES  = 5


    @Test
    fun `should call addToCart function multiple times`() = runTest {

        val viewModel = MainViewModel(mainDispatcherRule.testDispatcher)
        repeat(ADD_TIMES){
            viewModel.addToCart()
        }
        repeat(REMOVE_TIMES){
            viewModel.removeFromCart()
        }
        advanceUntilIdle()
        assertEquals(ADD_TIMES - REMOVE_TIMES, viewModel.uiState.value.products.size)
    }

}