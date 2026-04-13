plugins {
    alias(libs.plugins.com.android.library)
}

android {
    namespace = "com.github.jameshnsears.chance.data.domain"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(project(":module:common"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
