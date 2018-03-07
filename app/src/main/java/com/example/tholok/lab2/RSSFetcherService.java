package com.example.tholok.lab2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import java.net.URL;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class RSSFetcherService extends Service {

    DBHandler dbHandler;

    private static final String LOG_NAME = "rssservice";

    public RSSFetcherService() {

        dbHandler = new DBHandler(this, null, null, -1);

        Log.d(LOG_NAME, "I was created!!");

        Log.d(LOG_NAME, "Starting dispact thread");
        DispatcherTask dispatcherTask = new  DispatcherTask();

        /*
        In modern API levels only one AsyncTask can be running at once, and RSSFetcherService has an
        AsyncTask running forever. This task is blocking all other tasks. A (and dirty) fix to this
        is what I've done here: just make AsyncTask execute in parallel.

        In the future I'll be avoiding having long-lived AsyncTasks

        If you're in IMT3003; see issue #20
         */
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            dispatcherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            dispatcherTask.execute();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(LOG_NAME, "I was started!!");

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class DispatcherTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            // TBD

            // sleep prefs amount of time then fetch topics, update db and notify MainActivity
            while (true) {

                // fetch pref sleep
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RSSFetcherService.this);
                int hours = prefs.getInt(PreferencesActivity.INTERVAL_HOUR_PREF, 0);
                int minutes = prefs.getInt(PreferencesActivity.INTERVAL_MINUTE_PREF, 10);

                Log.d(LOG_NAME, "Pref hours, sleep: " + Integer.toString(hours) + ", " + Integer.toString(minutes));

                // convert to milliseconds
                int milliseconds = minutes*60*1000 + hours*24*60*10000;

                Log.d(LOG_NAME, "Pref milli: " + Integer.toString(milliseconds));

                // try and sleep
                try {
                    sleep(milliseconds);
                } catch (Exception ex) {
                    Log.d(LOG_NAME, "Couldn't sleep.. : " + ex.getMessage());
                }

                // do work
                Log.d(LOG_NAME, "DOING WORK");

                fetch(RSSFetcherService.this, dbHandler);
            }
        }
    }

    /**
     * Fetches from given rss feed and updates db. Also notifies MainActivity of update
     */
    public static void fetch(final Context ctx, final DBHandler dbHandler) {

        // get pref
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        // get url from pref
        String url = prefs.getString(PreferencesActivity.RSS_FEED_PREF, "");

        // get max from pref
        final int maxTopics = prefs.getInt(PreferencesActivity.MAX_TOPICS_PREF, 0);


        // clear the database (new rss stuff will completely replace old ones)
        dbHandler.clear();

        Log.d(LOG_NAME, "starting url: " + url);

        // Make parser do work
        Parser parser = new Parser();
        parser.execute(url);
        parser.onFinish(new Parser.OnTaskCompleted() {


            @Override
            public void onTaskCompleted(ArrayList<Article> list) {

                Log.d(LOG_NAME, "task completed successfully");

                // for each topic -> debugprint and add to db
                // get maximum 'maxTopics' topics
                // (maxiumum 0 means infinite)
                for (int i = 0; i < list.size() && (i < maxTopics || maxTopics == 0); ++i) {
                    Log.d(LOG_NAME, ">> title: " + list.get(i).getTitle());
                    Log.d(LOG_NAME, ">> link: " + list.get(i).getLink());
                    Log.d(LOG_NAME, "--");
                    dbHandler.addTopic(new Topic(list.get(i).getTitle(), list.get(i).getLink()));
                }

                // notify MainActivity through broadcast receiver
                Intent i = new Intent("android.intent.action.DATABASE_UPDATED");
                ctx.sendBroadcast(i);

            }

            @Override
            public void onError() {
                // TODO: handle more gracefully
                Log.d(LOG_NAME, "Couldn't fetch from rss source :(");
            }
        });

    }
}
