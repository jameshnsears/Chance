# Shared ProGuard/R8 rules for the repo
# Place rules that should apply across modules and app here.

# --- General ---
-keepattributes *Annotation*
-keepclassmembers class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Keep classes and members annotated with @Keep
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-keep @androidx.annotation.Keep class *

# --- Jetpack Compose / AndroidX metadata ---
-keepclassmembers class androidx.compose.** { *; }
-keep class kotlin.jvm.internal.** { *; }
-dontwarn androidx.compose.**

# --- Coil (image loading) ---
-dontwarn coil.**
-keep class coil.** { *; }

# --- Firebase Crashlytics / Installations ---
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**
-keep class com.google.firebase.installations.** { *; }

# --- AndroidX Lifecycle (ViewModel / SavedState) ---
-dontwarn androidx.lifecycle.**
-keepclassmembers class * {
    @androidx.lifecycle.* *;
}

# --- Optional: serialization/reflection libs (enable if used) ---
# Gson
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.** { *; }
#-dontwarn com.google.gson.**

# Moshi / kotlinx.serialization
#-keep class com.squareup.moshi.** { *; }
#-keep class kotlinx.serialization.** { *; }
#-dontwarn kotlinx.serialization.**

# --- Notes ---
# - Add additional library-specific keep rules here when a library in the project
#   requires them (e.g., Room, Retrofit adapters, Dagger/Hilt generated code).
# - For module libraries that should force consumers to keep rules, prefer
#   placing consumer rules in the module's `consumer-rules.pro` file.
# - Consider enabling mapping file upload for Crashlytics in app build.gradle.
