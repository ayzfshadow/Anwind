package com.saki.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ayzf.anwind.R;
import com.ayzf_shadow.Activity.AuthorityActivity;
import com.saki.client.SQApplication;
import com.saki.encode.Canvart;
import com.saki.qq.global.Device;
import com.saki.qq.global.Global;
import com.saki.service.NewConnection;
import com.saki.service.NewService;
import com.saki.tools.BitmapUtil;
import com.saki.ui.i.IDR;
import com.saki.ui.i.IUI;
import com.saki.ui.method.NinePasswordTransformationMethod;
import com.saki.ui.view.RoundImageView;
import com.saki.ui.view.UpdateView;

public class NewLoginActivity extends Activity implements OnClickListener,OnLongClickListener, IUI
{

	private DrawerLayout main_drawer_layout;
	public static final int CMD_VERIFYLOCKCODE = 6;

	public static final int CMD_VERIFYLOCKFALSE = 7;
    public class JellyInterpolator extends LinearInterpolator
	{
        private float factor;

        public JellyInterpolator()
		{
            this.factor=0.15f;
        }

        @Override
        public float getInterpolation(float input)
		{
            return (float) (Math.pow(2,-10 * input)
				* Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }

    public static final int CMD_LOGIN_SUCCESS = 0;
    public static final int CMD_LOGIN_FAILT = 1;
    public static final int CMD_VERIFYCODE = 2;
    public static final int CMD_SET_HEAD = 3;
    public static final int CMD_OTHER = 4;
	public static final int CMD_VERIFYLOCK = 5;
    private LinearLayout layoutBox;
    private LinearLayout layoutAccount;
    private LinearLayout layoutPassword;
    private LinearLayout layoutHead;
    private Button login;
    private EditText account;
    private EditText password;
    private NewConnection connection;
    private RoundImageView head;
    private UpdateView updata;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg)
		{
            if (msg.what == CMD_LOGIN_FAILT)
			{
                recover();
                String error = msg.obj.toString();
                if (error.replace(" ","").equals(""))
				{
                    error="登录失败:请关闭QQ设备锁";
                }
                Toast.makeText(NewLoginActivity.this,error,
							   Toast.LENGTH_LONG).show();
            }
			else if (msg.what == CMD_SET_HEAD)
			{
                head.setImageBitmap((Bitmap) msg.obj);
            }
			else if (msg.what == CMD_VERIFYCODE)
			{
                byte[] bin = (byte[]) msg.obj;
                new OtherWindow(NewLoginActivity.this,
					new String(bin),
					new IDR() {
						@Override
						public void result(String value)
						{
							if (value == null || value.equals(""))
								recover();
							else
								connection.getService().verifyCode(value);
						}
					});
            } 
			else if (msg.what == 5)//设备锁
			{
				final byte[] bArr = (byte[]) msg.obj;
				final AlertDialog.Builder normalDialog = 
					new AlertDialog.Builder(NewLoginActivity.this);
				normalDialog.setTitle("需要验证登陆保护");
				normalDialog.setMessage("是否发送验证码到手机型号");
				normalDialog.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							connection.getService().send(bArr);
						}
					});
				normalDialog.setNegativeButton("关闭", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{



						}
					});
				// 显示
				normalDialog.show();
			}
			else if (msg.what == 6)//设备锁
			{

				final EditText edit = new EditText(NewLoginActivity.this);
				final AlertDialog.Builder normalDialog = 
					new AlertDialog.Builder(NewLoginActivity.this);
				normalDialog.setView(edit);
				normalDialog.setTitle("需要验证登录保护");
				if (msg.what == 6)
				{
					normalDialog.setMessage("输入密保手机收到的短信验证码");
				}

				normalDialog.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							if (edit.getText().toString().isEmpty())
							{
								//NewLoginActivity.this.resetInputStatus();
							}
							else
							{
								connection.getService().sendLockVerify(edit.getText().toString());
							}
						}
					});
				normalDialog.setNegativeButton("关闭", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							//NewLoginActivity.this.resetInputStatus();
						}
					});
				// 显示
				normalDialog.show();
			}
			else if (msg.what == 7)//设备锁验证失败
			{

				final EditText edit = new EditText(NewLoginActivity.this);
				final AlertDialog.Builder normalDialog = 
					new AlertDialog.Builder(NewLoginActivity.this);
				normalDialog.setView(edit);
				normalDialog.setTitle("验证码错误");
				if (msg.what == 7)
				{
					normalDialog.setMessage("重新输入密保手机收到的短信验证码");
				}

				normalDialog.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							if (edit.getText().toString().isEmpty())
							{
								//NewLoginActivity.this.resetInputStatus();
							}
							else
							{
								connection.getService().sendLockVerify(edit.getText().toString());
							}
						}
					});
				normalDialog.setNegativeButton("关闭", 
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which)
						{
							//NewLoginActivity.this.resetInputStatus();
						}
					});
				// 显示
				normalDialog.show();
			}
			else if (msg.what == CMD_LOGIN_SUCCESS)
			{
                startActivity(new Intent(NewLoginActivity.this,
										 PagerActivity.class));
                NewLoginActivity.this.finish();
            }
			else
			{
                Toast.makeText(NewLoginActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
            }
        }

        ;
    };
    private ObjectAnimator anim;
    private long mExitTime;
    private PopHit hit;
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
	{
        if (keyCode == KeyEvent.KEYCODE_BACK)
		{
            if ((System.currentTimeMillis() - mExitTime) > 2000)
			{
                Toast t = Toast.makeText(this,"再按一次，退出程序",Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
                mExitTime=System.currentTimeMillis();
            }
			else
			{
                stopService(new Intent(this,NewService.class));
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    private void getHead(final String qq)
	{
        new Thread() {
            @Override
            public void run()
			{
                Global.qq=Long.parseLong(qq);
                Bitmap b = BitmapUtil.loadFace(qq,false);
                updateUI(CMD_SET_HEAD,b);
            }
        }.start();
    }

    private void headAnim()
	{
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
																	 0.5f,1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
																	  0.5f,1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(
			layoutHead,animator,animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
    @Override
    public void onClick(View v)
	{
        String id = account.getText().toString();
        String psd = password.getText().toString();
        if (id == null || psd == null || id.equals("") || psd.equals(""))
		{
            Toast.makeText(this,"请输入账号密码",Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        boolean connection2 = false;
        for (int i = 0; i < networkInfo.length; i++)
        {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
            {
                connection2=true;
                break;
            }
        }
        if (!connection2)
        {
            Toast.makeText(this,"无网络",Toast.LENGTH_SHORT).show();
            return;
        }
        else
		{
            saveAccount(id,psd);
            getHead(id);
            connection.getService().login(id,psd,updata.getCode());
            anim=ObjectAnimator.ofFloat(layoutBox,"scaleX",1.0f,
                                        0.5f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(500);
            anim.addListener(new LoginAnimatorListener());
            anim.start();
        }
    }

	@Override
	public boolean onLongClick(View p1)
	{
		//startActivity(new Intent(NewLoginActivity.this,
		//	 PagerActivity.class));

		return true;
	}


    private class LoginAnimatorListener implements AnimatorListener
	{

        private boolean isCancel = false;
        @Override
        public void onAnimationCancel(Animator animation)
		{
            if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
            {
                System.exit(-1);
            }
            isCancel=true;
            layoutAccount.setVisibility(View.VISIBLE);
            layoutPassword.setVisibility(View.VISIBLE);
            login.setEnabled(true);
            login.setText("登录");
            layoutBox.setScaleX(1.0f);
        }

        @Override
        public void onAnimationEnd(Animator animation)
		{
            if (!isCancel)
			{
                layoutHead.setVisibility(View.VISIBLE);
                layoutBox.setVisibility(View.INVISIBLE);
                headAnim();
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation)
		{
        }

        @Override
        public void onAnimationStart(Animator animation)
		{
            if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
            {
                System.exit(-1);
            }
            login.setEnabled(false);
            login.setText("登录中...");
            layoutAccount.setVisibility(View.INVISIBLE);
            layoutPassword.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        SQApplication.activity=this;
        if (com.ayzf_shadow.tool.jiance.vpn.֏֏֏֏֏֏() == false || com.ayzf_shadow.tool.jiance.xposed.֏֏֏֏֏֏() == false)
        {
            System.exit(-1);
        }
        layoutBox=findViewById(R.id.input_layout);
        layoutAccount=findViewById(R.id.input_layout_name);
        layoutPassword=findViewById(R.id.input_layout_psw);
        layoutHead=findViewById(R.id.layout_progress);
        login=findViewById(R.id.main_btn_login);
        account=findViewById(R.id.input_account);
        password=findViewById(R.id.input_password);
        head=findViewById(R.id.head);
        updata=findViewById(R.id.updateView1);
		//main_drawer_layout = findViewById(R.id.main_drawer_layout);

        login.setOnClickListener(this);
		login.setOnLongClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
            int REQUEST_CODE_CONTACT = 302;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions)
			{
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED)
				{
                    //申请权限
                    this.requestPermissions(permissions,REQUEST_CODE_CONTACT);
                }
            }
        }
        checkNetWord();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);


		Device.display=sp.getString("edittext_display","DAMIMI.2018.003 test-key");
		Device.product=sp.getString("edittext_product","8848Μ6");
		Device.device=sp.getString("edittext_device","8848Μ6");
		Device.board=sp.getString("edittext_board","ocean");
		Device.brand=sp.getString("edittext_brand","8848");
		Device.model=sp.getString("edittext_model","8848 Μ6");
		Device.bootloader=sp.getString("edittext_bootloader","unknown");
		Device.fingerprint=sp.getString("edittext_fingerprint","8848/Μ6/Μ6/DAMIMI.2018.003/547250:Device/release-keys");
		Device.bootId=sp.getString("edittext_bootId","20fe4dfe-54e1-8a1e-a445-061ea2ef662a");
		Device.procVersion=sp.getString("edittext_procVersion","Linux version 4.14.117-UnrealEngine-fe40de4a122e0 (mahuateng@ubuntu) (Android (6031699 based on r253063e4) clang version 9.0.3 (https://android.googlesource.com/toolchain/clang 745b335211bb9eadfa6aa6301f84715cee4b37c5) (https://android.googlesource.com/toolchain/llvm 31c3f8c4ae6cc980405a3b90e7e88db00249eba5) (based on LLVM 9.0.3svn)) #1 Thu Dec 02:09:57 CST 2018");
		Device.baseBand=sp.getString("edittext_baseBand","MPSS.HE.1.0.c11.1-00007-SM8150_GEN_PACK-2.233059.1.235876.1");
		Device.simInfo=sp.getString("edittext_simInfo","cmcc");
		Device.osType=sp.getString("edittext_osType","android");
		Device.macAddress=sp.getString("edittext_macAddress","02:00:00:00:00:00");
		Device.wifiBSSID=sp.getString("edittext_wifiBSSID","02:00:00:00:00:00");
		Device.wifiSSID=sp.getString("edittext_wifiSSID","<unknown ssid>");
		Device.imsiMd5=Canvart.Hex2Byte(sp.getString("edittext_imsiMd5","2ef1a7b23265dacb272ef1a7b23265da"));
		Device.imei=sp.getString("edittext_imei","868209161547921");
		Device.apn=sp.getString("edittext_apn","wifi");
		Device.androidId=sp.getString("edittext_androidId","DAMIMI.2018.003");
		Device.codename=sp.getString("edittext_codename","Raphaere");
		Device.incremental=sp.getString("edittext_incremental","super.angelways.20190426.145685");
		Device.innerVersion=sp.getString("edittext_innerVersion","DaJiJi");
		Device.osVersion=sp.getString("edittext_osVersion","10.0.1");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
	{
        switch (requestCode)
		{
            case 302: {
					if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
					{
						if (connection != null)
						{
							stopService(new Intent(this,NewService.class));
						}
						this.finish();
					}
					return;
				}
        }

    }

    private void checkNetWord()
	{
//        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
//            .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
//        boolean connection = false;
//        for (int i = 0; i < networkInfo.length; i++)
//        {
//            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
//            {
//                connection=true;
//                break;
//            }
//        }
//        if (!connection)
//        {
//            Toast.makeText(this,"无网络",Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
        updata.checkUpdate(AuthorityActivity.server + "UpdateV9.php");
        startService(new Intent(this,NewService.class));
        bindService(new Intent(this,NewService.class),this.connection = new NewConnection(this),Context.BIND_AUTO_CREATE);
//        }
        initAccount();
        //startActivity(new Intent(this, PagerActivity.class));
    }
    public void authority(View view)
    {
        startActivity(new Intent(NewLoginActivity.this,com.ayzf_shadow.Activity.AuthorityActivity.class));
    }
    private String getAccount()
	{
        return getSharedPreferences("login",Context.MODE_PRIVATE).getString(
			"account","");
    }

    private String getPassWord()
	{
        return getSharedPreferences("login",Context.MODE_PRIVATE).getString(
			"passWord","");
    }
    private void initAccount()
	{
		account.setText(getAccount());
		password.setText(getPassWord());
		password.setTransformationMethod(new NinePasswordTransformationMethod());
    }
    private void saveAccount(String account,String passWord)
	{
        SharedPreferences sp = getSharedPreferences("login",
													Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("account",account);
        e.putString("passWord",passWord);
        e.commit();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
	{
        if (hasFocus && hit == null)
		{
            hit=new PopHit(this,getWindow().getDecorView());
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy()
	{
        if (connection != null)
            unbindService(connection);
        super.onDestroy();
    }

    private void recover()
	{
        layoutHead.setVisibility(View.GONE);
        layoutBox.setVisibility(View.VISIBLE);
        if (anim != null && anim.isRunning())
		{
            anim.cancel();
        }
		else
		{
            layoutAccount.setVisibility(View.VISIBLE);
            layoutPassword.setVisibility(View.VISIBLE);
            login.setEnabled(true);
            login.setText("登录");
            layoutBox.setScaleX(1.0f);
        }
    }

    @Override
    public void updateUI(int cmd,Object value)
	{
        Message msg = Message.obtain();
        msg.what=cmd;
        msg.obj=value;
        handler.sendMessage(msg);
    }
}
