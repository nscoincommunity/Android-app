# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# org.jetbrains.kotlinx:kotlinx-coroutines-core
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# com.android.support:design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# com.squareup.retrofit2:retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-dontwarn okio.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# org.greenrobot:eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# com.github.PhilJay:MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# NewRelic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes InnerClasses

# Needed to resolve the following issues:
#
# Warning: com.stocksexchange.android.ui.orders.fragment.OrdersFragment$initAdapter$1$3$invokeSuspend$$inlined$get$1:
# can't find referenced class com.stocksexchange.android.ui.orders.fragment.OrdersFragment$initAdapter$1$3
# Warning: com.stocksexchange.android.ui.orders.fragment.OrdersFragment$initAdapter$1$3$invokeSuspend$$inlined$get$1:
# can't find referenced class com.stocksexchange.android.ui.orders.fragment.OrdersFragment$initAdapter$1$3
-dontwarn com.stocksexchange.android.ui.orders.fragment.*

# Needed to resolve the following crash:
#
# Fatal Exception: java.lang.RuntimeException
# Parcel android.os.Parcel@b2ba846: Unmarshalling unknown type code 1279544898 at offset 4152
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Intercom
-keep class io.intercom.android.** { *; }
-keep class com.intercom.** { *; }

-keep class com.stocksexchange.android.model { *; }
-keep class com.stocksexchange.android.api.model { *; }
-keep class com.stocksexchange.android.socket.model.** { *; }

# Android Lollipo crashes
# https://stackoverflow.com/questions/16474904/proguard-with-android-java-lang-nosuchmethoderror-android-util-xml-asattribute
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** { *; }

# SimpleXML
-dontwarn com.bea.xml.stream.**
-dontwarn org.simpleframework.xml.stream.**
-keep class org.simpleframework.xml.**{ *; }
-keepclassmembers,allowobfuscation class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <init>(...);
}

# To prevent the following crash:
# java.lang.ClassCastException: TwitterNewsResources cannot be cast to BlogNewsResources
-keep class com.stocksexchange.android.ui.news.** { *; }

# Deliberately keeping a language presenter because ProGuard just
# decides to delete it every time
-keep class com.stocksexchange.android.ui.language.LanguagePresenter