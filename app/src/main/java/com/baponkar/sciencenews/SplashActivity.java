package com.baponkar.sciencenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    private static final int splashTime = 3000;
    SharedPreferences pref;
    String initialUrlArrayString;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //hiding title bar
        getSupportActionBar().hide();

        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);

        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setAnimation(animZoomIn);

        TextView title = findViewById(R.id.textView);
        title.startAnimation(animZoomIn);

        pref = getApplicationContext().getSharedPreferences("Settings", 0 );

        if(initialUrlArrayString == null){
            LoadInitialUralArray();
        }

        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(splashTime);  //Delay of 3 seconds
                } catch (Exception e) {

                } finally {
                    //loading main activity
                    Intent i = new Intent( SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (pref.getBoolean("firstRun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            pref.edit().putInt("homeIndex", 0).commit(); //initialize homeIndex into zero
            // using the following line to edit/commit prefs
            pref.edit().putBoolean("firstRun", false).commit();
        }
    }
    public void LoadInitialUralArray(){
        //storing initial url array string
        Resources res = getResources();
        String [] urlArray = res.getStringArray(R.array.urls);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urlArray.length; i++) {
            sb.append(urlArray[i]).append(",");
        }
        pref.edit().putString("initialUrlArrayStrings", sb.toString()).commit();
    }
}