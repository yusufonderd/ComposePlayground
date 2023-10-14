package com.yonder.mutexsample.ui.custom

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.yonder.mutexsample.ui.theme.MutexSampleTheme

@Composable
fun LinearProgress(
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
fun LinearProgressPreview() {
    MutexSampleTheme {
        LinearProgress(true)
    }
}