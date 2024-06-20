import com.android.build.api.dsl.LintOptions

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.com.google.gms.google.services)
}

android {
    namespace = "com.github.jameshnsears.chance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.jameshnsears.chance"
        minSdk = 24
        targetSdk = 34
        versionCode = 12434
        versionName = "1.0.0"
        extra["versionName"] = versionName

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
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    debugImplementation(libs.leakcanary.android)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    "googleplayImplementation"(platform(libs.com.google.firebase.bom))
    "googleplayImplementation"(platform(libs.com.google.firebase.crashlytics))
    implementation(project(":module:common"))
    implementation(project(":module:data"))
    implementation(project(":module:test"))
    implementation(project(":module:ui"))
    implementation(project(":module:ui-dialog-bag"))
}
