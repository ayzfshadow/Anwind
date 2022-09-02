package com.saki.tools;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

public class InstallUtil {
    public static void installAPK(Context c, File mUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(c,"com.saki.sqv9.fileprovider", mUrl);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!c.getPackageManager().canRequestPackageInstalls()) {
                    startInstallPermissionSettingActivity(c);
                    return;
                }
            }
        }else{
            intent.setDataAndType(Uri.fromFile(mUrl), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (c.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            c.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context mContext) {
        //注意这个是8.0新API
        Uri packageUri = Uri.parse("package:"+ mContext.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageUri);
        mContext.startActivity(intent);
    }
}
