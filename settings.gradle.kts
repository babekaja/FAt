rootProject.name = "Fata"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        // Dépôt Compose “dev” pour les artefacts alpha/beta :
     //   maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }

        maven { url = uri("https://jitpack.io") }
        maven{url =  uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")}
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        // Dépôt Compose “dev” pour les artefacts alpha/beta :
     //   maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://jitpack.io") }
        maven{url =  uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")}

    }
}

include(":composeApp")

