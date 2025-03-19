package com.arc49.airflow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform