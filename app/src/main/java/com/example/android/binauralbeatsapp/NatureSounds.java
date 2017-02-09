package com.example.android.binauralbeatsapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.view.View.OnTouchListener;
import android.content.DialogInterface.OnClickListener;

public class NatureSounds extends Activity {

    Button rain_button = (Button) findViewById(R.id.rainforest_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nature_sounds);
    }


}
