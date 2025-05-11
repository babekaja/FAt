import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {


    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
  // alias(libs.plugins.buildKonfig)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
   // alias(libs.plugins.google.gms.google.services)
}

kotlin {




    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                sourceMaps = true
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }



    sourceSets {
        val desktopMain by getting




//        val wasmJsMain by getting {
//            dependencies {
//                // cœur Ktor
//                implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
//                // moteur JS
//                implementation("io.ktor:ktor-client-js:${libs.versions.ktor.get()}")
//                // JSON / serialization
//                implementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktor.get()}")
//                implementation("io.ktor:ktor-serialization-kotlinx-json:${libs.versions.ktor.get()}")
//                // si vous utilisez Coil via Ktor
//              //  implementation("io.coil-kt:coil-network-ktor:${libs.versions.coil.get()}")
//            }
//        }


        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)


            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.cryptography.provider.jdk)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.datastore.core)
            implementation("androidx.compose.ui:ui-text-google-fonts:1.7.8")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.navigation.compose)
           // implementation(libs.compose.adaptive)
          //  implementation(libs.kotlinx.coroutines)
            implementation(libs.coil.network.ktor)
            implementation(libs.bundles.ktor)
            implementation(libs.coil.compose)
            api(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.okio)
           // implementation(libs.androidx.room.runtime)
           // implementation(libs.sqlite.bundled)
            implementation(libs.cryptography.core)
            implementation(libs.kotlinx.datetime)
          //  implementation(libs.androidx.datastore.core)

            // Bibliothèque principale de widgets Cupertino
            implementation("io.github.alexzhirkevich:cupertino-core:0.1.0-alpha04")
            // Icônes SF Symbols étendues
            implementation("io.github.alexzhirkevich:cupertino-icons-extended:0.1.0-alpha04")

            implementation("io.github.alexzhirkevich:cupertino-adaptive:0.1.0-alpha04")
           // implementation("io.github.alexzhirkevich:cupertino-core:0.1.0-alpha04")
           // implementation("io.github.alexzhirkevich:cupertino-icons-extended:0.1.0-alpha04")


            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.0.0-alpha03")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.0.0-alpha03")
            implementation("org.jetbrains.compose.material3.adaptive:adaptive-navigation:1.0.0-alpha03")
            implementation("org.jetbrains.compose.material3:material3-window-size-class:1.7.3")

            implementation(compose.material3AdaptiveNavigationSuite)

            // implementation("io.github.ismai117:kottie:2.0.1")
            implementation("io.github.alexzhirkevich:compottie:2.0.0-rc04")
            // modules optionnels selon vos besoins :
            implementation("io.github.alexzhirkevich:compottie-dot:2.0.0-rc04")       // pour .lottie/ZIP
            implementation("io.github.alexzhirkevich:compottie-network:2.0.0-rc04")   // chargement Web

            implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1")


          //  implementation("io.ktor:ktor-client-okhttp:3.1.3")
            // implementation("org.jetbrains.compose.ui:ui-text-google-fonts:1.4.2")

         implementation("io.ktor:ktor-client-cio:3.1.3")


            implementation ("com.google.code.gson:gson:2.9.0")

         //   implementation("org.jetbrains.kotlin-wrappers:kotlin-stdlib-wasm-js:2.1.0")








            //   implementation("org.jetbrains.compose.material3.adaptive:adaptive-layout:1.1.0-beta02")








            // Adaptive navigation suite
          //  implementation(compose.material3AdaptiveNavigationSuite)


        }

        commonTest.dependencies {
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlin.test)
            @OptIn(ExperimentalComposeLibrary::class) implementation(compose.uiTest)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.cryptography.provider.jdk)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.datastore.core)
      //      implementation("com.google.accompanist:accompanist-adaptive:0.37.3")

        }



        dependencies {
            ksp(libs.androidx.room.compiler)
        }

        room {
            schemaDirectory("$projectDir/schemas")
        }
    }
}

android {
    namespace = "com.babe.fata"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.babe.fata"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.annotations)
    implementation(libs.annotations)

    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.babe.fata.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.babe.fata"
            packageVersion = "1.0.0"
        }
    }
}


