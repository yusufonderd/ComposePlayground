package com.yonder.mutexsample.ui.home

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yonder.mutexsample.R
import com.yonder.mutexsample.ui.custom.HugeCenteredText
import com.yonder.mutexsample.ui.custom.LinearProgress
import com.yonder.mutexsample.ui.custom.StandardText

@Composable
fun HomeProductsScreen(
    uiState: HomeUiState.Success,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    val products = uiState.products

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {

        LinearProgress(isLoading = uiState.shouldShowProgress)

        HugeCenteredText(text = stringResource(R.string.added_products, uiState.productSize))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onAddToCart,
                enabled = uiState.isAddToCartButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                )
            ) {
                StandardText(text = stringResource(id = R.string.add_to_cart))
            }

            Button(
                onClick = onRemoveFromCart,
                enabled = uiState.isRemoveFromCartButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.red),
                    contentColor = Color.White
                )
            ) {
                StandardText(text = stringResource(id = R.string.remove_from_cart))
            }

        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(products) { index, product ->
                ProductListItem(
                    product = product,
                    index = index.inc(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                if (index < products.lastIndex) {
                    Divider()
                }
            }
        }
    }
}