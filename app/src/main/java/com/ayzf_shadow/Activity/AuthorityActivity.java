package com.ayzf_shadow.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.ayzf.anwind.R;
import com.ayzf_shadow.tool.Tea;
import com.saki.client.SQApplication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import org.json.JSONObject;

public class AuthorityActivity extends Activity
{
    public static final String server = "http://www.ayzfshadow.com/Anwind/";
    public static final String server2 = "http://www.ayzfshadow.com/Anwind/authorize.php?";
    public static final String teaKey = "04215866666652000013145229932901";
    private EditText cxqqet;
    private EditText cxkeyet;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.authority_activity_app);
        if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
        {
            System.exit(-1);
        }
        SQApplication.activity=this;
        cxqqet=(EditText)findViewById(R.id.cxqqet);
        cxkeyet=(EditText)findViewById(R.id.cxkeyet);
    }

    public void cxqqbt(View view)
    {
        final String a = cxqqet.getText().toString();
        if (a == null || a.equals(""))
        {
            Toast.makeText(getApplication(),"要查询的QQ号为空",Toast.LENGTH_SHORT).show();
        }
        else
        {
            new Thread
            (new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final String status=getUrlContent(server2 + "type=0");
                        AuthorityActivity.super.runOnUiThread
                        (new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (status == null || status.equals(""))
                                    {
                                        Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        new Thread
                                        (new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    final String content=Tea.decrypt(teaKey,getUrlContent(server2 + "id="+Tea.encrypt(teaKey,appMd5(AuthorityActivity.this))+"&type=1&qq=" + Tea.encrypt(teaKey,a)));
                                                    AuthorityActivity.super.runOnUiThread
                                                    (new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                if (content == null || content.equals(""))
                                                                {
                                                                    Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else if (content.equals("9995"))
                                                                {
                                                                    Toast.makeText(getApplication(), "输入QQ号为空", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else
                                                                {
                                                                    generalDialog(AuthorityActivity.this,"查询授权",content);
                                                                }
                                                            }
                                                        }
                                                    );
                                                    return;
                                                }
                                            }
                                        ).start();
                                    }
                                }
                            }
                        );
                        return;
                    }
                }).start();
        }
    }
    public void cxkeybt(View view)
    {
        final String a = cxkeyet.getText().toString();
        if (a == null || a.equals(""))
        {
            Toast.makeText(getApplication(),"要查询的卡密为空",Toast.LENGTH_SHORT).show();
        }
        else
        {
            new Thread
            (new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final String status=getUrlContent(server2 + "type=0");
                        AuthorityActivity.super.runOnUiThread
                        (new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (status == null || status.equals(""))
                                    {
                                        Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        new Thread
                                        (new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    final String content=Tea.decrypt(teaKey,getUrlContent(server2 + "id="+Tea.encrypt(teaKey,appMd5(AuthorityActivity.this))+"&type=2&key=" + Tea.encrypt(teaKey,a)));
                                                    AuthorityActivity.super.runOnUiThread
                                                    (new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                if (content == null || content.equals(""))
                                                                {
                                                                    Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else if (content.equals("9995"))
                                                                {
                                                                    Toast.makeText(getApplication(), "输入卡密为空", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else if (content.equals("5699"))
                                                                {
                                                                    Toast.makeText(getApplication(), "卡密不存在", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else
                                                                {
                                                                    try
                                                                    {
                                                                        JSONObject json = new JSONObject(content);
                                                                        String code = json.get("code").toString();
                                                                        if (code.matches("5644"))
                                                                        {
                                                                            Toast.makeText(getApplication(), "卡密已被使用，已增加授权时长：" + json.get("time").toString()+"\n卡密类型：" + json.get("type").toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (code.matches("5692"))
                                                                        {
                                                                            Toast.makeText(getApplication(), "卡密正常，可增加授权时长：" + json.get("time").toString()+"\n卡密类型：" + json.get("type").toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(getApplication(), "未知错误", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    catch (Exception e)
                                                                    {
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    );
                                                    return;
                                                }
                                            }
                                        ).start();
                                    }
                                }
                            }
                        );
                        return;
                    }
                }).start();
        }
    }
    public static final String appMd5(Context a)
    {
        return getFileMd5(a.getPackageResourcePath());
    }
    public static String getFileMd5(String path)
    {
        try
        {
            // 获取一个文件的特征信息，签名信息。
            File file = new File(path);
            // md5
            MessageDigest digest = MessageDigest.getInstance("md5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1)
            {
                digest.update(buffer,0,len);
            }
            byte[] result = digest.digest();
            StringBuffer sb  = new StringBuffer();
            for (byte b : result)
            {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                // System.out.println(str);
                if (str.length() == 1)
                {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static String getUrlContent(String url)
    {
        if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
        {
            return "";
        }
        int loss = 0;
        String data = null;
        while (data == null)
        {
            if (loss > 6)
            {
                break;
            }
            data=getinternet(url);
        }
        return data;
    }
    public static String getinternet(String url)
    {
        if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
        {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        try
        {
            //获取URLConnection对象
            HttpURLConnection connection=(HttpURLConnection)new URL(url).openConnection();
            //设置请求头
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //设置连接超时(3秒)
            connection.setConnectTimeout(3000);
            //设置请求类型
            connection.setRequestMethod("GET");
            //建立实际的连接
            connection.connect();
            String line;
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static void generalDialog(Context context,String title,String message)//普通对话框
    {
        AlertDialog dialog=new AlertDialog.Builder(context)//新建一个对话框对象
            .setTitle(title)//标题
            .setMessage(message)//内容
            .create();//创建对话框
        dialog.show();//显示对话框
    }
}
