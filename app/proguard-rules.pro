# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android_Studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-obfuscationdictionary proguard_dic.txt
-classobfuscationdictionary proguard_dic.txt
-packageobfuscationdictionary proguard_dic.txt

-dontnote **
-dontwarn **
-keepattributes *Annotation*
-keep class android.support.annotation.Keep
-keep class com.github.megatronking.stringfog.annotation.StringFogIgnore
-keep class com.saki.QVMProtect {*;}
-keep class com.saki.c.e {*;}
-keep class com.saki.d.n {*;}
-keep class com.setqq.free.Msg {*;}
-keep class com.setqq.free.sdk.IPlugin {*;}
-keep class com.setqq.free.sdk.PluginApiInterface {*;}
-keep class * extends com.setqq.script.sdk.ApiAdapter {*;}
-keep class org.keplerproject.luajava.** {*;}
-keep class com.eclipsesource.v8.** {*;}
-keep class com.mcsqnxa.** {<init>(...);}
-dontwarn com.file.zip.**
-dontwarn org.bouncycastle.**
-dontwarn javax.naming.**

-keep class androidx.annotation.Keep
-keepclassmembers class * {
    @android.support.annotation.Keep <methods>;
}
#保持使用了Keep注解的成员域以及类不被混淆
-keepclassmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclassmembers class * {
    @android.support.annotation.Keep <init>(...);
}



# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
