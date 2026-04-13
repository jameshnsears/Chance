// Top-level build file where you can add configuration options common to all sub-projects/./modules.
plugins {
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.com.diffplug.spotless)
    alias(libs.plugins.com.google.firebase.crashlytics) apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose) apply false
    alias(libs.plugins.org.jlleitschuh.gradle.ktlint)
    alias(libs.plugins.org.sonarqube)
}

// ────────────────────────────────────────────────────────────────────

sonarqube {
    properties {
        property("sonar.branch.name", "main")
        property("sonar.cpd.exclusions", "**/src/main/java/**")
        property("sonar.exclusions", "**/src/main/java/**")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.kotlin.binaries", "**/build/tmp/kotlin-classes/fdroidDebug")
        property("sonar.organization", "jameshnsears-github")
        property("sonar.projectKey", "jameshnsears_Chance")
        property("sonar.sources", "**/src/main/kotlin")
    }
}

// ────────────────────────────────────────────────────────────────────

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/composable/**") // Exclude files under composable directories
    }
}

// ────────────────────────────────────────────────────────────────────

detekt {
    // https://detekt.dev/docs/gettingstarted/gradle
    parallel = true

    source.setFrom(
        files(
            "${rootProject.projectDir}/app/src/androidTest/kotlin",
            "${rootProject.projectDir}/app/src/main/kotlin",
            "${rootProject.projectDir}/module/common/src/main/kotlin",
            "${rootProject.projectDir}/module/data/src/androidTest/kotlin",
            "${rootProject.projectDir}/module/data/src/main/kotlin",
            "${rootProject.projectDir}/module/data/src/test/kotlin",
            "${rootProject.projectDir}/module/ui/src/main/kotlin",
            "${rootProject.projectDir}/module/ui/src/test/kotlin",
            "${rootProject.projectDir}/module/ui-dialog-bag/src/androidTest/kotlin",
            "${rootProject.projectDir}/module/ui-dialog-bag/src/main/kotlin",
            "${rootProject.projectDir}/module/ui-dialog-bag/src/test/kotlin",
        )
    )

    debug = true

    ignoreFailures = false

    buildUponDefaultConfig = true

//    baseline = file("${rootProject.projectDir}/app/detekt-baseline.xml")

    parallel = true

    config.setFrom("${rootProject.projectDir}/detekt.yml")
}

// ────────────────────────────────────────────────────────────────────

ktlint {
    android.set(true)
    outputColorName.set("RED")
    outputToConsole.set(true)
    filter {
        exclude("build.gradle.kts")
    }
}

// ────────────────────────────────────────────────────────────────────

// 1. Centralize configuration to avoid duplication
val inclusions = listOf(
    "**/intermediates/built_in_kotlinc/**/*.class",
    "**/intermediates/javac/**/*.class"
)

val exclusions = listOf(
    "**/BuildConfig.*",
    "**/*Instrumented*.*",
    "**/*Test.*",
    "**/composable/*.*",
    "**/domain/proto/*.*",
    "**/R.class",
    "**/R$*.class",
    "**/Manifest*.*"
)

// Helper to configure reports consistently
fun JacocoReport.configureReport(type: String) {
    group = "chance"
    description = "Generate Jacoco $type coverage reports."

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/$type"))
    }
}

subprojects {
    apply(plugin = "jacoco")

    // Use plugins.withType instead of afterEvaluate for better performance
    plugins.withId("com.android.application") { configureJacoco() }
    plugins.withId("com.android.library") { configureJacoco() }
}

fun Project.configureJacoco() {
    tasks.register<JacocoReport>("jacocoFdroidTestReport") {
        configureReport("unitTest")
        dependsOn("testFdroidDebugUnitTest")

        val buildDir = layout.buildDirectory.asFile.get()

        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        classDirectories.setFrom(
            fileTree(buildDir) {
                setIncludes(inclusions)
                setExcludes(exclusions)
            }
        )
        executionData.setFrom(
            fileTree(buildDir) { include("**/*.exec", "**/*.ec") }
        )
    }

    // Combine AndroidTest (Instrumented) + Unit Test
    tasks.register<JacocoReport>("jacocoFdroidAndroidTestReport") {
        configureReport("androidTest")
        dependsOn("connectedFdroidDebugAndroidTest")

        val buildDir = layout.buildDirectory.asFile.get()

        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        classDirectories.setFrom(fileTree(buildDir) {
            setIncludes(inclusions)
            setExcludes(exclusions)
        })

        // Grab both local unit tests and instrumented results
        executionData.setFrom(
            fileTree(buildDir) { include("outputs/unit_test_code_coverage/**/*.exec", "outputs/code_coverage/**/*.ec") }
        )
    }
}

// Global Aggregated Report
tasks.register<JacocoReport>("jacocoCombinedReport") {
    configureReport("combined")

    // Collect data from all subprojects
    val subProjectList = subprojects.filter {
        it.plugins.hasPlugin("com.android.application") || it.plugins.hasPlugin("com.android.library")
    }

    sourceDirectories.setFrom(subProjectList.map { "${it.projectDir}/src/main/kotlin" })
    classDirectories.setFrom(subProjectList.map { proj ->
        fileTree(proj.layout.buildDirectory) {
            setIncludes(inclusions)
            setExcludes(exclusions)
        }
    })
    executionData.setFrom(subProjectList.map { proj ->
        fileTree(proj.layout.buildDirectory) { include("**/*.exec", "**/*.ec") }
    })
}
