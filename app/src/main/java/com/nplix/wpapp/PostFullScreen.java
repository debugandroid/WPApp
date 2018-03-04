package com.nplix.wpapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class PostFullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_full_screen);
       // TextView textView=findViewById(R.id.fullpost);
        Intent intent=getIntent();
        Bundle bundle;
        bundle=intent.getExtras();
        WebView webView = findViewById(R.id.fullpost);

        if(bundle.isEmpty()){
            Log.d("PostFullScreen","Bundle is empty");
            webView.loadData("Data is empty", "text/html; charset=utf-8", "UTF-8");
        }
        else{

            webView.loadData(bundle.getString("content"), "text/html; charset=utf-8", "UTF-8");

           // textView.setText(Html.fromHtml());
        }

    }
}
