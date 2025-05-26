package com.arc49.airflow.presentations.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.AVKit.*
import platform.Foundation.*
import platform.QuartzCore.CALayer
import platform.UIKit.*

actual fun createVideoPlayer(): VideoPlayer = object : VideoPlayer {
    @OptIn(ExperimentalForeignApi::class)
    @Composable
    override fun VideoPlayer(
        videoUrl: String,
        modifier: Modifier,
        onPlaybackComplete: () -> Unit
    ) {
        val player = remember {
            AVPlayer(uRL = NSURL(string = videoUrl)!!).apply {
                addPeriodicTimeObserverForInterval(
                    interval = CMTimeMake(1, 1),
                    queue = dispatch_get_main_queue()
                ) { time ->
                    if (time.seconds >= currentItem?.duration?.seconds ?: 0.0) {
                        onPlaybackComplete()
                    }
                }
            }
        }

        DisposableEffect(
            UIKitView(
                factory = {
                    AVPlayerViewController().apply {
                        this.player = player
                        this.view.setBackgroundColor(UIColor.blackColor)
                    }
                },
                modifier = modifier
            )
        ) {
            player.play()
            onDispose {
                player.pause()
                player.replaceCurrentItemWithPlayerItem(null)
            }
        }
    }
} 