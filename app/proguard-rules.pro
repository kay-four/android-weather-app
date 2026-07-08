# Add project specific ProGuard rules here.
# Keep kotlinx.serialization models
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclasseswithmembers class com.example.weatherapp.data.remote.dto.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.weatherapp.data.remote.dto.** {
    kotlinx.serialization.KSerializer serializer(...);
}
