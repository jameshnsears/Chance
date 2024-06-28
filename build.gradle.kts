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
    id("org.sonarqube") version libs.versions.org.sonarqube
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
                description = "coverage"

                // doesn't produce .exec from androidTest
                // +
                // can't get coverage in Android Studio either for androidTest
                // dependsOn("testFdroidDebugUnitTest","connectedFdroidDebugAndroidTest")
                dependsOn("testFdroidDebugUnitTest")

                val exclusions =
                    listOf(
                        "**/BuildConfig.*",
                        "**/compose/*.*",
                    )

                sourceDirectories.setFrom(
                    files(
                        fileTree("src") {
                            include(
                                "main/kotlin/**/*.kt",
                                "main/java/**/*.java",
                                "androidTest/kotlin/**/*.kt",
                            )
                            exclude(exclusions)
                        },
                    ),
                )

                classDirectories.setFrom(
                    files(
                        fileTree("build") {
                            include(
                                "intermediates/javac/fdroidDebug/**/*.class",
                                "tmp/kotlin-classes/**/*.class")
                            exclude(exclusions)
                        },
                    ),
                )

                executionData.setFrom(
                    fileTree("build/outputs") {
                        include("unit_test_code_coverage/**/*.exec")
                    },
                )

                reports {
                    xml.required.set(true)
                    xml.outputLocation.set(file("build/reports/jacoco/TEST-report.xml"))
                    html.required.set(true)
                }
            }
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "jameshnsears-github_chance")
        property("sonar.organization", "jameshnsears-github")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.sources",
            "./module/common/src/main/kotlin/**/*.kt,./module/data/src/main/java/**/*.kt,./module/data/src/main/kotlin/**/*.kt,./module/test/src/main/kotlin/**/*.kt,./module/ui/src/main/kotlin/**/*.kt,./module/ui-dialog-bag/src/main/kotlin/**/*.kt",
        )
        property("sonar.exclusions", "module/data/src/res/**/*.xml,module/data/src/main/java/**/*.java,**/androidTest/**/*.kt")
        property("sonar.tests", "./module/test/src/test/kotlin")
    }
}
