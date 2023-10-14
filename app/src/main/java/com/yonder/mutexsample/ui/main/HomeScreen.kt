package com.yonder.mutexsample.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yonder.mutexsample.ui.custom.CircularProgress

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            HomeUiState.Loading -> CircularProgress()
            is HomeUiState.Success -> HomeProductsScreen(
                uiState = uiState,
                onAddToCart = onAddToCart,
                onRemoveFromCart = onRemoveFromCart
            )
        }
    }
}

