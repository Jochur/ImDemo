package com.grechur.imdemo.auth;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ContentLoadingProgressBar;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import com.grechur.imdemo.R;
import com.grechur.imdemo.utils.Preferences;

public class CrawlerTaobaoActivity extends AppCompatActivity {

    private WebView webView;
    private ContentLoadingProgressBar progress;

//    private boolean isFristLogin = false;

//    private String[] cookiesUrls = {
//            "http://h5.m.taobao.com/mlapp/mytaobao.html","https://m.jd.com/",
//            "https://www.amazon.cn/ap/signin","http://msinode.suning.com/m/home.do",
//            "https://u.m.gome.com.cn/my_gome.html","http://www.yhd.com/"
//    };
//    private String[] cookiesUrls = {
//            "https://market.m.taobao.com/app/mtb/evaluation-list/pages/index","https://home.jd.com",
//            "https://www.amazon.cn/gp/yourstore","https://msinode.suning.com/m/home.do",
//            "https://u.m.gome.com.cn/my_gome.html","http://home.m.yhd.com/h5index/index.do"
//    };

    private String[] cookiesUrls = {
            "https://market.m.taobao.com/app/tmall-wireless/sign-center/pages/index_v2","https://home.jd.com",
            "https://www.amazon.cn/gp/yourstore","https://msinode.suning.com/m/home.do",
            "https://u.m.gome.com.cn/my_gome.html","http://home.m.yhd.com/h5index/index.do"
    };

    private String[] loginUrls = {
            "https://login.m.taobao.com/login.htm","https://passport.jd.com/new/login.aspx",
            "https://www.amazon.cn/ap/signin",
            "https://passport.suning.com/ids/login",
            "https://login.m.gome.com.cn/login.html","https://passport.yhd.com/m/login_input.do"
            };

    private String[] cartUrls = {
            "https://h5.m.taobao.com/mlapp/cart.html","https://p.m.jd.com/cart/cart.action",
            "https://www.amazon.cn/gp/aw/c","https://shopping.suning.com/project/cart/cart1.html",
            "https://cart.m.gome.com.cn/shopping_cart.html","https://cart.m.yhd.com/cart/showCart.do"
    };

