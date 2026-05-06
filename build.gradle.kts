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
val d = "$"
val inclusions = listOf(
    "**/tmp/kotlin-classes/fdroidDebug/**/*.class",
    "**/intermediates/javac/fdroidDebug/classes/**/*.class",
    "**/intermediates/built_in_kotlinc/fdroidDebug/**/*.class"
)

val exclusions = listOf(
    "**/*${d}Companion${d}*.*",
    "**/*${d}DefaultImpls${d}*.*",
    "**/*${d}inlined${d}*.*",
    "**/*${d}Lambda${d}*.*",
    "**/*${d}MethodHandler${d}*.*",
    "**/*${d}Preview${d}*.*",
    "**/*${d}SAM${d}*.*",
    "**/BuildConfig.*",
    "**/composable/*.*",
    "**/*Composable${d}*.*",
    "**/domain/proto/*.*",
    "**/*_Factory*.*",
    "**/*Instrumented*.*",
    "**/Manifest*.*",
    "**/*_MembersInjector*.*",
    "**/*_Provide*.*",
    "**/R${d}*.class",
    "**/R.class",
    "**/*Test.*",
    "**/*_ViewBinding*.*",
)

// Helper to configure reports consistently
fun JacocoReport.configureReport(project: Project, type: String) {
    group = "chance"
    description = "Generate Jacoco $type coverage reports."

    reports {
        xml.required.set(true)
        xml.outputLocation.set(project.layout.buildDirectory.file("reports/jacoco/$type.xml"))
        html.required.set(true)
        html.outputLocation.set(project.layout.buildDirectory.dir("reports/jacoco/$type"))
    }
}

// Access version catalog safely
val libsCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val jacocoVersion = libsCatalog.findVersion("jacocoVersion").get().requiredVersion

subprojects {
    apply(plugin = "jacoco")

    extensions.configure<JacocoPluginExtension> {
        toolVersion = jacocoVersion
    }

    // Use plugins.withType instead of afterEvaluate for better performance
    plugins.withId("com.android.application") { configureJacoco() }
    plugins.withId("com.android.library") { configureJacoco() }
}

fun Project.configureJacoco() {
    tasks.register<JacocoReport>("jacocoFdroidTestReport") {
        configureReport(this@configureJacoco, "unitTest")
        dependsOn("testFdroidDebugUnitTest")

        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        classDirectories.setFrom(
            fileTree(layout.buildDirectory) {
                setIncludes(inclusions)
                setExcludes(exclusions)
            }
        )
        executionData.setFrom(
            fileTree(layout.buildDirectory) { include("**/*.exec", "**/*.ec") }
        )
    }

    // Combine AndroidTest (Instrumented) + Unit Test
    tasks.register<JacocoReport>("jacocoFdroidAndroidTestReport") {
        configureReport(this@configureJacoco, "androidTest")
        dependsOn("testFdroidDebugUnitTest", "connectedFdroidDebugAndroidTest")

        sourceDirectories.setFrom(files("$projectDir/src/main/java", "$projectDir/src/main/kotlin"))
        classDirectories.setFrom(fileTree(layout.buildDirectory) {
            setIncludes(inclusions)
            setExcludes(exclusions)
        })

        // Grab both local unit tests and instrumented results
        // Support for Orchestrator: .ec files are often nested in subdirectories
        executionData.setFrom(
            fileTree(layout.buildDirectory) {
                include(
                    "outputs/unit_test_code_coverage/**/*.exec",
                    "outputs/code_coverage/**/*.ec",
                    "outputs/managed_device_code_coverage/**/*.ec"
                )
            }
        )
    }
}

// Global Aggregated Report
tasks.register<JacocoReport>("jacocoCombinedReport") {
    configureReport(project, "combined")

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
