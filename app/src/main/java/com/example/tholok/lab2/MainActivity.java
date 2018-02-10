package com.example.tholok.lab2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        String[] foods = {
                "Bacon",
                "Omelette",
                "Hotdog",
                "Bacon",
                "Omelette",
                "Hotdog",
                "Bacon",
                "Omelette",
                "Hotdog",
                "Bacon",
                "Omelette",
                "Hotdog",
                "Bacon",
                "Omelette",
                "Hotdog",
        };


        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
        ListView listView = (ListView) findViewById(R.id.contentList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String food = String.valueOf(adapterView.getItemAtPosition(i));

                        Toast.makeText(MainActivity.this, food, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    /**
     * Move to settings page on click of settings button
     * @param view
     */
    public void settingsClick(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    /**
     * Fetch RSS feed upon click
     * @param view
     */
    public void fetchClick(View view) {

        // TODO. just show toast
        Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
    }
}
