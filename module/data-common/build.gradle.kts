plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

android {
    namespace = "com.github.jameshnsears.chance.data.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }

        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }

        create("benchmarkRelease") {
        }

        create("nonMinifiedRelease") {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        encoding = "UTF-8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE-notice.md,FastDoubleParser-LICENSE,FastDoubleParser-NOTICE,thirdparty-LICENSE}"
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
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
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
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":module:common"))
    implementation(project(":module:data-domain"))
    implementation(project(":module:data-repo-api"))
    implementation(project(":module:data-repo-impl"))
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
}
