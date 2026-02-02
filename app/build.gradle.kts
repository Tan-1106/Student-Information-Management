plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Firebase
    id("com.google.gms.google-services")
    // KSP
    id("com.google.devtools.ksp")
    // Dependency Injection - Hilt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.studentinformationmanagement"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.studentinformationmanagement"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // DEPENDENCIES
    // Navigation
    implementation(libs.navigation.compose)
    // UI
    implementation(libs.me.swipe)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.compose.material.material.icons.extended)
    // Image Loading
    implementation(libs.coil.kt.coil.compose)
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.auth)
    // Security
    implementation(libs.mindrot.jbcrypt)
    implementation(libs.favre.bcrypt)
    implementation(libs.androidx.security.crypto)
    // Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Dependency Injection - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // DataStore
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
}