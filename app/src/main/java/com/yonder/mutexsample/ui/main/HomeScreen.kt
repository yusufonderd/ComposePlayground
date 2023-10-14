package com.yonder.mutexsample.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yonder.mutexsample.R
import com.yonder.mutexsample.ui.custom.HugeCenteredText
import com.yonder.mutexsample.ui.custom.LinearProgressIndicatorView
import com.yonder.mutexsample.ui.custom.StandardText

@Composable
fun HomeScreen(
    uiState: HomeViewModel.UiState,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            LinearProgressIndicatorView(isLoading = uiState.isLoading)

            HugeCenteredText(text = stringResource(R.string.added_products, uiState.products.size))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = onAddToCart,
                    enabled = uiState.isLoading.not(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    StandardText(text = stringResource(id = R.string.add_to_cart))
                }

                Button(
                    onClick = onRemoveFromCart,
                    enabled = uiState.isLoading.not() && uiState.products.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    StandardText(text = stringResource(id = R.string.remove_from_cart))
                }

            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(uiState.products) { index, productId ->
                    ProductItem(
                        productId = productId,
                        index = index.inc(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                    Divider()
                }
            }
        }
    }
}

