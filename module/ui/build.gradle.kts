plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

android {
    namespace = "com.github.jameshnsears.chance.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val versionName = libs.versions.versionName.get()

    buildTypes {
        release {
            isMinifyEnabled = false
            buildConfigField("String", "VERSION", "\"${versionName}\"")
            buildConfigField("String", "GIT_HASH", "\"${gitHash()}\"")
        }

        debug {
            buildConfigField("String", "VERSION", "\"${versionName}\"")
            buildConfigField("String", "GIT_HASH", "\"${gitHash()}\"")
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }

        create("benchmarkRelease") {
        }

        create("nonMinifiedRelease") {
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    androidTestImplementation(project(":module:data-domain"))
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui.test.junit4)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.colorpicker.compose)
    implementation(libs.compose.icons.extended)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jsonschema.generator)
    implementation(libs.mockk)
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.java.util)
    implementation(libs.protobuf.kotlin)
    implementation(libs.slf4j.simple)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":module:common"))
    implementation(project(":module:data-common"))
    implementation(project(":module:data-domain"))
    implementation(project(":module:data-repo-api"))
    implementation(project(":module:data-repo-impl"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    testImplementation(platform(libs.org.junit.bom))
}

fun gitHash() = providers.exec {
    commandLine("git", "rev-parse", "--short=8", "HEAD")
}.standardOutput.asText.get().trim()
