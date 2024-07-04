plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

android {
    namespace = "com.github.jameshnsears.chance.ui.dialog.bag"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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

    lint {
        xmlReport = true
    }
}

dependencies {
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.datastore.core)
    androidTestImplementation(libs.androidx.datastore.preferences)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.coil.compose)
    androidTestImplementation(libs.coil.svg)
    androidTestImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.protobuf.kotlin)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(project(":module:common"))
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.colorpicker.compose)
    implementation(libs.mockk)
    implementation(libs.slf4j.simple)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":module:common"))
    implementation(project(":module:data"))
    testImplementation(libs.kotlin.test)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.org.junit.jupiter)
    testImplementation(platform(libs.org.junit.bom))
}
