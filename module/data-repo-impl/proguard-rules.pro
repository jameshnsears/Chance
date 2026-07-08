# Surgical keep for Protobuf generated classes used with JsonFormat reflection
-keep class com.github.jameshnsears.chance.data.domain.proto.** { *; }
-keep interface com.github.jameshnsears.chance.data.domain.proto.** { *; }

# Keep all members of the generated classes to ensure reflection (JsonFormat) works
-keepclassmembers class com.github.jameshnsears.chance.data.domain.proto.** {
    <fields>;
    <methods>;
}

# Keep the Builders as they are used for parsing and merging.
-keep class com.github.jameshnsears.chance.data.domain.proto.**$Builder { *; }
-keepclassmembers class com.github.jameshnsears.chance.data.domain.proto.**$Builder {
    public *;
}

# Keep the Protobuf runtime, which is heavily used by JsonFormat and DataStore.
-keep class com.google.protobuf.** { *; }
-keepclassmembers class com.google.protobuf.** {
    public *;
    static *;
}

# Keep the repository implementations and serializers to ensure DataStore can access them
-keep class com.github.jameshnsears.chance.data.repo.impl.** { *; }

# Attributes required for reflection-based tools like JsonFormat.
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses, LineNumberTable
