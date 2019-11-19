package com.app.twentyseven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private String loginURL = "http://192.168.0.38/AZ27/login.php";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Deconnexion:


                String androidID = android.provider.Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                loginURL += "?logout=1&android_id="+androidID;
                StringRequest SRequest = new StringRequest(Request.Method.GET, loginURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.substring(0, response.indexOf(' ')).equals("Deconnexion")) {

                            tmsg(response);
                            Intent Login = new Intent(MainActivity.this, Login.class);
                            startActivity(Login);
                            finish();


                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tmsg("Server Error!");
                    }
                });

                queue.add(SRequest);

                return true;
            case R.id.Rafraichir:
                finish();
                startActivity(getIntent());
                return true;


            case R.id.quitter:
                System.exit(1);
                return true;
        }
        return true;
    }
    private void tmsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
