package com.baponkar.sciencenews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    LottieAnimationView animationView,loadingAnimationView;
    String[] urlArray;
    int index,minIndex = 0,maxIndex,homeIndex;
    WebView webView;
    //ProgressBar progressBar;
    TextToSpeech textToSpeech;
    String currentUrl, webviewText = "Please wait the Webpage is loading.";
    BottomNavigationView bottomNavigationView;
    FloatingActionButton speakrButton, shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speakrButton = findViewById(R.id.speaker_button);
        shareButton = findViewById(R.id.fab_button);

        animationView = findViewById(R.id.animationView);
        animationView.setVisibility(View.GONE);

        loadingAnimationView = findViewById(R.id.loadingAnimationView);
        loadingAnimationView.setVisibility(View.GONE);

        //progressBar = findViewById(R.id.progressBar);

        //Set up webview
        webView = findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient());
        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient());

        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        //Setup Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationId);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.btnBack:{
                        if(webView == null & webView.canGoBack()) {
                            webView.goBack();
                        }
                        else {
                            if(index > minIndex)
                            {
                                index--;
                                loadUrl(index);
                            }
                        }
                        break;
                    }

                    case R.id.btnHome:{
                        index = homeIndex;
                        loadUrl(index);
                        break;
                    }


                    case R.id.btnNext: {
                        if (index < maxIndex) {
                            index++;
                            loadUrl(index);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        //Shared preference
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", 0 );
        //We need an editor to edit and save the changes in shared preferences
        final SharedPreferences.Editor editor = pref.edit();

        Boolean checkFirstRun = pref.getBoolean("firstRun", true );
        if(checkFirstRun){
            Toast.makeText(this, "Thank you for Science News Installation", Toast.LENGTH_LONG).show();
        }

        //Url array
        String initialUrlArrayStrings = pref.getString("initialUrlArrayStrings","https://phys.org");
        urlArray = initialUrlArrayStrings.split(",");

        //set maxIndex
        maxIndex = urlArray.length - 1;
        int homeIndex = pref.getInt("homeIndex", 0);//setting Home
        index = homeIndex;

        loadUrl(index);
    }

    //---------------Methods------------------------------
    // Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file, menu);
        return true;
    }
    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings_intent);
                break;
            case R.id.info:
                Intent info_intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(info_intent);
                break;
            case R.id.quit:
                super.finish();
                this.finishAffinity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    //Handel Back Button Pressed
    public void onBackPressed() {
        if(webView!= null & webView.canGoBack()) {
            webView.goBack();// if there is previous page open it
        }
        else {
            super.onBackPressed();//if there is no previous page, close app
            this.finishAffinity();
        }
    }
    public void onClick(View view){
        //Handel Previous and Next Button
        //This methods will help to change loaded website
        switch(view.getId())
        {
            case R.id.fab_button:{
                currentUrl = webView.getUrl();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentUrl);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
            break;

            case R.id.speaker_button:{
                if(!textToSpeech.isSpeaking())
                {
                    textToSpeech.speak(webviewText,TextToSpeech.QUEUE_FLUSH,null);
                }
                else {
                    textToSpeech.stop();
                }


            }
        }
    }
    public void loadUrl(Integer index){
        String url = urlArray[index];
        if(checkNetworkConnection(this))
        {
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
            speakrButton.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);

            webView.loadUrl(url);
        }
        else{
            //webView.loadUrl("file:///android_res/raw/error.html");
            animationView.playAnimation();
            animationView.setVisibility(View.VISIBLE);
            speakrButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
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
            //progressBar.setVisibility(View.VISIBLE);
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
            //progressBar.setVisibility(View.GONE);
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
                //Loading Error HTML Page
               // webView.loadUrl("file:///android_res/raw/error.html");

                //Loading LOttie Anim
                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();

            }
        }
    }
}
