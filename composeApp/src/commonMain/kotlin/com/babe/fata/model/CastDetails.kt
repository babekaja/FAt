package com.babe.fata.model

import kotlinx.serialization.Serializable

@Serializable
data class CastDetails(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String?,
    val place_of_birth: String?,
    val profile_path: String?
)
