// src/commonMain/kotlin/com/babe/fata/model/CreditsResponse.kt
package com.babe.fata.model

import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponse(
    val id: Int,
    val cast: List<CastMember>
)

@Serializable
data class CastMember(
    val adult: Boolean,
    val gender: Int?,                      // 1 = femme, 2 = homme, null = inconnu
    val id: Int,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String? = null,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int
)
