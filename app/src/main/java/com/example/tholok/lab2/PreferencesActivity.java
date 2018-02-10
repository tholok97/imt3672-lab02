package com.example.tholok.lab2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // set 24 hour time picker
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    /**
     * Update preferences with values specified in widgets
     * @param view
     */
    void updateClick(View view) {

        Toast.makeText(this, "Not implemented!", Toast.LENGTH_SHORT).show();
    }
}
