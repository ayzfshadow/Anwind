package com.saki.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.zip.ZipInputStream;

import com.saki.QVMProtect;
import com.saki.encode.Code;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;

import com.file.zip.ZipEntry;
import com.file.zip.ZipOutputStream;
import com.saki.log.SQLog;
@QVMProtect
public class FileUtil {
    public static final int SCRIPT_SELECT_CODE = 3026 ;
    public static  String BASE_PATH ;
    public static  String CACHE_PATH;
    public static  String SCRIPT_PATH ;
    public static  String SCRIPT_LIB_PATH ;
    public static final int FILE_SELECT_CODE = 3027 ;
    public static void initPath(Context c){
        System.out.println("----------------------InitPath-------------------------");
        BASE_PATH = c.getApplicationContext().getExternalFilesDir(null).getAbsolutePath()+"/";
        CACHE_PATH = c.getApplicationContext().getExternalCacheDir().getAbsolutePath()+"/";
        System.out.println("BASE_PATH:"+BASE_PATH);
        System.out.println("CACHE_PATH:"+ CACHE_PATH);
        SCRIPT_PATH = BASE_PATH + "Script/";
        SCRIPT_LIB_PATH = BASE_PATH + "ScriptLib/";
        System.out.println("----------------------InitPath-------------------------");
    }
    public static byte[] getFileMd5(File file)
    {
        byte[] bin = readFile(file);
        return Code.MD5(bin);
    }
    public static String getPath(Context context, Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static String loadEncodeFile(String path){
        return new String(loadEncodeFile(new File(path)));
    }
    public static byte[] loadEncodeFile(File file){
        if(file.exists()){
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = new FileInputStream(file);
                int len = -1;
                byte[] bin = new byte[0x10000];
                while ((len = in.read(bin)) != -1) {
                    out.write(bin, 0, len);
                }
                in.close();
                bin = out.toByteArray();
                byte[] key = new byte[16];
                System.arraycopy(bin,0,key,0,16);
                byte[] data = new byte[bin.length - 16];
                System.arraycopy(bin,16,data,0,data.length);
                return Code.QQTEAdecrypt(data,Code.MD5(key));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static final byte[] readFile(File file) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileInputStream read = new FileInputStream(file);
            int len = -1;
            byte[] bin = new byte[0x10000];
            while ((len = read.read(bin)) != -1)
                out.write(bin,0,len);
            read.close();
            return out.toByteArray();
        } catch (IOException e) {
            SQLog.e("文件读取失败:"+e);
        }
        return new byte[0];
    }

    public static boolean saveEndcodeFile(byte[] data,File path){
        byte[] key = new byte[16];
        new Random().nextBytes(key);
        byte[] bin = Code.QQTEAencrypt(data,Code.MD5(key));
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(bin.length + 16);
            out.write(key);
            out.write(bin);
            bin = out.toByteArray();
            out.close();
        }catch (IOException e){
            return false;
        }
        return writeFile(path, bin);
    }
    public static boolean saveEndcodeFile(File f,File topath) {
        if (f.exists()) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = new FileInputStream(f);
                int len = -1;
                byte[] bin = new byte[0x10000];
                while ((len = in.read(bin)) != -1) {
                    out.write(bin,0,len);
                }
                return saveEndcodeFile(out.toByteArray(),topath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void showFileChooser(Activity a,int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            a.startActivityForResult(Intent.createChooser(intent, "选择文件"), code);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(a, "请安装一个文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    public static final boolean writeFile(File file,byte[] data) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            SQLog.e("文件写入失败:"+e);
        }
        return false;
    }
    public static boolean unPackZip(byte[] data,String path){
        File dir = new File(path);
        if(dir.isDirectory())
            dir.mkdirs();
        else
            dir.getParentFile().mkdirs();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry entry = null;
        try {
            while ((entry = (ZipEntry) zin.getNextEntry()) != null) {
                dir = new File(path+entry.getName());
                if(entry.isDirectory()){
                    if(!dir.exists())
                        dir.mkdirs();
                }else{
                    if(!dir.exists())
                        dir.createNewFile();
                    FileOutputStream out = new FileOutputStream(dir);
                    byte[] bin = new byte[0x10000];
                    int len = -1;
                    while((len = zin.read(bin))!=-1)
                        out.write(bin,0,len);
                    out.flush();
                    out.close();
                }
            }
            zin.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static byte[] packZip(File[] files,String newDir)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        try {
            for (File file : files) {
                if (file == null || !file.exists())
                    continue;
                if (file.isDirectory()) {
                    recursionZip(zout, file, newDir+"/"+file.getName() + File.separator);
                } else {
                    recursionZip(zout, file, newDir + "/");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileSec : files) {
                if (fileSec == null) {
                    continue;
                }
                if (fileSec.isDirectory()) {
                    baseDir = file.getName() + File.separator + fileSec.getName() + File.separator;
                    recursionZip(zipOut, fileSec, baseDir);
                } else {
                    recursionZip(zipOut, fileSec, baseDir);
                }
            }
        } else {
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            zipOut.write(readFile(file));
        }
    }

    public static void deleteFile(File file) {
        if(file == null)
            return;
        if(file.isDirectory()){
            File[] fs = file.listFiles();
            if(fs != null){
                for(File f:fs)
                    deleteFile(f);
            }
        }
        file.delete();
    }
}
