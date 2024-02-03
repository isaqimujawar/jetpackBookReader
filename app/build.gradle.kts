plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")

}

android {
    namespace = "com.maddy.jetpackbookreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maddy.jetpackbookreader"
        minSdk = 29
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    val composeVersion = "1.6.0"
    val hiltVersion = "2.50"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.2.0-rc01")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    // Truth Assertion
    testImplementation ("com.google.truth:truth:1.3.0")
    // Mockk Library
    testImplementation("io.mockk:mockk:1.13.9")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Hilt Dagger Dependency Injection
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    // Hilt Kapt annotation processor
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // Hilt Integration with the Jetpack navigation library
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Firebase Dependencies
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    // Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")


    // Coroutine Lifecycle Scopes
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Jetpack Compose Navigation Component
    implementation ("androidx.navigation:navigation-compose:2.7.6")

    // Coil Library for Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Retrofit Dependencies
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    // GSON converter
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}

// Kapt to allow references to generated code
kapt {
    correctErrorTypes = true
}