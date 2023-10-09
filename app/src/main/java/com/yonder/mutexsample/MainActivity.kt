package com.yonder.mutexsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yonder.mutexsample.ui.MainViewModel
import com.yonder.mutexsample.ui.theme.MutexSampleTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(
                        dispatcher = Dispatchers.IO
                    ) as T
                }
            }

            val viewModel: MainViewModel = factory.create(MainViewModel::class.java)

            MutexSampleTheme {

                val uiState by viewModel.uiState.collectAsState()

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

                        LinearProgressIndicator(
                            modifier = Modifier.alpha(
                                alpha = if (uiState.isLoading) {
                                    1f
                                } else {
                                    0f
                                }
                            )
                        )

                        Text(
                            text = "Eklenen ürün: ${uiState.products.size}",
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = TextAlign.Center
                        )


                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                            Button(
                                onClick = viewModel::addToCart,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                            ) {
                                Text(text = "Add To Cart")
                            }

                            Button(
                                onClick = viewModel::removeFromCart,
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
                                Text(text = productId)
                            }
                        }

                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MutexSampleTheme {
        Greeting("Android")
    }
}