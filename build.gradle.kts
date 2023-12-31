// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.android.library") version "8.2.0" apply false

    id("io.gitlab.arturbosch.detekt").version("1.23.3")
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"
    id("com.diffplug.spotless") version "6.23.3"
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint()
    }
}

detekt {
    // https://detekt.dev/docs/gettingstarted/gradle
    parallel = false
}
