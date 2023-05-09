package com.baponkar.sciencenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //hiding title bar
        //getSupportActionBar().hide();
    }

    //Handel Back Button Pressed
    public void onBackPressed() {
        finish();
    }

    public void onClick(View view){
        //Handel Previous and Next Button
        //This methods will help to change loaded website
        switch(view.getId()){
            case R.id.btnBack:{
                finish();
            }
            break;
            case R.id.privacyBtn:{
                Intent privacyIntent = new Intent(InfoActivity.this, PrivacyActivity.class);
                startActivity(privacyIntent);
            }
            break;
            case R.id.termsBtn:{
                Intent termsIntent = new Intent(InfoActivity.this, TermsActivity.class);
                startActivity(termsIntent);
            }
            break;
        }
    }
}