    private String jd_hideOthers_android = "(function() {function jdnonedomeFn() {  let loginBanner = document.getElementsByClassName('login-banner')[0];  let tipsWrapper = document.getElementsByClassName('tips-wrapper');  let loginForm = document.getElementsByClassName('login-form')[0];  let w = document.getElementsByClassName('w');  let loginBox = document.getElementsByClassName('login-box')[0];  let qrcodeLogin = document.getElementsByClassName('qrcode-login')[0];  let loginTab = document.getElementsByClassName('login-tab');  let loginTabL = document.getElementsByClassName('login-tab-l')[0];  let loginTabR = document.getElementsByClassName('login-tab-r')[0];  let mc = document.getElementsByClassName('mc');  let tabH = document.getElementsByClassName('tab-h')[0];  let form = document.getElementsByClassName('form')[0];  let itemFore4 = document.getElementsByClassName('item-fore4')[0];  let itemFore1 = document.getElementsByClassName('item-fore1');  let itemFore5 = document.getElementsByClassName('item-fore5');  let kbCoagent = document.getElementById('kbCoagent');  let msgWrap = document.getElementsByClassName('msg-wrap')[0];  let loginWrap = document.getElementsByClassName('login-wrap')[0];  let logo = document.getElementById('logo');  let formlogin = document.getElementById('formlogin');  let html = document.getElementsByTagName('html')[0];  let body = document.getElementsByTagName('body')[0];  let content = document.getElementById('content');  var meta = document.createElement('meta') ;  meta.setAttribute('name', 'viewport');  meta.setAttribute('content', 'width=device-width');  document.getElementsByTagName('head')[0].appendChild(meta);  document.getElementsByClassName('q-link')[0].style.display = 'none'; document.getElementsByClassName('login-banner')[0].style.display = 'none';  let content_offsetWidth = content.offsetWidth;  console.log(content_offsetWidth, 'content_offsetWidth');  loginForm.style.display = 'inline-block';  loginForm.style.float = 'left';  loginForm.style.width = '0';  loginTabL.firstChild.nextElementSibling.className = '';  loginTabR.firstChild.nextElementSibling.className = 'checked';  loginBanner.style.display = 'none';  loginTabR.click();  console.log(loginForm, 'loginForm');  let formlogin_offsetWidth = formlogin.offsetWidth;  console.log(formlogin_offsetWidth, 'formlogin_offsetWidth');  loginForm.style['margin-left'] = ((content_offsetWidth - formlogin_offsetWidth) / 2) + 'px';  tabH.style.display = 'none';  itemFore4.style.display = 'none';  kbCoagent.style.display = 'none';  msgWrap.style.display = 'none';  loginBox.style.padding = 0;  loginBox.style.margin = 0;  loginWrap.style.height = 0;  loginWrap.style.margin = 0;  form.style.margin = '0 auto';  console.log(itemFore5, 'itemFore5');  for (let j = 0; j < tipsWrapper.length; j++) {    let item = tipsWrapper[j];    item.style.display = 'none';  }  for (let i = 0; i < w.length; i++) {    let item = w[i];    if (i === 0) {      item.style.width = '100%';      item.style.display = 'inline-block';      let w0_offsetWidth = item.offsetWidth;      let logo_offsetWidth = logo.offsetWidth;      logo.style['margin-left'] = ((w0_offsetWidth - logo_offsetWidth) / 2) + 'px';      console.log(logo, 'logo');      console.log(w0_offsetWidth, 'w0_offsetWidth');      console.log(logo_offsetWidth, 'logo_offsetWidth');      continue;    }    item.style.width = '0';    if (i === 1) {      item.style.margin = '0';    }    if (i === 3) {      item.style.display = 'none';    }  }  for (let k = 0; k < loginTab.length; k++) {    let item = loginTab[k];    item.style.display = 'none';  }}jdnonedomeFn();document.getElementsByClassName('login-tab login-tab-r')[0].click();})()";

    private String tb_hideOthers_android = "(function() {document.getElementsByClassName('f-right')[0].style.display = 'none';document.getElementsByClassName('f-left')[0].style.display = 'none';document.getElementsByClassName('am-button btn-change')[0].style.display = 'none';})()";

    private String yhd_hideOthers_android = "(function() {document.getElementById('div_global_app').style.display = 'none';document.getElementsByClassName('touchweb-com_header')[0].style.display = 'none';document.getElementsByClassName('joint_login')[0].style.display = 'none';document.getElementsByClassName('remember_login')[0].style.display = 'none';document.getElementsByClassName('sms_login')[0].style.display = 'none';})()";

    private String gm_hideOthers_android = "(function() {document.getElementsByClassName('nav_return nav_ico nav_back')[0].style.display = 'none';document.getElementsByClassName('other')[0].style.display = 'none';document.getElementsByClassName('other-login')[0].style.display = 'none';document.getElementsByClassName('qk-login')[0].style.display = 'none';document.getElementsByClassName('login-style')[0].style.display = 'none';})()";

    private String su_hideOthers_android = "(function() {document.getElementsByClassName('sn-nav')[0].style.display = 'none';document.getElementsByClassName('login-guide')[0].style.display = 'none';document.getElementsByClassName('login-others')[0].style.display = 'none';document.getElementsByClassName('sign-btn')[0].style.display = 'none';})()";

    private String amz_hideOthers_android = "(function() {document.getElementById('accordion-row-register').style.display='none';document.getElementsByClassName('a-accordion-row-a11y')[0].style.display='none';document.getElementsByClassName('a-columna-span6a-spacing-medium')[0].style.display='none';;document.getElementsByClassName('a-columna-span6a-text-righta-spacing-nonea-span-last')[0].style.display='none';;document.getElementsByClassName('a-columna-span6a-spacing-medium')[0].style.display='none';;document.getElementsByClassName('a-sectiona-spacing-nonea-text-left')[0].style.display='none';document.getElementsByClassName('a-size-smallaccordionHeaderMessage')[0].style.display='none';})()";

