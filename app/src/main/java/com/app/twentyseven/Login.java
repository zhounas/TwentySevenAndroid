package com.app.twentyseven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Login extends AppCompatActivity {
    private String loginURL = "http://192.168.0.38/AZ27/login.php";
    private RequestQueue queue;
    private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        String androidID = android.provider.Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginURL += "?android_id="+androidID;
        setContentView(R.layout.activity_login);
        Button loginButton=findViewById(R.id.loginButton);
        username=findViewById(R.id.username);
        password=findViewById(R.id.passeword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginURL += "&username="+ username.getText()+"&password="+ password.getText();
                StringRequest SRequest = new StringRequest(Request.Method.GET, loginURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tmsg(response);
                        if (response.substring(0, response.indexOf(' ')).equals("Bienvenu")) {

                            Intent Main = new Intent(Login.this, MainActivity.class);
                            Main.putExtra("username", username.getText());
                            startActivity(Main);
                            finish();
                        } else if (response.substring(0, response.indexOf(' ')).equals("Error")) {
                            tmsg("Login Error!");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tmsg("Server Error!");
                    }
                });

                queue.add(SRequest);
            }
        });

    }
    private void tmsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
