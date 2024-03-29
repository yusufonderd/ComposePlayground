package com.yonder.mutexsample.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yonder.mutexsample.ui.custom.HeaderText
import com.yonder.mutexsample.ui.custom.StandardText
import com.yonder.mutexsample.ui.theme.MutexSampleTheme


@Composable
fun ProductListItem(product: Product, index: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText("$index")
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StandardText(text = "Hello")
            StandardText(text = product.id)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    MutexSampleTheme {
        ProductListItem(
            product = Product("Android"),
            index = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }
}