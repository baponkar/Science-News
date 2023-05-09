package com.baponkar.sciencenews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {
    EditText editText;
    public static final String URL_REGEX = "^((http|https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    Spinner spinner;
    //items show on listview
    String [] urlArray;
    String [] urlArrayTitle;
    int homeIndex = 0;
    SharedPreferences pref;
    String initialUrlArrayStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = getApplicationContext().getSharedPreferences("Settings", 0 );
        initialUrlArrayStrings = pref.getString("initialUrlArrayStrings","https://phys.org");
        urlArray = initialUrlArrayStrings.split(",");

        editText = (EditText) findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newUrl = editText.getText().toString();
                if(UrlCheck(newUrl))
                {
                    initialUrlArrayStrings += "," + newUrl;
                    //Show toast
                    Toast.makeText(getApplicationContext(),
                            "Successfully added : " + newUrl, Toast.LENGTH_LONG).show();
                }
                else
                {
                    //Show toast
                    Toast.makeText(getApplicationContext(),
                            "It is not a valid URL : " + newUrl, Toast.LENGTH_LONG).show();
                }
            }
        });

        //Shared preference
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", Context.MODE_WORLD_READABLE );
        //We need an editor to edit and save thhe changes in shared preferences
        final SharedPreferences.Editor editor = pref.edit();

        homeIndex = pref.getInt("homeIndex", 0);

        //Url array
        Resources res = getResources();
        urlArrayTitle = res.getStringArray(R.array.urlTitles);
        urlArray = res.getStringArray(R.array.urls);

        spinner = (Spinner) findViewById(R.id.spinner);
        //make an array of the objects according to a layout design
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, urlArrayTitle);
        spinner.setAdapter(adapter);
        //On item selected event
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Set Pref
                editor.putInt("homeIndex", i);
                editor.commit();
                Toast.makeText(SettingsActivity.this, "Successfully Changed home into " + urlArrayTitle[i].toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }
    //Buttons operations
    public void onClick(View view){
        switch(view.getId()){
            case R.id.insertBtn: {
                String url = editText.getText().toString();
                if (UrlCheck(url)) {
                    Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Not a valid URL!!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnBack: {
                finish();
            }
            break;
        }
    }

    //Handel Back Button Pressed
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public boolean UrlCheck(String checkUrl) {
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(checkUrl);
        if(m.find() ) {
            for (int i=0; i < urlArray.length; i++){
                if(checkUrl == urlArray[i]){
                    return true;
                }
            }

        }
        return false;
    }


}