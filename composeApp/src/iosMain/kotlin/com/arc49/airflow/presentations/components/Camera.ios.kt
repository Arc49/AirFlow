package com.arc49.airflow.presentations.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class CameraController {
    private var captureSession: AVCaptureSession? = null
    private var photoOutput: AVCapturePhotoOutput? = null
    private var previewLayer: AVCaptureVideoPreviewLayer? = null

    actual fun startCamera() {
        val session = AVCaptureSession()
        session.beginConfiguration()
        
        // Add video input
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null)
        if (session.canAddInput(input)) {
            session.addInput(input)
        }

        // Add photo output
        val photoOutput = AVCapturePhotoOutput()
        if (session.canAddOutput(photoOutput)) {
            session.addOutput(photoOutput)
            this.photoOutput = photoOutput
        }

        session.commitConfiguration()
        session.startRunning()
        this.captureSession = session
    }

    actual fun stopCamera() {
        captureSession?.stopRunning()
        captureSession = null
        photoOutput = null
        previewLayer = null
    }

    actual suspend fun takePicture(): ByteArray = suspendCoroutine { continuation ->
        val photoOutput = photoOutput ?: return@suspendCoroutine
        val settings = AVCapturePhotoSettings()
        
        photoOutput.capturePhotoWithSettings(
            settings,
            object : NSObject(), AVCapturePhotoCaptureDelegateProtocol {
                override fun captureOutput(
                    output: AVCapturePhotoOutput,
                    didFinishProcessingPhoto: AVCapturePhoto,
                    error: NSError?
                ) {
                    if (error != null) {
                        continuation.resume(ByteArray(0))
                        return
                    }

                    val imageData = didFinishProcessingPhoto.fileDataRepresentation()
                    if (imageData != null) {
                        val bytes = ByteArray(imageData.length.toInt())
                        imageData.getBytes(bytes, imageData.length)
                        continuation.resume(bytes)
                    } else {
                        continuation.resume(ByteArray(0))
                    }
                }
            }
        )
    }

    fun setPreviewLayer(layer: AVCaptureVideoPreviewLayer) {
        this.previewLayer = layer
        layer.session = captureSession
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Camera(
    modifier: Modifier,
    onImageCaptured: (ByteArray) -> Unit,
    onError: (String) -> Unit
) {
    val cameraController = remember { CameraController() }

    DisposableEffect(Unit) {
        cameraController.startCamera()
        onDispose {
            cameraController.stopCamera()
        }
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = {
            UIView().apply {
                backgroundColor = UIColor.blackColor()
            }
        },
        update = { view ->
            val previewLayer = AVCaptureVideoPreviewLayer()
            previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            previewLayer.frame = view.bounds
            view.layer.addSublayer(previewLayer)
            cameraController.setPreviewLayer(previewLayer)
        }
    )
} 