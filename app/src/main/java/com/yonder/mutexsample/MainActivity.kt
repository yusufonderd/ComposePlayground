package com.yonder.mutexsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yonder.mutexsample.ui.main.HomeScreen
import com.yonder.mutexsample.ui.main.HomeViewModel
import com.yonder.mutexsample.ui.theme.MutexSampleTheme
import kotlinx.coroutines.Dispatchers


class MainActivity : ComponentActivity() {
    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                dispatcher = Dispatchers.IO
            ) as T
        }
    }
    private val viewModel: HomeViewModel = factory.create(HomeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MutexSampleTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(
                    uiState = uiState,
                    onAddToCart = viewModel::addToCart,
                    onRemoveFromCart = viewModel::removeFromCart
                )
            }
        }
    }
}
