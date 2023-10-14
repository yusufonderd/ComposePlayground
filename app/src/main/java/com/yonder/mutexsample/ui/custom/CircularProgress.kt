package com.yonder.mutexsample.ui.custom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yonder.mutexsample.ui.theme.MutexSampleTheme

@Composable
fun CircularProgress(){
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .size(80.dp),
        color = MaterialTheme.colorScheme.tertiary,
    )
}


@Preview(showBackground = true)
@Composable
fun CircularProgressPreview() {
    MutexSampleTheme {
        CircularProgress()
    }
}