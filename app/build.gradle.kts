plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.github.jameshnsears.chance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.jameshnsears.chance"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "24.34.1"

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
                "proguard-rules.pro",
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
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE-notice.md}"
        }
    }

    flavorDimensions += listOf("store")
    productFlavors {
        create("fdroid") {
            dimension = "store"
        }
        create("googleplay") {
            dimension = "store"
        }
    }
}

dependencies {
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation(project(":module:common"))
    implementation(project(":module:data"))
    implementation(project(":module:test"))
    implementation(project(":module:ui-dialog-bag"))
    implementation(project(":module:ui-tab"))
    implementation(project(":module:ui-zoom"))
}
