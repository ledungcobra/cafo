package com.ledungcobra.cafo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;


public class MainActivity extends Activity {
    View appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = findViewById(R.id.app_title);
        appTitle.setAlpha(0);

        appTitle.animate().alphaBy(1).setDuration(3000).start();
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this,ShopSelected.class);

                finish();
                startActivity(intent);
            }
        }.start();
//        String url = "https://app-demo-0123.herokuapp.com/";
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jor = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("ANDROIDDEBUG", "onResponse: " + response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("ANDROIDDEBUG", "Error: " );
//            }
//        });
//
//
//        queue.add(jor);
//        queue.start();
    }

}