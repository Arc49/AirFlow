package com.arc49.airflow.presentations.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arc49.airflow.data.ScanResult
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ScanResults(
    result: ScanResult,
    onBackToScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Analysis Results",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Facial Measurements
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Facial Measurements",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Key Measurements
                MeasurementRow("Jaw Width", result.landmarks["jaw_width"] ?: 0f)
                MeasurementRow("Face Height", result.landmarks["face_height"] ?: 0f)
                MeasurementRow("Nose Length", result.landmarks["nose_length"] ?: 0f)
                MeasurementRow("Cheekbone Width", result.landmarks["cheekbone_width"] ?: 0f)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Facial Proportions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Facial Proportions",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Calculate and display proportions
                val jawWidth = result.landmarks["jaw_width"] ?: 0f
                val faceHeight = result.landmarks["face_height"] ?: 0f
                val noseLength = result.landmarks["nose_length"] ?: 0f
                
                if (faceHeight > 0) {
                    ProportionRow("Jaw-to-Face Ratio", jawWidth / faceHeight)
                    ProportionRow("Nose-to-Face Ratio", noseLength / faceHeight)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scan Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Scan Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val dateTime = Instant.fromEpochMilliseconds(result.timestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                
                DetailRow("Date", "${dateTime.date}")
                DetailRow("Time", "${dateTime.hour}:${dateTime.minute}")
                DetailRow("Scan ID", result.id)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackToScan,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = "New Scan",
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun MeasurementRow(
    label: String,
    value: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = String.format("%.2f", value),
            color = Color.White
        )
    }
}

@Composable
private fun ProportionRow(
    label: String,
    value: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = String.format("%.2f", value),
            color = Color.White
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            color = Color.White
        )
    }
} 