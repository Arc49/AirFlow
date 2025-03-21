package com.arc49.airflow.domain.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(
    @SerialName("user_id")
    val userId: String?,
    @SerialName("email")
    val email: String,
    @SerialName("subscription")
    val subscription: Subscription
)

@Serializable
data class Subscription(
    @SerialName("isSubscribed")
    val isSubscribed: Boolean,
    @SerialName("subscriptionCode")
    val subscriptionCode: String,
    @SerialName("startSubscribed")
    val startSubscribed: String,
    @SerialName("endSubscribed")
    val endSubscribed: String,
)