package com.grechur.imdemo.auth;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.grechur.imdemo.R;

public class CrawlerTaobaoActivity extends AppCompatActivity {

    private final int timeoutMoren = 180000; // 设置TimeOut 时间
    private int timeout = 1000;
    private WebView webView;

    //    private String mOrderList = "http://h5.m.taobao.com/mlapp/olist.html"; //订单列表
    private String mOrderListYS = "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm"; //订单列表
    private boolean isFristLogin = false;
    private int mCountPage = 0; // 订单页面加载到100即停止加载，然后请求
    private int mMaxCountPage = 10; // 抓取20页之后的数据就不再抓取了

    private String[] allUrl = {
            "https://plogin.m.jd.com/user/login.action","https://login.m.taobao.com/login.htm",
            "https://passport.yhd.com/m/login_input.do","https://login.m.gome.com.cn/login.html",
            "https://msinode.suning.com/m/home.do","https://www.amazon.cn/ap/signin?_encoding=UTF8&openid.assoc_handle=cnflex&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.ns.pape=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Fpape%2F1.0&openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.cn%2Fgp%2Fyourstore%2Fcard%3Fie%3DUTF8%26ref_%3Dcust_rec_intestitial_signin",
    };

    private String jd_hideOthers_android = "(function() {document.getElementById('header').style.display = 'none' ;document.getElementsByClassName('J_ping findpwd')[0].style.display = 'none'; document.getElementsByClassName('login-type')[0].style.display = 'none';document.getElementsByClassName('txt-quickReg')[0].style.display = 'none';document.getElementsByClassName('icon icon-clear')[0].style.display = 'none';document.getElementsByClassName('label-checkbox J_ping')[0].style.display = 'none';})()";

    private String tb_hideOthers_android = "(function() {document.getElementsByClassName('f-right')[0].style.display = 'none';document.getElementsByClassName('f-left')[0].style.display = 'none';document.getElementsByClassName('am-button btn-change')[0].style.display = 'none';})()";

    private String yhd_hideOthers_android = "(function() {document.getElementById('div_global_app').style.display = 'none';document.getElementsByClassName('touchweb-com_header')[0].style.display = 'none';document.getElementsByClassName('joint_login')[0].style.display = 'none';document.getElementsByClassName('remember_login')[0].style.display = 'none';document.getElementsByClassName('sms_login')[0].style.display = 'none';})()";

    private String gm_hideOthers_android = "(function() {document.getElementsByClassName('nav_return nav_ico nav_back')[0].style.display = 'none';document.getElementsByClassName('other')[0].style.display = 'none';document.getElementsByClassName('other-login')[0].style.display = 'none';document.getElementsByClassName('qk-login')[0].style.display = 'none';document.getElementsByClassName('login-style')[0].style.display = 'none';})()";

    private String su_hideOthers_android = "(function() {document.getElementsByClassName('sn-nav')[0].style.display = 'none';document.getElementsByClassName('login-guide')[0].style.display = 'none';document.getElementsByClassName('login-others')[0].style.display = 'none';document.getElementsByClassName('sign-btn')[0].style.display = 'none';})()";

    private String amz_hideOthers_android = "(function() {document.getElementById('auth-create-account-link').style.display = 'none';document.getElementsByClassName('a-column a-span7 a-text-right a-span-last')[0].style.display = 'none';document.getElementsByClassName('a-row a-spacing-top-medium')[0].style.display = 'none';;document.getElementsByClassName('a-button a-button-span12 a-button-base auth-wechat-login-button')[0].style.display = 'none';;document.getElementsByClassName('a-section auth-third-party-content')[0].style.display = 'none';;document.getElementsByClassName('a-row a-spacing-top-medium a-size-small')[0].style.display = 'none';;document.getElementsByClassName('a-section a-spacing-small a-text-center a-size-mini')[0].style.display = 'none';;document.getElementsByClassName('a-size-mini a-color-secondary')[0].style.display = 'none';})()";

    private String[] allJS = {jd_hideOthers_android,tb_hideOthers_android,yhd_hideOthers_android,gm_hideOthers_android,su_hideOthers_android,amz_hideOthers_android};
    Map<String,String> mUrlMap = new HashMap();

    private int position = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler_taobao);
        webView = (WebView) findViewById(R.id.webView);
        position = getIntent().getIntExtra("position",0);
        for (int i = 0; i < allUrl.length; i++) {
            mUrlMap.put(allUrl[i],allJS[i]);
        }
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getSettings().setSafeBrowsingEnabled(false);
        }
        webView.getSettings().setDomStorageEnabled(true);//对H5支持
//        webView.addJavascriptInterface(new OnJsHtml(), "local_obj");

        String mUsetAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2783.5 Safari/537.36";
        webView.getSettings().setUserAgentString(mUsetAgent);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final Uri uri = Uri.parse(url);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("url----->> ", url);
                if (isFristLogin) {

                    onLoginSuccess(view, url);
                    return;
                }
                if (url.startsWith(allUrl[position])) {
                    isFristLogin = false;
                    System.out.println(mUrlMap.get(allUrl[position]));
                    if(url.startsWith(allUrl[0])||url.startsWith(allUrl[4])||url.startsWith(allUrl[5])){
                        mHandler.sendEmptyMessageDelayed(4, 1000);
                    }else{
                        webView.loadUrl("javascript:"+mUrlMap.get(allUrl[position]));
                    }
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.setVisibility(View.VISIBLE);
                        }
                    },2000);
                } else {
                    if (url.startsWith("http://home.m.jd.com/")||url.startsWith("http://h5.m.taobao.com/mlapp/mytaobao.html") || url.startsWith("https://h5.m.taobao.com/mlapp/mytaobao.html")) {
                        CookieManager cookieManager = CookieManager.getInstance();
                        String CookieStr = cookieManager.getCookie(url);
                        Log.e("CookieStr", CookieStr);
                        Log.e("url", url);
                        if (!isFristLogin) {
                            isFristLogin = true;
                        }
                    }
                }

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @Override
            public void onLoadResource(WebView view, String url) {
            }
        });
        webView.loadUrl(allUrl[position]);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 4){
                webView.loadUrl("javascript:"+mUrlMap.get(allUrl[position]));
            }
        }
    };
    public void onLoginSuccess(WebView view, String url) {

    }


    Handler mHandlerTimeOut = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            String orgurl=webView.getUrl();
            finish();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRunnable != null && mHandlerTimeOut != null) {
            mHandlerTimeOut.removeCallbacks(mRunnable);
        }
        if(webView != null) {
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFristLogin) {
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }





}
