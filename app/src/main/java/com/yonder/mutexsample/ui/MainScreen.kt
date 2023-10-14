package com.yonder.mutexsample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yonder.mutexsample.ui.theme.MutexSampleTheme

@Composable
fun MainScreen(
    uiState: MainViewModel.UiState,
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

            Text(
                text = "Eklenen ürün: ${uiState.products.size}",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text(text = "Add To Cart")
                }

                Button(
                    onClick = onRemoveFromCart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Remove From Cart")
                }

            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(uiState.products) { productId ->
                    Greeting(name = productId, modifier = Modifier.padding(12.dp))
                    Divider()
                }
            }
        }
    }
}


@Composable
fun LinearProgressIndicatorView(
    isLoading: Boolean, modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        modifier = modifier.alpha(
            if (isLoading) {
                1f
            } else {
                0f
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LinearProgressIndicatorViewPreview() {
    MutexSampleTheme {
        LinearProgressIndicatorView(true)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hello!")
        Text(text = name)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MutexSampleTheme {
        Greeting(name = "Android", modifier = Modifier.padding(12.dp))
    }
}