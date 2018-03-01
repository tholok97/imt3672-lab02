package com.example.tholok.lab2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.URL;

import static java.lang.Thread.sleep;

public class RSSFetcherService extends Service {

    DBHandler dbHandler;

    private static final String LOG_NAME = "rssservice";

    public RSSFetcherService() {

        dbHandler = new DBHandler(this, null, null, -1);

        Log.d(LOG_NAME, "I was created!!");

        Log.d(LOG_NAME, "Starting dispact thread");
        new DispatcherTask().execute();
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

            // sleep prefs amount of time -> do rss stuff

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

                // TODO
                dbHandler.addTopic(new Topic("From rss fetcher", "www.rss.com"));


                // TODO: make the whole download thing work

            }
        }
    }
}
