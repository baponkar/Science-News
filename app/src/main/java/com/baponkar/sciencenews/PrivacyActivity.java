package com.baponkar.sciencenews;
import static com.baponkar.sciencenews.MainActivity.checkNetworkConnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

public class PrivacyActivity extends AppCompatActivity {
    WebView webView;
    LottieAnimationView loadingAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        webView = findViewById(R.id.webView);
        loadingAnimationView = findViewById(R.id.loadingAnimationView);

        loadUrl("https://baponkar.github.io/Science-News/privacy.html");

    }

    //Handel Back Button Pressed
    public void onBackPressed() {

        finish();
    }
    public void onClick(View view){
        finish();
    }
    public void loadUrl(String url){
        if(checkNetworkConnection(this))
        {
            webView.loadUrl(url);
        }
        else{
            webView.loadUrl("file:///android_res/raw/error.html");
        }
    }
    public static boolean checkNetworkConnection(Context _context){
        //If there is net connection then this method
        //will give true otherwise give false
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }
    public class WebViewClient extends android.webkit.WebViewClient{
        //To handel webview connection
        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon){
            super.onPageStarted(webView,url,favicon);
            loadingAnimationView.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView,String url){
            webView.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView webView, String url){
            super.onPageFinished(webView, url);
            loadingAnimationView.setVisibility(View.GONE);
        }

        @Override
        public void onLoadResource(WebView webView, String url){
            //Todo auto-generated method sub
            super.onLoadResource(webView, url);
        }

        public void OnReceivedError(WebView webView,int errorCode, String description, String failedUrl){
            super.onReceivedError(webView,errorCode,description,failedUrl);

            if(errorCode == -2) {
                webView.loadUrl("file:///android_res/raw/error.html");
            }
        }
    }
}