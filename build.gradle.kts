// Top-level build file where you can add configuration options common to all sub-projects/./modules.
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
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.org.sonarqube)
}

sonarqube {
    properties {
        property("sonar.projectKey", "jameshnsears-github_chance")
        property("sonar.organization", "jameshnsears-github")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.branch.name", "main")
        property("sonar.sources", "**/src/main/kotlin")
        property("sonar.exclusions", "**/src/main/java")
        property("sonar.kotlin.binaries", "**/build/tmp/kotlin-classes/fdroidDebug")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/composable/**") // Exclude files under composable directories
    }
}

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

ktlint {
    android.set(true)
    outputColorName.set("RED")
    outputToConsole.set(true)
    filter {
        exclude("build.gradle.kts")
    }
}

val exclusions =
    listOf(
        "**/BuildConfig.*",
        "**/*Instrumented*.*",
        "**/*Test.*",
        "**/composable/*.*",
        "**/domain/proto/*.*"
    )

subprojects {
    apply(plugin = "jacoco")

    afterEvaluate {
        if (plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")) {
            tasks.register<JacocoReport>("jacocoFdroidTestReport") {
                group = "chance"
                description = "coverage - test"

                dependsOn("testFdroidDebugUnitTest")

                sourceDirectories.setFrom(
                    files(
                        fileTree("src/main/kotlin") {
                            include(
                                "**/*.kt",
                            )
                            exclude(exclusions)
                        },
                    ),
                )

                classDirectories.setFrom(
                    files(
                        fileTree("build") {
                            include(
                                "intermediates/javac/**/*.class",
                                "tmp/kotlin-classes/**/*.class"
                            )
                            exclude(exclusions)
                        },
                    ),
                )

                executionData.setFrom(
                    fileTree("build/outputs") {
                        include("**/*.exec")
                    },
                )

                reports {
                    xml.required.set(true)
                    xml.outputLocation.set(file("build/reports/jacoco/test.xml"))
                    html.required.set(true)
                }
            }

            //////////////////////////////////////////////////////

            tasks.register<JacocoReport>("jacocoFdroidAndroidTestReport") {
                group = "chance"
                description = "coverage - androidTest, API 33+"

                dependsOn("connectedFdroidDebugAndroidTest")

                sourceDirectories.setFrom(
                    files(
                        fileTree("src/main/kotlin") {
                            include(
                                "**/*.kt",
                            )
                            exclude(exclusions)
                        },
                    ),
                )

                classDirectories.setFrom(
                    files(
                        fileTree("build") {
                            include(
                                "intermediates/javac/**/*.class",
                                "tmp/kotlin-classes/**/*.class"
                            )
                            exclude(exclusions)
                        },
                    ),
                )

                executionData.setFrom(
                    fileTree("build/outputs") {
                        include(
                            "**/coverage.ec",
                            "**/testFdroidDebugUnitTest.exec"
                        )
                    },
                )

                reports {
                    xml.required.set(true)
                    xml.outputLocation.set(file("build/reports/jacoco/androidTest.xml"))
                    html.required.set(true)
                }
            }
        }
    }
}

apply(plugin = "jacoco")
tasks.register<JacocoReport>("jacocoCombinedReport") {
    group = "chance"
    description = "coverage - combined"

    sourceDirectories.setFrom(
        files(
            fileTree("module") {
                include(
                    "**/src/main/kotlin/**/*.kt",
                )
                exclude(exclusions)
            },
        ),
    )

    classDirectories.setFrom(
        files(
            fileTree(".") {
                include(
                    "**/build/intermediates/javac/**/*.class",
                    "**/build/tmp/kotlin-classes/**/*.class"
                )
                exclude(exclusions)
            },
        ),
    )

    executionData.setFrom(
        fileTree(".") {
            include(
                "**/coverage.ec",
                "**/testFdroidDebugUnitTest.exec"
            )
        },
    )

    reports {
        xml.required.set(false)
        html.outputLocation.set(file("build/reports/jacoco/combined"))
        html.required.set(true)
    }
}
