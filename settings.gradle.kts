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
include(":module:ui-dialog-dice")
include(":module:ui-dialog-about")
include(":module:ui-dialog-colorpicker")
include(":module:ui-zoom")
include(":module:test")
include(":module:ui-tab-roll")
include(":module:ui-tab-bag")
