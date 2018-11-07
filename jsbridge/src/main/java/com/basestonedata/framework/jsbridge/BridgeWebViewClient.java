package com.basestonedata.framework.jsbridge;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.startsWith(BridgeUtil.WVJB_BSD_SCHEMA) || url.startsWith(BridgeUtil.WVJB_TEL_SCHEMA)) {
            if (webView.navigateImpl != null) {
                webView.navigateImpl.navigate(url);
            }
            return true;
        }
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//19版本以上原始逻辑不改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (url.startsWith(BridgeUtil.WVJB_BRIDGE_LOADED)) {
                BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);

                if (webView.getStartupMessage() != null) {
                    for (Message m : webView.getStartupMessage()) {
                        webView.dispatchMessage(m);
                    }
                    webView.setStartupMessage(null);
                }
                return true;
            } else if (url.startsWith(BridgeUtil.WVJB_RETURN_DATA)) { // 如果是返回数据
                webView.handlerReturnData(url);
                return true;
            } else if (url.startsWith(BridgeUtil.WVJB_OVERRIDE_SCHEMA)) { //
                webView.flushMessageQueue();
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        } else {
            if (url.startsWith(BridgeUtil.WVJB_BRIDGE_LOADED)) {
                BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);

                if (webView.getStartupMessage() != null) {
                    for (Message m : webView.getStartupMessage()) {
                        webView.dispatchMessage(m);
                    }
                    webView.setStartupMessage(null);
                }
//            return true;
            }
            if (url.startsWith(BridgeUtil.WVJB_RETURN_DATA)) { // 如果是返回数据
                webView.handlerReturnData(url);
                return true;
            } else if (url.startsWith(BridgeUtil.WVJB_OVERRIDE_SCHEMA)) { //
                webView.flushMessageQueue();
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }


    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}