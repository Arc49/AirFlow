package com.arc49.airflow.presentations.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface VideoPlayer {
    @Composable
    fun VideoPlayer(
        videoUrl: String,
        modifier: Modifier = Modifier,
        onPlaybackComplete: () -> Unit = {}
    )
}

expect fun createVideoPlayer(): VideoPlayer 