package com.app.twentyseven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Logo extends AppCompatActivity {
    private int Time=4000;
    private String loginURL = "http://192.168.0.38/AZ27/login.php";
    private RequestQueue queue;
    private boolean loggedin = false;
    Intent nextpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        String androidID = android.provider.Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginURL += "?id_status="+androidID;


        StringRequest SRequest = new StringRequest(Request.Method.GET, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.substring(0, response.indexOf(' ')).equals("Bienvenu")) {
                    loggedin=true;
                    tmsg(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tmsg("Server Error!");
            }
        });

        queue.add(SRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loggedin==true){
                     nextpage = new Intent (Logo.this,MainActivity.class);
                }else {
                     nextpage = new Intent(Logo.this, Login.class);
                }
                startActivity(nextpage );
                finish();
            }
        },Time);
    }
private void tmsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }
}
