# Reglas ProGuard para FocusLens
# Retrofit / Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Domain models (no ofuscar)
-keep class com.focuslens.domain.model.** { *; }
-keep class com.focuslens.data.remote.dto.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# ML Kit
-keep class com.google.mlkit.** { *; }
