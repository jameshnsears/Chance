pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Chance"
include(":app")
include(":module:data")
include(":module:common")
include(":module:ui-dialog-bag")
include(":module:test")
include(":module:ui")
