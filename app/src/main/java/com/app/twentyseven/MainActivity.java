package com.app.twentyseven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private TextView mainText,nSelectedElements;
    private TableLayout table;
    private String loginURL = com.app.twentyseven.Settings.ServerUrl+"AZ27/login.php";
    private String order, menuURL = com.app.twentyseven.Settings.ServerUrl+"AZ27/menu.php";
    private RequestQueue queue;
    public String[] menuElements, menuElementsInfos;
    public int n, numberOfArticles, nOfSelectedElements = 0, totalPrice = 0;
    public double temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainText = findViewById(R.id.mainText);
        nSelectedElements = findViewById(R.id.nSelectedElements);

        Intent intent = getIntent();
        if(intent.getStringExtra("nOfSelectedElements") != null){
            order = intent.getStringExtra("order");
            nOfSelectedElements=Integer.parseInt(intent.getStringExtra("nOfSelectedElements"));
            nSelectedElements.setText("" + nOfSelectedElements);

        }

        table = (TableLayout) findViewById(R.id.menuTable);
        ImageButton orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent Order = new Intent(MainActivity.this, Order.class);
                Order.putExtra("order", order);
                Order.putExtra("nOfSelectedElements", nSelectedElements.getText());
                startActivity(Order);
                finish();
            }
        });
        getMenu(0);
    }

    public void onBackPressed() {
        mainText.setText("Menu");
        getMenu(0);
            return;

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

    private void getMenu(final int id_famille){
        menuURL += "?android_id=" + com.app.twentyseven.Settings.getAndroidId(this) + "&querry=" + id_famille;

        StringRequest SRequest = new StringRequest(Request.Method.GET, menuURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showMenu(response,id_famille);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tmsg("Server Error!");
            }

        });
        queue.add(SRequest);
    }

    public void showMenu(String serverResponse, final int id_famille) {
        table.removeAllViews();

        menuElements = serverResponse.split(",");
        numberOfArticles = Integer.parseInt(menuElements[0]);
        if(numberOfArticles>0) {
            n=1;
            int numberOfRows = (int) Math.ceil(numberOfArticles / com.app.twentyseven.Settings.numberOfCols);
            for (int row = 0; row <= numberOfRows; row++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                tableRow.setGravity(Gravity.CENTER);
                table.addView(tableRow);

                for (int col = 0; col < com.app.twentyseven.Settings.numberOfCols; col++) {
                    Button button = new Button(this);
                    button.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));

                    if(n <= numberOfArticles) {
                        menuElementsInfos = menuElements[n].split(":");
                        final String name = menuElementsInfos[0];
                        final int idE = Integer.parseInt(menuElementsInfos[1]);
                        if(id_famille != 0){
                            temp = Double.parseDouble( menuElementsInfos[2]);
                        }
                        final double price = temp;
                        button.setText(name);
                        n++;
                        button.setPadding(20, 20, 20, 20);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(id_famille!=0) {
                                    nOfSelectedElements += 1;
                                    nSelectedElements.setText("" + nOfSelectedElements);
                                    order += "," + name + ":" + price;
                                }else{
                                    getMenu(idE);
                                    mainText.setText(name);
                                }
                            }
                        });
                    }else{
                        button.setBackgroundColor(Color.TRANSPARENT);
                    }
                    tableRow.addView(button);
                }
            }
        }
    }

    private void tmsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
