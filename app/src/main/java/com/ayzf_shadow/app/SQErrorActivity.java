package com.ayzf_shadow.app;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.io.*;

/**
 * @Author 暗影之风
 *
 * @Class 异常显示Activity
 *
 * @CreateTime 2020-08-04 19:02 周二
 */
public class SQErrorActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final String log = super.getIntent().getExtras().getString("log");
        final ScrollView scrollView = new ScrollView(this);
        final HorizontalScrollView horizontal = new HorizontalScrollView(this);
        final TextView textView = new TextView(this);
        scrollView.addView(horizontal);
        scrollView.setBackgroundColor(0xFFFFFF);
        scrollView.setVerticalScrollBarEnabled(false);
        horizontal.addView(textView);
        horizontal.setHorizontalScrollBarEnabled(false);
        textView.setText("此页面可以左右滑动\n\n很抱歉，当你看见这个页面，是程序出现了问题\n我们需要你提供这些信息来有助于我们下个版本修复这个问题\n提供这些信息可能包含您的个人信息，例如手机型号，安卓版本等\n提供的所有信息都在下面，点击可以复制，安卓10及以上用户请给予剪贴版权限，然后发给制作人。还可以长按，注意给存储权限，我们会把错误日志存到文件，您可以自由选择是否发给我们，下面就是报告内容\n\n"+readFile(log));
        textView.setTextColor(0x03A9F4);
        super.onCreate(savedInstanceState);
        super.setContentView(scrollView);
        textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    copyText(SQErrorActivity.this,"复制日志","点击复制以下内容会被复制到剪贴板，您可以自由选择给制作人，安卓10及以上用户请给予本软件剪贴板权限，否则不能复制，内容过长可以下滑\n\n"+readFile(log),readFile(log));
                }
            });
        textView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    /*AlertDialog dialog=new AlertDialog.Builder(ErrorActivity.this)
                        .setTitle("错误日志")
                        .setMessage("即将把错误日志从\n"+log+"\n导出到\n"+Version.lj+"cache/logs/\n点击确定即可执行，请确保已给予本软件读写手机存储权限")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dia,int which)
                            {
                                copyFile(new File(log),new File(Version.lj+"cache/logs/"+new File(log).getName()));
                                ShadowApp.toast(ErrorActivity.this,R.color.main,R.color.green,ErrorActivity.this.getWindow().getDecorView(),"执行成功",ShadowApp.toastShowTime);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .setCancelable(false)
                        .create();
                    dialog.show();*/
                    AlertDialog dialog=new AlertDialog.Builder(SQErrorActivity.this)
                        .setTitle("错误日志")
                        .setMessage("错误日志存储路径：\n"+log)
                        .setPositiveButton("确定",null)
                        .setCancelable(false)
                        .create();
                    dialog.show();
                    return true;
                }
            });
    }

    private static String readFile(String log)//读取文件
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int length;
            byte[] buffer = new byte[102400];
            final FileInputStream fis = new FileInputStream(new File(log));
            while ((length = fis.read(buffer)) > 0)
            {
                baos.write(buffer,0,length);
            }
            fis.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new String(baos.toString());
    }

    private static void copyText(final AppCompatActivity context,String title,String message,final String copy)//复制文本
    {
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("关闭",null)
            .setPositiveButton("复制",null).create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton
        (AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    dialog.dismiss();
                }
            }
        );
        dialog.getButton
        (AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label",copy);
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(context, "复制完成", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    /*private static void copyFile(File from,File to)
    {
        if (from!=null&&to!=null&&from.isFile()&&from.length()>0)
        {
            try
            {
                if (!to.exists())
                {
                    to.getParentFile().mkdirs();
                    to.createNewFile();
                }
                int length;
                byte[] buffer = new byte[1024*1024*5];
                final FileInputStream fis = new FileInputStream(from);
                final FileOutputStream fos = new FileOutputStream(to);
                while ((length=fis.read(buffer))>0)
                {
                    fos.write(buffer,0,length);
                }
                fis.close();
                fos.flush();
                fos.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }*/
}
