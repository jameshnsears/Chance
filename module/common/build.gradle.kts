plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

android {
    namespace = "com.github.jameshnsears.chance.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        execution = "ANDROID_TEST_ORCHESTRATOR"

        unitTests {
            isIncludeAndroidResources = true
        }
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
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.appcompat)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.json.kotlin.schema)
    implementation(libs.mockk)
    implementation(libs.org.jetbrains.kotlinx.coroutines.test)
    implementation(libs.slf4j.simple)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
}