    private String[] allJS = {tb_hideOthers_android,jd_hideOthers_android,amz_hideOthers_android,su_hideOthers_android,gm_hideOthers_android,yhd_hideOthers_android};

    private int position = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawler_taobao);
        webView = findViewById(R.id.webView);
        progress = findViewById(R.id.progress);
        position = getIntent().getIntExtra("position",0);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
//        CookieManager.getInstance().removeSessionCookie();
//        if(true){
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                CookieManager.getInstance().flush();
//            } else {
//                CookieSyncManager.createInstance(this.getApplicationContext());
//                CookieSyncManager.getInstance().sync();
//            }
//        }
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getSettings().setSafeBrowsingEnabled(false);
        }
        webView.getSettings().setDomStorageEnabled(true);//对H5支持
        webView.addJavascriptInterface(new OnJsHtml(), "local_obj");

        String mUsetAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2783.5 Safari/537.36";
        webView.getSettings().setUserAgentString(mUsetAgent);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(progress!=null&&view.getUrl().startsWith(loginUrls[position])){
                    Log.e("onProgressChanged","url"+ view.getUrl()+" progress"+ newProgress);
                    progress.show();
                    progress.setProgress(newProgress);
//                    if(newProgress == 100){
//                        progress.hide();
//                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("url----->> ", url);
//                if (isFristLogin) {
//
//                    onLoginSuccess(view, url);
//                    return;
//                }
//                webView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        progress.hide();
//                        webView.setVisibility(View.VISIBLE);
//                    }
//                },1000);
                if (url.startsWith(loginUrls[position])) {
//                    isFristLogin = false;
                    System.out.println(allJS[position]);
//                    mHandler.sendEmptyMessageDelayed(4, 1000);
                    webView.loadUrl("javascript:"+allJS[position]);
                    webView.setVisibility(View.VISIBLE);
                    progress.hide();
                } else {
                    if (!url.startsWith(loginUrls[position])) {

//                        if(!url.startsWith(cartUrls[position])){
//                            webView.loadUrl(cartUrls[position]);
//                            return;
//                        }

//                        if (!isFristLogin) {
//                            isFristLogin = true;
//                        }
                        CookieManager cookieManager = CookieManager.getInstance();
                        String CookieStr = cookieManager.getCookie(url);
                        if(CookieStr.contains("cookie2")) {
                            Log.e("CookieStr", CookieStr);
                            finish();
                        }
                    }
                }

            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                Log.e("onPageCommitVisible",url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @Override
            public void onLoadResource(WebView view, String url) {
            }

        });
        webView.loadUrl(cookiesUrls[position]);


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 4){
                webView.loadUrl("javascript:"+allJS[position]);
            }
        }
    };
    public void onLoginSuccess(WebView view, String url) {

    }


    Handler mHandlerTimeOut = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRunnable != null && mHandlerTimeOut != null) {
            mHandlerTimeOut.removeCallbacks(mRunnable);
        }
        webView.clearCache(true);
            // 清除cookie即可彻底清除缓存
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
        if(webView != null) {
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

                finish();
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



    /**
     * android  兼容4.4 系统的兼容问题！！！！！！！
     * H5 跳转 PC淘宝网页，会多次跳转，本机测试和模拟测试都是跳转了4 次
     * 逻辑贼操蛋
     */
    public class OnJsHtml {
        @JavascriptInterface
        public void loadAccountManagemenHtml(String titleHtml, String mHtmlString, String loginName) {

        }

        @JavascriptInterface
        public void loadAddressHtml(String titleHtml, String mHtmlString) {

        }

        @JavascriptInterface
        public void loadVipGrowthHtml(String titleHtml, String mHtmlString) {

        }


        @JavascriptInterface
        public void loadOrderHtml(String titleHtml, String mHtmlString) {
        }

        @JavascriptInterface
        public void loadOrderHtmlMore3(String titleHtml, String mHtmlString) {

        }

        /**
         * @param titleHtml
         * @param mHtmlString boolean ture 说明不可点击 ，
         * @param haveElement y 表示有这个标签  n 表示没这个标签
         */
        @JavascriptInterface
        public void loadMoreHtml(String titleHtml, boolean mHtmlString, String haveElement) {


        }
    }

}
