import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.disasterpreparednessapp"
    compileSdk = 37

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

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        val mapsKey = localProperties.getProperty("MAPS_API_KEY") ?: ""
        val weatherKey = localProperties.getProperty("WEATHER_API_KEY") ?: ""
        val myKey = localProperties.getProperty("MY_API_KEY") ?: ""

        manifestPlaceholders["MAPS_API_KEY"] = mapsKey
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsKey\"")
        buildConfigField("String", "WEATHER_API_KEY", "\"$weatherKey\"")
        buildConfigField("String", "MY_API_KEY", "\"$myKey\"")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.2.0"
    }

}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    implementation(libs.googleid)
    implementation(libs.litert.support.api)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.room.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Jetpack Compose integration

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
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.work.runtime.ktx)

    // Lifecycle and Compose
    implementation(libs.androidx.lifecycle.runtime.compose.v262)
    implementation(libs.androidx.lifecycle.viewmodel.compose.v262)

    // Testing Dependencies
    testImplementation(libs.mockito.core.v5190)
    testImplementation(libs.mockito.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test.v1102)

    implementation(libs.jsoup)

    //andriod gms loaction
    implementation(libs.play.services.location)

    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler.v284)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)
}
