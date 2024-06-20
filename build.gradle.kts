// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose) apply false
    alias(libs.plugins.com.diffplug.spotless)
    alias(libs.plugins.com.google.gms.google.services) apply false
    alias(libs.plugins.com.google.firebase.crashlytics) apply false
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jlleitschuh.gradle.ktlint)
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint().setEditorConfigPath("$projectDir/.editorconfig")
    }
}

detekt {
    // https://detekt.dev/docs/gettingstarted/gradle
    parallel = false
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
    outputToConsole.set(true)
}
