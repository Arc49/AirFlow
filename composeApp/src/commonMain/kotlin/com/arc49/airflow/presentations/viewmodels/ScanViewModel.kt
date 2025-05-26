package com.arc49.airflow.presentations.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arc49.airflow.data.ScanRepository
import com.arc49.airflow.data.ScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.core.Classifications
import org.tensorflow.lite.task.vision.facelandmarker.FaceLandmarker
import org.tensorflow.lite.task.vision.facelandmarker.FaceLandmarkerResult

sealed class ScanState {
    object IDLE : ScanState()
    object FRONT_FACE : ScanState()
    object SIDE_FACE : ScanState()
    object PROCESSING : ScanState()
    object RESULTS : ScanState()
    object ERROR : ScanState()
    object HISTORY : ScanState()
}

class ScanViewModel(
    private val repository: ScanRepository
) : ViewModel() {
    private val _scanState = MutableStateFlow<ScanState>(ScanState.IDLE)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()

    private val _currentResult = MutableStateFlow<ScanResult?>(null)
    val currentResult: StateFlow<ScanResult?> = _currentResult.asStateFlow()

    private val _scanHistory = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanHistory: StateFlow<List<ScanResult>> = _scanHistory.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var frontFaceImage: ByteArray? = null
    private var sideFaceImage: ByteArray? = null

    init {
        loadScanHistory()
    }

    fun startNewScan() {
        frontFaceImage = null
        sideFaceImage = null
        _currentResult.value = null
        errorMessage = null
        _scanState.value = ScanState.FRONT_FACE
    }

    fun captureImage(imageData: ByteArray, isFrontFace: Boolean) {
        if (isFrontFace) {
            frontFaceImage = imageData
            _scanState.value = ScanState.SIDE_FACE
        } else {
            sideFaceImage = imageData
            processImages()
        }
    }

    private fun processImages() {
        viewModelScope.launch {
            try {
                _scanState.value = ScanState.PROCESSING

                // Upload images to Supabase
                val frontFaceUrl = frontFaceImage?.let { repository.uploadFaceImage(it) }
                val sideFaceUrl = sideFaceImage?.let { repository.uploadFaceImage(it) }

                if (frontFaceUrl == null || sideFaceUrl == null) {
                    throw Exception("Failed to upload images")
                }

                // Process images with MediaPipe
                val landmarks = processWithMediaPipe(frontFaceImage!!, sideFaceImage!!)

                // Create and save scan result
                val result = ScanResult(
                    id = generateId(),
                    userId = "current_user", // TODO: Get from auth
                    frontFaceUrl = frontFaceUrl,
                    sideFaceUrl = sideFaceUrl,
                    landmarks = landmarks,
                    timestamp = System.currentTimeMillis()
                )

                repository.saveScanResult(result)
                _currentResult.value = result
                _scanState.value = ScanState.RESULTS

                // Refresh scan history
                loadScanHistory()
            } catch (e: Exception) {
                errorMessage = e.message ?: "An error occurred"
                _scanState.value = ScanState.ERROR
            }
        }
    }

    fun selectScan(scan: ScanResult) {
        _currentResult.value = scan
        _scanState.value = ScanState.RESULTS
    }

    fun showHistory() {
        _scanState.value = ScanState.HISTORY
    }

    fun resetScan() {
        frontFaceImage = null
        sideFaceImage = null
        _currentResult.value = null
        errorMessage = null
        _scanState.value = ScanState.IDLE
    }

    fun handleError(error: String) {
        errorMessage = error
        _scanState.value = ScanState.ERROR
    }

    private fun loadScanHistory() {
        viewModelScope.launch {
            try {
                val history = repository.getScanHistory()
                _scanHistory.value = history
            } catch (e: Exception) {
                errorMessage = "Failed to load scan history"
            }
        }
    }

    private suspend fun processWithMediaPipe(frontImage: ByteArray, sideImage: ByteArray): Map<String, Float> {
        // TODO: Implement MediaPipe processing
        // This is a placeholder that returns dummy data
        return mapOf(
            "jaw_width" to 120f,
            "face_height" to 180f,
            "nose_length" to 45f,
            "cheekbone_width" to 130f
        )
    }

    private fun generateId(): String {
        return "scan_${System.currentTimeMillis()}"
    }
} 