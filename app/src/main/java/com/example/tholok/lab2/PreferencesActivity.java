package com.example.tholok.lab2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;

public class PreferencesActivity extends Activity {

    // names of prefs
    private static final String RSS_FEED_PREF = "rssFeed";
    private static final String INTERVAL_HOUR_PREF = "intervalHour";
    private static final String INTERVAL_MINUTE_PREF = "intervalMinute";
    private static final String MAX_TOPICS_PREF = "maxTopics";

    // save widgets as properties
    private TextView rssFeed;
    private TimePicker timePicker;
    private EditText maxTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // save widgets as properties
        rssFeed = (TextView) findViewById(R.id.rssFeed);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        maxTopics = (EditText) findViewById(R.id.maxTopics);

        rssFeed.setText("koko");

        // set 24 hour time picker
        timePicker.setIs24HourView(true);

        // load widgets from prefs
        loadWidgetsFromPrefs();
    }

    /**
     * Load widget values from prefs
     */
    void loadWidgetsFromPrefs() {

        // get prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // set rssFeed
        rssFeed.setText(prefs.getString(RSS_FEED_PREF, ""));

        // set timepicker
        timePicker.setCurrentHour(prefs.getInt(INTERVAL_HOUR_PREF, 0));
        timePicker.setCurrentMinute(prefs.getInt(INTERVAL_MINUTE_PREF, 0));

        // set max topics
        maxTopics.setText(Integer.toString(prefs.getInt(MAX_TOPICS_PREF, 0)));
    }

    /**
     * Update preferences with values specified in widgets
     * @param view
     */
    public void updateClick(View view) {

        // get prefs and editor
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        // save RSS feed
        editor.putString(RSS_FEED_PREF, rssFeed.getText().toString());

        // save interval
        editor.putInt(INTERVAL_HOUR_PREF, timePicker.getCurrentHour());
        editor.putInt(INTERVAL_MINUTE_PREF, timePicker.getCurrentMinute());


        // save max topics
        editor.putInt(MAX_TOPICS_PREF, Integer.parseInt(maxTopics.getText().toString()));

        // apply
        editor.apply();


        // display toast
        Toast.makeText(this, "Updated preferences!", Toast.LENGTH_SHORT).show();
    }
}
