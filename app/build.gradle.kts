plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.github.jameshnsears.chance"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.jameshnsears.chance"
        minSdk = 24
        targetSdk = 35

        // changelog | min sdk | max sdk
        versionCode = 112435

        versionName = "1.6.0"

        extra["versionName"] = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // The generated .map file, for google play store manual upload, in:
            // app/build/outputs/mapping/release/
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
        baseline = file("lint-baseline.xml")
        xmlReport = true
    }
}

kotlin {
    compilerOptions {
        if (System.getProperty("idea.active") == "true") {
            println("Enable coroutine debugging")
            freeCompilerArgs.add("-Xdebug")
        }
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
    "baselineProfile"(project(":module:baselineprofile"))
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // comment out leakcanary when profiling: fdroidDebug; on API 30+ emulator
    // run app, via Profiler (complete data)
    // Profiler > select Track Memory Consumption
    // Start profiler task (attached to selected process)
    debugImplementation(libs.leakcanary.android)

    "googleplayImplementation"(libs.com.google.firebase.crashlytics)
    "googleplayImplementation"(platform(libs.com.google.firebase.bom))
    implementation(libs.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":module:common"))
    implementation(project(":module:data"))
    implementation(project(":module:ui"))
    implementation(project(":module:ui-dialog-bag"))
}
