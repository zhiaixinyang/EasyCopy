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
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

-keep class * implements java.io.Serializable {
    *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepattributes *Annotation*
-keepclassmembers class * implements android.os.Parcelable {
public static final ** CREATOR;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class rx.**{*;}
-keep class okhttps.**{*;}
-keep class okio.**{*;}
-keep class retrofit2.**{*;}
-keep class com.hwangjr.rxbus.**{*;}
-keep class android.support.** {*;}
-keep class com.google.** {*;}
-keep class com.google.gson.**
-keep interface com.google.gson.**

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class com.baidu.ocr.sdk.**{*;}
-dontwarn com.baidu.ocr.**