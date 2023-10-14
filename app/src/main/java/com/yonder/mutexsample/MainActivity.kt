package com.yonder.mutexsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yonder.mutexsample.ui.MainScreen
import com.yonder.mutexsample.ui.MainViewModel
import com.yonder.mutexsample.ui.theme.MutexSampleTheme
import kotlinx.coroutines.Dispatchers


class MainActivity : ComponentActivity() {
    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(
                dispatcher = Dispatchers.IO
            ) as T
        }
    }
    private val viewModel: MainViewModel = factory.create(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MutexSampleTheme {
                val uiState by viewModel.uiState.collectAsState()
                MainScreen(
                    uiState = uiState,
                    onAddToCart = viewModel::addToCart,
                    onRemoveFromCart = viewModel::removeFromCart
                )
            }
        }
    }
}
