package com.example.tholok.lab2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayTopicActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_topic);

        // get the webview
        webView = (WebView) findViewById(R.id.web_view_topic);

        // get url from intent
        Intent intent = getIntent();
        String url = intent.getExtras().getString("url");

        webView.setWebViewClient(new WebViewClient());

        // load url
        webView.loadUrl(url);
    }
}
