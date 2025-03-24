plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.next_gen_recipe_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.next_gen_recipe_app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SPOONACULAR_API_KEY", "\"${project.properties["SPOONACULAR_API_KEY"]}\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core AndroidX and Compose dependencies
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
    implementation(libs.material.icons.extended)
    implementation(libs.gson)

    // Retrofit & Moshi for API integration (Spoonacular)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)
    implementation(libs.firebase.coroutines)
    implementation(libs.coil.compose)
    implementation(libs.security.crypto)

    // WorkManager for background syncing
    implementation(libs.workManager)

    // Room for local data persistence using KSP for annotation processing
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler) // Using KSP for Room instead of KAPT

    // DataStore for lightweight preferences (e.g., dark mode)
    implementation(libs.datastore)

    // Navigation Compose for in-app navigation
    implementation(libs.navigation.compose)

    // ML Kit for recipe recommendations
    implementation("com.google.mlkit:object-detection:17.0.2")
    implementation(libs.mlkit.text.recognition)

    // Accompanist for shimmer effect and additional Compose utilities
    implementation(libs.accompanist.placeholder)
}
