package com.babe.fata.ui.theme

import androidx.compose.ui.graphics.Color

// Netflix Brand Colors
val NetflixRed         = Color(0xFFE50914)
val NetflixDarkGray    = Color(0xFF221F1F)
val NetflixLightGray   = Color(0xFF737373)
val NetflixWhite       = Color(0xFFFFFFFF)

// Light Theme
val primaryLight               = NetflixRed
val onPrimaryLight             = NetflixWhite
val primaryContainerLight      = NetflixRed.copy(alpha = 0.12f)
val onPrimaryContainerLight    = NetflixRed

val secondaryLight             = NetflixLightGray
val onSecondaryLight           = NetflixWhite
val secondaryContainerLight    = NetflixLightGray.copy(alpha = 0.12f)
val onSecondaryContainerLight  = NetflixDarkGray

val backgroundLight            = NetflixWhite
val onBackgroundLight          = NetflixDarkGray
val surfaceLight               = NetflixWhite
val onSurfaceLight             = NetflixDarkGray

val errorLight                 = NetflixRed
val onErrorLight               = NetflixWhite

val outlineLight               = NetflixDarkGray
val scrimLight                 = Color(0x80000000) // 50% black

// Dark Theme
val primaryDark                = NetflixRed
val onPrimaryDark              = NetflixDarkGray
val primaryContainerDark       = NetflixRed.copy(alpha = 0.24f)
val onPrimaryContainerDark     = NetflixRed

val secondaryDark              = NetflixLightGray
val onSecondaryDark            = NetflixWhite
val secondaryContainerDark     = NetflixLightGray.copy(alpha = 0.24f)
val onSecondaryContainerDark   = NetflixDarkGray

// Passez ici en vrai noir
val backgroundDark             = Color(0xFF000000)
val onBackgroundDark           = NetflixWhite
// Idem si vous voulez que vos surfaces soient noires
val surfaceDark                = Color(0xFF000000)
val onSurfaceDark              = NetflixWhite

val errorDark                  = NetflixRed
val onErrorDark                = NetflixDarkGray

val outlineDark                = NetflixWhite.copy(alpha = 0.5f)
val scrimDark                  = Color(0x80000000) // 50% black
