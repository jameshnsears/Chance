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
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation(project(mapOf("path" to ":module:ui-dialog-side")))
    implementation(project(mapOf("path" to ":module:ui-zoom")))
    implementation(project(mapOf("path" to ":module:ui-dialog-about")))
    implementation(project(mapOf("path" to ":module:data")))
    implementation(project(mapOf("path" to ":module:ui-dialog-colorpicker")))
    implementation(project(mapOf("path" to ":module:common")))
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(project(mapOf("path" to ":module:test")))

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}
