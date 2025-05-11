package com.babe.fata

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.babe.fata.di.AppModule
import kotlinx.browser.document
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        // active les logs Koin au niveau INFO (ou Level.NONE pour réduire)
        printLogger()
        modules(AppModule)
    }
    ComposeViewport(document.body!!) {
        App()
    }
}
