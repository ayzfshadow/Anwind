package com.saki.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.support.annotation.Keep;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ayzf.anwind.R;
import com.saki.ui.i.IDR;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class OtherWindow extends Dialog implements
View.OnClickListener ,DialogInterface.OnDismissListener
{

	private Context context;
	class WebC extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,SslError error) {
            handler.proceed();
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadUrl("file:///android_asset/404.html");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           System.out.println(url);
			return true;
//            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri u = request.getUrl();
			System.out.println();
			System.out.println(URLDecoder.decode(u.toString()));
			onVerifyCAPTCHA(URLDecoder.decode(u.toString()).replaceAll("^jsbridge://CAPTCHA/onVerifyCAPTCHA\\?p=","").replaceAll("#2$",""));
//            String scheme = u.getScheme();
//            if (scheme.equals("http")||scheme.equals("https")){
//                return super.shouldOverrideUrlLoading(view,request);
//            } else {
			
			return true;
//            }
        }
    }
    private WebView web;
    
    private IDR i;

	private String ticket;

	private int result;

    public OtherWindow(Context context, String url, IDR i) {
        super(context);
        this.i = i;
		this.context=context;
		System.out.println(url);
        setContentView(R.layout.lay_verifycode);
        web = (WebView) findViewById(R.id.img_verifycode);
		WebSettings mWebSettings = web.getSettings();
        //让webview支持js
        mWebSettings.setJavaScriptEnabled(true);
        //设置是否支持缩放模式
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(true);
        // 是否显示+ -
        mWebSettings.setDisplayZoomControls(false);
		web.addJavascriptInterface(this,"CAPTCHA");
		web.setWebViewClient(new WebC());
		
		
		
        web.loadUrl(url);
      
        
        setOnDismissListener(this);
        show();
    }

	@JavascriptInterface
	@Keep
	public void onVerifyCAPTCHA(String json){
		try
		{
			JSONObject g = new JSONObject(json);
			this.result=g.getInt("result");
			if(result==0){
				this.ticket=g.getString("ticket");
			}
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		this.dismiss();
	}
	
    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (i != null) {
			if(this.result==0){
            i.result(this.ticket);
			}
        }
    }
}
