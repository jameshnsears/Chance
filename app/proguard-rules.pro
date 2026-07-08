# Add project specific ProGuard rules here.

# Preserve line numbers for better stack traces in Crashlytics
-keepattributes SourceFile,LineNumberTable

# Kotlin Reflection
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# Timber and Firebase bundle their own consumer rules, no manual keeps required.

# JUnit 5 extensions referenced by transitive dependencies (e.g. MockK) but not used in the project
-dontwarn org.junit.jupiter.**
