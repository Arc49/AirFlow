package com.arc49.airflow.presentations.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect class CameraController {
    fun startCamera()
    fun stopCamera()
    suspend fun takePicture(): ByteArray
}

@Composable
expect fun Camera(
    modifier: Modifier = Modifier,
    onImageCaptured: (ByteArray) -> Unit,
    onError: (String) -> Unit
) 