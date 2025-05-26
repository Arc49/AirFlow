package com.arc49.airflow.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class ScanResult(
    val id: String,
    val userId: String,
    val frontFaceUrl: String,
    val sideFaceUrl: String,
    val landmarks: Map<String, Float>,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

class ScanRepository(
    private val supabaseClient: SupabaseClient
) {
    private val storage = supabaseClient.storage
    private val BUCKET_NAME = "face-scans"

    suspend fun uploadFaceImage(
        imageBytes: ByteArray,
        isFrontFace: Boolean
    ): String {
        val fileName = "${Clock.System.now().toEpochMilliseconds()}_${if (isFrontFace) "front" else "side"}.jpg"
        
        storage.from(BUCKET_NAME).upload(
            path = fileName,
            data = imageBytes,
            upsert = true
        )

        return storage.from(BUCKET_NAME).publicUrl(fileName)
    }

    suspend fun saveScanResult(
        frontFaceUrl: String,
        sideFaceUrl: String,
        landmarks: Map<String, Float>
    ): ScanResult {
        val result = ScanResult(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            userId = "current_user_id", // TODO: Get from auth
            frontFaceUrl = frontFaceUrl,
            sideFaceUrl = sideFaceUrl,
            landmarks = landmarks
        )

        // Save to Supabase database
        supabaseClient.postgrest["scan_results"].insert(result)

        return result
    }

    fun getScanHistory(): Flow<List<ScanResult>> = flow {
        val results = supabaseClient.postgrest["scan_results"]
            .select()
            .order("timestamp", ascending = false)
            .decodeList<ScanResult>()
        
        emit(results)
    }
} 