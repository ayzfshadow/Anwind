#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <stdio.h>

extern "C"
 {
   /**
	* 杀死进程;既关闭虚拟机
	*/
   void killProcess(JNIEnv* env)
	{
	  //绑定Java的System类
	  jclass system = env->FindClass("java/lang/System");
	  //绑定System类的exit方法
	  jmethodID exit = env->GetStaticMethodID(system,"exit","(I)V");
	  //调用System类的exit方法,相当于System.exit(0);
	  env->CallStaticVoidMethod(system,exit,0);
	}
	
   /**
	* 此项目签名(AIDE默认打包签名),用于盗版检测
	*/
    char* getSign()
	 {
	  return "30820293308201fca0030201020204323fafdb300d06092a864886f70d010105050030818c310b300906035504061302434e311830160603550408130f4c69616f204e696e67205368656e6731123010060355040713095368656e2059616e67311e301c060355040a0c15e5bdb1e6ae87e7b3bbe58897e5b7a5e4bd9ce5aea431183016060355040b0c0fe5bdb1e6ae87e5bc80e58f91e7bb843115301306035504030c0ce69a97e5bdb1e4b98be9a38e3020170d3231313131313134343235355a180f32313231313031383134343235355a30818c310b300906035504061302434e311830160603550408130f4c69616f204e696e67205368656e6731123010060355040713095368656e2059616e67311e301c060355040a0c15e5bdb1e6ae87e7b3bbe58897e5b7a5e4bd9ce5aea431183016060355040b0c0fe5bdb1e6ae87e5bc80e58f91e7bb843115301306035504030c0ce69a97e5bdb1e4b98be9a38e30819f300d06092a864886f70d010101050003818d003081890281810093a18b8471832737b08cd70a0e0a03c89d10bf5737abe22b042ebed082d53616bc1ce53bca3a98d477b37d4b66142b40faf9b4fb43245ae0888c680c8737af10b4f6eddecb1333f6a3f82be03e65396c9d8c2990b33cc89c7e144ec1a3fbe97c1968cc9a55271960d739ee33f3380db232d9cab79e5a9d94d8f776c8771fa3fd0203010001300d06092a864886f70d01010505000381810046335ea5ee8754d0a1b4b04597d5692a640279c415f641390d4189d57e734fdbb1586974c617d7a5dfcaa8496a8b6c4e45805025f536174907e61b64b1b434ed98240e9b7a9545cfc1b750ce5624c1ecc6cf53d038bf9e4bea3217287eeab40b7e9d63e2c467a332f60f79a7200a35dabaaaa9ae322a6df9ce0cacf01e460e0a";
	 }
	
    /**
	 * 盗版检测
	 */
	 void piracyDetection(JNIEnv* env,jobject activity)
	  {
	   //获取类,既MainActivity
	   jclass mainActivity = env->GetObjectClass(activity);
	   //获取包名管理器,既Android this.context.getPackageManager ( );
       jobject packageManager = env->CallObjectMethod(activity,env->GetMethodID(mainActivity,"getPackageManager","()Landroid/content/pm/PackageManager;"));
	   //获取包名信息,既Android packageManager.getPackageInfo(this.getPackageName(),0);
	   jobject packageInfo = env->CallObjectMethod(packageManager,env->GetMethodID(env->GetObjectClass(packageManager),"getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;"),env->CallObjectMethod(activity,env->GetMethodID(mainActivity,"getPackageName","()Ljava/lang/String;")),64);
	   //获取ApplicationInfo对象,既Android packageInfo.applicationInfo
	   jobject applicationInfo = env->GetObjectField(packageInfo,env->GetFieldID(env->GetObjectClass(packageInfo),"applicationInfo","Landroid/content/pm/ApplicationInfo;"));
       //获取App名称,既Android packageInfo.applicationInfo.loadLabel(packageManager).toString();
	   const char* appName = env->GetStringUTFChars((jstring)env->CallObjectMethod(applicationInfo,env->GetMethodID(env->GetObjectClass(applicationInfo),"loadLabel","(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;"),packageManager),JNI_FALSE);
	   //获取App包名,既Android packageInfo.packageName;
	   const char* appPackageName = env->GetStringUTFChars((jstring)env->GetObjectField(packageInfo,env->GetFieldID(env->GetObjectClass(packageInfo),"packageName","Ljava/lang/String;")),JNI_FALSE);
	   //获取App签名对象,既Android packageInfo.signatures [ 0 ];
	   jobject appSign_object = env->GetObjectArrayElement((jobjectArray)env->GetObjectField(packageInfo,env->GetFieldID(env->GetObjectClass(packageInfo),"signatures","[Landroid/content/pm/Signature;")),0);
	   //获取App签名,既Android packageInfo.signatures [ 0 ].toCharsString ( );
	   const char* appSign = env->GetStringUTFChars((jstring)env->CallObjectMethod(appSign_object,env->GetMethodID(env->GetObjectClass(appSign_object),"toCharsString","()Ljava/lang/String;")),JNI_FALSE);
	   //获取App版本名,既Android packageInfo.versionName;
	   const char* appVersionName = env->GetStringUTFChars((jstring)env->GetObjectField(packageInfo,env->GetFieldID(env->GetObjectClass(packageInfo),"versionName","Ljava/lang/String;")),JNI_FALSE);
	 
	   //App名称检测
	   if(strcmp(appName,"Anwind")!=0)
	    {
		  killProcess(env);
	    }
	  
	   //App包名检测
	   if(strcmp(appPackageName,"com.ayzf.anwind")!=0)
	    {
		  killProcess(env);
	    }
	  
	   //App签名检测
	   if(strcmp(appSign,getSign())!=0)
	    {
		  //killProcess(env);
	    }
	   
	   //App版本名检测
	   if(strcmp(appVersionName,"2.2")!=0)
	    {
		  killProcess(env);
	    }
	  }
	  
   /**
	* 伪盗版检测
	*/
	JNIEXPORT void JNICALL Java_com_saki_client_SQApplication_toPiracyDetection (JNIEnv* env, jclass object, jobject activity)
	 {
	   piracyDetection(env,activity);
	 }


 }
