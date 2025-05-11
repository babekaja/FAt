package com.babe.fata

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.babe.fata.di.AppModule
import org.koin.core.context.GlobalContext.startKoin


fun main() = application {

    startKoin {
        modules(AppModule)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Fata",
    ) {
        App()
    }
}