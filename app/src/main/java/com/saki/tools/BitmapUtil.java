package com.saki.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
    private static void cacheFace(String account, byte[] data) {
        cacheImage("face/" + account + ".png", data);
    }

    private static void cacheImage(String path, byte[] data) {
        File cache = new File(FileUtil.CACHE_PATH + path);
        if (!cache.exists()) {
            cache.getParentFile().mkdirs();
            try {
                cache.createNewFile();
            } catch (IOException e) {
                return;
            }
        }
        try {
            OutputStream out = new FileOutputStream(cache);
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cacheLogo(String number, byte[] data) {
        cacheImage("logo/" + number + ".png", data);
    }

    public static void load(final String account, final int type,
                            final BitmapCallback call) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (type == 0) {
                    call.callBack(loadLogo(account, true));
                } else {
                    call.callBack(loadFace(account, true));
                }
            }
        }).start();
    }

    public static void loadBitmap(final String url, final BitmapCallback call, final Bitmap b) {
        if (call != null) {
            if (url == null) {
                call.callBack(b);
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap map = BitmapFactory.decodeStream(new URL(url).openStream());
                        if (map != null) {
                            call.callBack(map);
                            return;
                        }
                    } catch (IOException e) {
                    }
                    call.callBack(b);
                }
            }).start();
        }
    }

    private static Bitmap loadCacheFace(String account) {
        return loadCacheImage(FileUtil.CACHE_PATH + "face/" + account + ".png");
    }

    private static Bitmap loadCacheImage(String path) {
        File cache = new File(FileUtil.CACHE_PATH + path);
        if (cache.exists()) {
            try {
                return BitmapFactory.decodeStream(new FileInputStream(cache));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Bitmap loadCacheLogo(String number) {
        return loadCacheImage(FileUtil.CACHE_PATH + "logo/" + number + ".png");
    }

    private static Bitmap loadFace(String account) {
        String url = "http://q2.qlogo.cn/headimg_dl?dst_uin=" + account
                + "&spec=100";
        byte[] data = HttpUtil.getUrlContent(url);
        Bitmap bit = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bit != null) {
            cacheFace(account, data);
            return bit;
        }
        return null;
    }

    public static Bitmap loadFace(String account, boolean cache) {
        Bitmap bit = null;
        if (cache) {
            bit = loadCacheFace(account);
        }
        if (bit == null) {
            bit = loadFace(account);
        }
        return bit;
    }

    private static Bitmap loadLogo(String number) {
        String url = "http://p.qlogo.cn/gh/" + number + "/" + number + "/40?t="
                + System.currentTimeMillis();
        byte[] data = HttpUtil.getUrlContent(url);
        Bitmap bit = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bit != null) {
            cacheLogo(number, data);
            return bit;
        }
        return null;
    }

    public static Bitmap loadLogo(String number, boolean cache) {
        Bitmap bit = null;
        if (cache) {
            bit = loadCacheLogo(number);
        }
        if (bit == null) {
            bit = loadLogo(number);
        }
        return bit;
    }
}
