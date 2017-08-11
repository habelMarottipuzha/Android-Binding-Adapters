-renamesourcefileattribute SourceFile

-keepattributes *Annotation*
-dontwarn javax.annotation.**
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature

-keep class in.habel.models.** { *; }