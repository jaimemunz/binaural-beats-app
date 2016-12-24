package com.example.android.binauralbeatsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Jaime on 12/24/2016.
 */

public class SleepTimerDialog extends Activity {

    CharSequence times[] = new CharSequence[] {"Sleep in 10 minutes", "Sleep in 15 minutes",
            "Sleep in 30 minutes", "Other..."};
    private int minutesToSleep = 0;

    public void getSleepTime(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose when to sleep");
        builder.setItems(times, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    minutesToSleep = 10;
                } else if (which == 1) {
                    minutesToSleep = 15;
                } else if (which == 2) {
                    minutesToSleep = 30;
                }
            }

        });
        builder.show();

    }

}
