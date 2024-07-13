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
    filter {
        exclude("build.gradle.kts")
    }
}

subprojects {
    apply(plugin = "jacoco")
    afterEvaluate {
        if (plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")) {
            tasks.register<JacocoReport>("jacocoTestReport") {
                group = "chance"
                description = "coverage - test"

                dependsOn("testFdroidDebugUnitTest")

                val exclusions =
                    listOf(
                        "**/BuildConfig.*",
                        "**/compose/*.*",
                        "**/*Preview.*",
                        "**/impl/*.*",
                        "**/domain/proto/*.*",
                        "**/UtilityJsonSchemaGenerator*.*",
                        "**/R.class",
                        "**/R\$*.class",
                        "**/Manifest*.*",
                        "**/kotlin/**",
                        "**/kotlinx/**",
                        "**/generated/**",
                        "**/*$*.class",
                        "**/*Companion*.class",
                        "**/*Lambda*.class",
                        "**/*\$WhenMappings*.class",
                        "**/*\$DefaultImpls*.class",
                        "**/META-INF/versions/**",
                        "**/*\$lambda\$*.class",
                        "**/*Test.class",
                        "**/*Test*.class",
                        "**/*TestTag.class",
                        "**/*Instrumented*.class",
                    )

                sourceDirectories.setFrom(
                    files(
                        fileTree("src/main") {
                            include(
                                "**/*.kt",
                                "**/*.java",
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
                description = "coverage - androidTest API 33+"

                dependsOn("connectedFdroidDebugAndroidTest")

                val exclusions =
                    listOf(
                        "**/BuildConfig.*",
                        "**/*Preview.*",
                        "**/mock/*.*",
                        "**/domain/proto/*.*",
                        "**/UtilityJsonSchemaGenerator*.*",
                        "**/R.class",
                        "**/R\$*.class",
                        "**/Manifest*.*",
                        "**/kotlin/**",
                        "**/kotlinx/**",
                        "**/generated/**",
                        "**/*$*.class",
                        "**/*Companion*.class",
                        "**/*Lambda*.class",
                        "**/*\$WhenMappings*.class",
                        "**/*\$DefaultImpls*.class",
                        "**/META-INF/versions/**",
                        "**/*\$lambda\$*.class",
                        "**/*ComposableKt.class",
                        "**/*Test.class",
                        "**/*Test*.class",
                        "**/*TestTag.class",
                        "**/*Instrumented*.class",
                    )

                sourceDirectories.setFrom(
                    files(
                        fileTree("src/main") {
                            include(
                                "**/*.kt",
                                "**/*.java",
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

    val exclusions =
        listOf(
            "**/BuildConfig.*",
            "**/*Preview.*",
            "**/mock/*.*",
            "**/domain/proto/*.*",
            "**/UtilityJsonSchemaGenerator*.*",
            "**/R.class",
            "**/R\$*.class",
            "**/Manifest*.*",
            "**/kotlin/**",
            "**/kotlinx/**",
            "**/generated/**",
            "**/*$*.class",
            "**/*Companion*.class",
            "**/*Lambda*.class",
            "**/*\$WhenMappings*.class",
            "**/*\$DefaultImpls*.class",
            "**/META-INF/versions/**",
            "**/*\$lambda\$*.class",
            "**/*ComposableKt.class",
            "**/*Test.class",
            "**/*Test*.class",
            "**/*TestTag.class",
            "**/*Instrumented*.class",
        )

    sourceDirectories.setFrom(
        files(
            fileTree("module") {
                include(
                    "**/src/main/**/*.kt",
                    "**/src/main/**/*.java",
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
