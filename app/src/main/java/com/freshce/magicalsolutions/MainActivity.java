package com.freshce.magicalsolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    WebView web;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.webView);
        progressBar = findViewById(R.id.pbar);
        progressBar.setMax(100);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);


        web.getSettings().setAppCacheEnabled(true);
//        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.loadData("", "text/html", null);
//        web.setWebChromeClient(new WebChromeClient());
//        web.setWebViewClient(new WebViewClient());
        // Performance
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setSaveFormData(true);
        webSettings.setGeolocationEnabled(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //performance ends
        web.setWebViewClient(new Callback());
//        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://freshce.com/");
        progressBar.setProgress(0);

        web.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress==100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });

//
//        web.setWebChromeClient(new WebChromeClient(){
//
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                progressBar.setProgress(newProgress);
//                if(newProgress==100)
//                    progressBar.setVisibility(View.INVISIBLE);
//                else
//                    progressBar.setVisibility(View.VISIBLE);
//                super.onProgressChanged(view, newProgress);
//            }
//        });

        web.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });

    }



    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            try {
                if ("freshce.com".equals(Uri.parse(url).getHost())) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }catch(Exception e) {}
            return false;
        }
    }

/*    protected class JsObject extends android.webkit.WebChromeClient {
        @JavascriptInterface
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       android.webkit.GeolocationPermissions.Callback callback) {

            // do we need to request permissions ?
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // this should never happen, it means user revoked permissions
                // need to warn and quit?
                callback.invoke(origin, false, false);
            }
            else {
                callback.invoke(origin, true, true);
            }
        }
    }*/
}