package com.arc49.airflow.presentations.tabs

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arc49.airflow.presentations.components.Camera
import com.arc49.airflow.presentations.components.ScanHistory
import com.arc49.airflow.presentations.components.ScanResults
import com.arc49.airflow.presentations.viewmodels.ScanViewModel
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.launch

enum class ScanState {
    IDLE,
    FRONT_FACE,
    SIDE_FACE,
    PROCESSING,
    RESULTS,
    ERROR,
    HISTORY
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun ScanTab(
    modifier: Modifier,
    viewModel: ScanViewModel,
    onSettingsClick: () -> Unit
) {
    var cardOffset by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    val scanState by viewModel.scanState.collectAsState()
    val currentResult by viewModel.currentResult.collectAsState()
    val scanHistory by viewModel.scanHistory.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // Settings Button
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }

        // Main Content
        when (scanState) {
            ScanState.IDLE -> {
                SwipeableScanCard(
                    onStartScan = { viewModel.startNewScan() }
                )
            }
            ScanState.FRONT_FACE -> {
                FaceCaptureScreen(
                    title = "Front Face",
                    instruction = "Position your face straight ahead",
                    onCapture = { imageData ->
                        viewModel.captureImage(imageData, isFrontFace = true)
                    },
                    onError = { error ->
                        viewModel.handleError(error)
                    }
                )
            }
            ScanState.SIDE_FACE -> {
                FaceCaptureScreen(
                    title = "Side Face",
                    instruction = "Turn your head to the side",
                    onCapture = { imageData ->
                        viewModel.captureImage(imageData, isFrontFace = false)
                    },
                    onError = { error ->
                        viewModel.handleError(error)
                    }
                )
            }
            ScanState.PROCESSING -> {
                ProcessingScreen()
            }
            ScanState.RESULTS -> {
                currentResult?.let { result ->
                    ScanResults(
                        result = result,
                        onBackToScan = { viewModel.resetScan() }
                    )
                }
            }
            ScanState.ERROR -> {
                ErrorScreen(
                    message = viewModel.errorMessage ?: "An error occurred",
                    onRetry = { viewModel.resetScan() }
                )
            }
            ScanState.HISTORY -> {
                ScanHistory(
                    scans = scanHistory,
                    onScanSelected = { scan ->
                        viewModel.selectScan(scan)
                    },
                    onBack = { viewModel.resetScan() }
                )
            }
        }
    }
}

@Composable
private fun SwipeableScanCard(
    onStartScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Add AirFlow character illustration here
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onStartScan,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Start Scan",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FaceCaptureScreen(
    title: String,
    instruction: String,
    onCapture: (ByteArray) -> Unit,
    onError: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        Text(
            text = instruction,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Camera Preview
        Camera(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onImageCaptured = onCapture,
            onError = onError
        )
    }
}

@Composable
private fun ProcessingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Processing...",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = "Try Again",
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}

