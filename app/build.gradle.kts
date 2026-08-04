plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.2.0"
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.gms.google.services)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.disasterpreparednessapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.disasterpreparednessapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.2.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    implementation(libs.litert.support.api)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    //Retrofit and gson-Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.simplexml)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    implementation (libs.androidx.lifecycle.viewmodel)
    implementation (libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.runtime.livedata)

    //coil
    implementation(libs.coil.compose)

    //material design
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.datastore.preferences)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt Dependency Injection with KSP
    implementation(libs.hilt.android.v254)
    ksp(libs.hilt.android.compiler.v254)
    implementation(libs.hilt.navigation.compose)
    
    // Lifecycle and Compose
    implementation(libs.androidx.lifecycle.runtime.compose.v262)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v262)
    
    // Testing Dependencies
    testImplementation(libs.mockito.core.v5190)
    testImplementation(libs.mockito.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test.v1102)

    implementation(libs.jsoup)

}
