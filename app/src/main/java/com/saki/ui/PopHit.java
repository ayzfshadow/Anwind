package com.saki.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import com.ayzf_shadow.Activity.AuthorityActivity;
import com.ayzf.anwind.R;

public class PopHit extends PopupWindow implements OnClickListener {
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
            ok.setEnabled(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri u = Uri.parse(url);
//            String scheme = u.getScheme();
//            if (scheme.equals("http")||scheme.equals("https")){
//                return super.shouldOverrideUrlLoading(view,url);
//            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, u);
                context.startActivity(intent);
                return true;
//            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri u = request.getUrl();
//            String scheme = u.getScheme();
//            if (scheme.equals("http")||scheme.equals("https")){
//                return super.shouldOverrideUrlLoading(view,request);
//            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, u);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
//            }
        }
    }
    private Button ok;
    private WebView mWebview;
    Context context;

    public PopHit(Context context, View p) {
        super(context);
        this.context = context;
        setContentView(LayoutInflater.from(context).inflate(R.layout.pop_hit,
                null));
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        ok = getContentView().findViewById(R.id.ok);
        mWebview = getContentView().findViewById(R.id.web1);
        mWebview.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebview.removeJavascriptInterface("accessibility");
        mWebview.removeJavascriptInterface("accessibilityTraversal");
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");// 避免中文乱码
        mWebview.requestFocusFromTouch();
        mWebview.setWebViewClient(new PopHit.WebC());
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setNeedInitialFocus(false);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadsImagesAutomatically(true);
        ok.setOnClickListener(this);
        ok.setEnabled(false);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.windowAnim);
        showAtLocation(p, Gravity.CENTER, 0, 50);
        int ver = -1;
        try {
            ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        mWebview.loadUrl(AuthorityActivity.server+"NoticeV9.php?&ver="+ver);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if (v == ok) {
            dismiss();
        }
    }
}
