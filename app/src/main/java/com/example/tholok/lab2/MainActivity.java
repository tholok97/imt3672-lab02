package com.example.tholok.lab2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String LOG_NAME = "mainlog";
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        // prepare dbhandler
        dbHandler = new DBHandler(this, null, null, -1);


        // TEST
        dbHandler.clear();
        dbHandler.addTopic(new Topic("VG TING", "www.vg.no"));
        dbHandler.addTopic(new Topic("dagobladet", "www.dag.no"));

        // start service
        Intent intent = new Intent(this, RSSFetcherService.class);
        startService(intent);


        // update list
        updateListView();
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

        updateListView();

        // TODO. just show toast
        Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
    }

    /**
     * when db updated -> fill list with stuff from db
     */
    public void updateListView() {
        Log.d("settopictask", "running onUpdateDB. Starting AsyncTask");
        SetTopicsTask setTopicsTask = new SetTopicsTask();
        setTopicsTask.execute(dbHandler);
    }

    private class SetTopicsTask extends AsyncTask<DBHandler, Void, ArrayList<Topic>> {

        public SetTopicsTask() {
            Log.d("settopictask", "running constructor for SetTopicTask");

        }

        @Override
        protected ArrayList<Topic> doInBackground(DBHandler... dbHandlers) {


            DBHandler dbHandler = dbHandlers[0];

            return dbHandler.getAllTopics();
        }

        @Override
        protected void onPostExecute(ArrayList<Topic> topics) {
            super.onPostExecute(topics);


            Log.d("settopictask","running onPostExecute!");



            ArrayAdapter arrayAdapter = new ArrayAdapter<Topic>(MainActivity.this, android.R.layout.simple_list_item_1, topics);
            ListView listView = (ListView) findViewById(R.id.contentList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Topic topic = (Topic) adapterView.getItemAtPosition(i);

                            Toast.makeText(MainActivity.this, topic.get_link(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

        }
    }
}
