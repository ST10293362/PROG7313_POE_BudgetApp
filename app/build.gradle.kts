plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "vcmsa.projects.prog7313_poe"
    compileSdk = 35
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    defaultConfig {
        applicationId = "vcmsa.projects.prog7313_poe"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Add Room schema export location
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        viewBinding = true
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

    kotlinOptions {
        jvmTarget = "11"
    }
}

// APP-LEVEL DEPENDENCIES
dependencies {
    
    // - Android
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // - Material Design
    
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // - Glide
    
    implementation(libs.glide)
    implementation(libs.identity.jvm)
    annotationProcessor(libs.glide.compiler) // Java
    kapt(libs.glide.compiler) // Kotlin

    // - Firebase
    
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    
    // - RoomDB
    
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)

    // - Kotlin coroutines with lifecycle support
    
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // - Testing
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // JUnit 5 (optional, but recommended for modern Kotlin tests)
    testImplementation(libs.junit.jupiter)

    // Coroutine test support
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // MockK for mocking classes/interfaces
    testImplementation("io.mockk:mockk:1.13.10")

    // Android Instrumentation tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation(libs.androidx.espresso.core)

    // JUnit 4 for unit testing
    testImplementation(libs.junit)

    // MockK for mocking classes and functions
    testImplementation("io.mockk:mockk:1.13.10")

    // Kotlin Coroutines Test library for testing coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    }
