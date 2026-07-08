// Top-level build file where you can add configuration options common to all modules.
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
    alias(libs.plugins.testretry) apply false
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
            "${rootProject.projectDir}/module/common/src/testFixtures/kotlin",
            "${rootProject.projectDir}/module/data-common/src/androidTest/kotlin",
            "${rootProject.projectDir}/module/data-common/src/main/kotlin",
            "${rootProject.projectDir}/module/data-common/src/test/kotlin",
            "${rootProject.projectDir}/module/data-domain/src/main/kotlin",
            "${rootProject.projectDir}/module/data-repo-api/src/main/kotlin",
            "${rootProject.projectDir}/module/data-repo-api/src/test/kotlin",
            "${rootProject.projectDir}/module/data-repo-impl/src/main/kotlin",
            "${rootProject.projectDir}/module/data-repo-impl/src/test/kotlin",
            "${rootProject.projectDir}/module/ui/src/androidTest/kotlin",
            "${rootProject.projectDir}/module/ui/src/main/kotlin",
            "${rootProject.projectDir}/module/ui/src/test/kotlin",
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
val inclusions = listOf(
    "com/github/jameshnsears/chance/**/*",
)

val exclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/domain/proto/*.*",
    "**/ComposableSingletons*.class",
    "**/*Test.class",
    "**/*Test$*.class",
    "**/*UnitTest.class",
    "**/*UnitTest$*.class",
)

val classDirPaths = listOf(
    "intermediates/built_in_kotlinc/fdroidDebug/compileFdroidDebugKotlin/classes",
    "intermediates/javac/fdroidDebug/classes",
    "tmp/kotlin-classes/fdroidDebug",
    "intermediates/runtime_library_classes_dir/fdroidDebug/bundleLibRuntimeToDirFdroidDebug"
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
    fun getAndroidProjects() = rootProject.subprojects.filter {
        it.plugins.hasPlugin("com.android.application") || it.plugins.hasPlugin("com.android.library")
    }

    tasks.register<JacocoReport>("jacocoFdroidTestReport") {
        val allAndroidProjects = getAndroidProjects()
        val targetProjects = if (project.path == ":app") allAndroidProjects else listOf(project)

        configureReport(this@configureJacoco, "unitTest")
        dependsOn(targetProjects.map { "${it.path}:testFdroidDebugUnitTest" })

        sourceDirectories.setFrom(allAndroidProjects.map {
            files("${it.projectDir}/src/main/java", "${it.projectDir}/src/main/kotlin")
        })
        classDirectories.setFrom(allAndroidProjects.map { proj ->
            classDirPaths.map { path ->
                fileTree(proj.layout.buildDirectory.dir(path)) {
                    include(inclusions)
                    exclude(exclusions)
                }
            }
        })
        executionData.setFrom(targetProjects.map { proj ->
            fileTree(proj.layout.buildDirectory) { include("**/*.exec", "**/*.ec") }
        })
    }

    // Combine AndroidTest (Instrumented) + Unit Test
    tasks.register<JacocoReport>("jacocoFdroidAndroidTestReport") {
        val allAndroidProjects = getAndroidProjects()
        val targetProjects = if (project.path == ":app") allAndroidProjects else listOf(project)

        configureReport(this@configureJacoco, "androidTest")
        dependsOn(targetProjects.map { "${it.path}:testFdroidDebugUnitTest" })
        dependsOn(targetProjects.map { "${it.path}:connectedFdroidDebugAndroidTest" })

        // Use allAndroidProjects for source and classes to allow cross-module coverage visibility
        sourceDirectories.setFrom(allAndroidProjects.map {
            files("${it.projectDir}/src/main/java", "${it.projectDir}/src/main/kotlin")
        })
        classDirectories.setFrom(allAndroidProjects.map { proj ->
            classDirPaths.map { path ->
                fileTree(proj.layout.buildDirectory.dir(path)) {
                    include(inclusions)
                    exclude(exclusions)
                }
            }
        })

        // Grab both local unit tests and instrumented results from all relevant projects
        executionData.setFrom(allAndroidProjects.map { proj ->
            fileTree(proj.layout.buildDirectory) {
                include(
                    "outputs/unit_test_code_coverage/**/*.exec",
                    "outputs/code_coverage/**/*.ec",
                    "outputs/managed_device_code_coverage/**/*.ec"
                )
            }
        })
    }
}

// Global Aggregated Report
tasks.register<JacocoReport>("jacocoCombinedReport") {
    configureReport(project, "combined")

    // Collect data from all subprojects
    val subProjectList = subprojects.filter {
        it.plugins.hasPlugin("com.android.application") || it.plugins.hasPlugin("com.android.library")
    }

    dependsOn(subProjectList.map { "${it.path}:testFdroidDebugUnitTest" })
    dependsOn(subProjectList.map { "${it.path}:connectedFdroidDebugAndroidTest" })

    sourceDirectories.setFrom(subProjectList.map {
        files("${it.projectDir}/src/main/java", "${it.projectDir}/src/main/kotlin")
    })
    classDirectories.setFrom(subProjectList.map { proj ->
        classDirPaths.map { path ->
            fileTree(proj.layout.buildDirectory.dir(path)) {
                include(inclusions)
                exclude(exclusions)
            }
        }
    })
    executionData.setFrom(subProjectList.map { proj ->
        fileTree(proj.layout.buildDirectory) { include("**/*.exec", "**/*.ec") }
    })
}
