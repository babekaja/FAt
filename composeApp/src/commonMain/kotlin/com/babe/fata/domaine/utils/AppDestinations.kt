package com.babe.fata.domaine.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.FileVideo
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.BrainHeadProfile
import io.github.alexzhirkevich.cupertino.icons.outlined.PersonCircle
import io.github.alexzhirkevich.cupertino.icons.outlined.Video

enum class AppDestinations(
    val label : String,
    val icon : ImageVector,
    val contentDescription : String? = null
) {
    COURSES("Video", CupertinoIcons.Default.Video),
    PROFILE("Reglage", Icons.Default.Settings)
}