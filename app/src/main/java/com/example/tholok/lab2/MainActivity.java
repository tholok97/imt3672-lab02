package com.example.tholok.lab2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private BroadcastReceiver mReceiver;

    private static final String LOG_NAME = "mainlog";
    private DBHandler dbHandler;

    /*
     Heavily inspired by http://inchoo.net/dev-talk/android-development/broadcast-receiver-from-activity/
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.DATABASE_UPDATED");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                updateListView();

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }

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

        RSSFetcherService.fetch(this);

        Toast.makeText(this, "Force-fetching...", Toast.LENGTH_SHORT).show();
    }

    /**
     * when db updated -> fill list with stuff from db
     */
    public void updateListView() {
        Log.d("settopictask", "running onUpdateDB. Starting AsyncTask");
        SetTopicsTask setTopicsTask = new SetTopicsTask();

        /*
        In modern API levels only one AsyncTask can be running at once, and RSSFetcherService has an
        AsyncTask running forever. This task is blocking all other tasks. A (and dirty) fix to this
        is what I've done here: just make AsyncTask execute in parallel.

        In the future I'll be avoiding having long-lived AsyncTasks

        If you're in IMT3003; see issue #20
         */
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            setTopicsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dbHandler);
        } else {
            setTopicsTask.execute();
        }
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
