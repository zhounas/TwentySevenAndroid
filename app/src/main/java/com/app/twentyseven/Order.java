package com.app.twentyseven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.text.DecimalFormat;

public class Order extends AppCompatActivity {
    private String newOrder,orderURL = com.app.twentyseven.Settings.ServerUrl+"AZ27/order.php";;
    private String[] splitedOrder,orderElementInfos;
    private int nOfSelectedElements,n;
    private double add;
    private RequestQueue queue;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        table = (TableLayout) findViewById(R.id.orderTable);
        Intent intent = getIntent();
        newOrder = intent.getStringExtra("order");
        nOfSelectedElements = Integer.parseInt(intent.getStringExtra("nOfSelectedElements"));

        showOrder(newOrder,nOfSelectedElements);
        ImageButton upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(nOfSelectedElements>0){
                uploadOrder(newOrder);
            }else{
                tmsg("Commande Vide!");
            }

            }
        });







    }

    public void onBackPressed() {
goBack();
        return;

    }
    private void goBack(){
        Intent Main = new Intent(Order.this, MainActivity.class);
        Main.putExtra("order", newOrder);
        Main.putExtra("nOfSelectedElements", ""+nOfSelectedElements);
        startActivity(Main);
        finish();
    }


    private void showOrder(String order,int nOfSelectedElements){
        table.removeAllViews();
        add=0;

            TableRow labels=new TableRow(this);
            labels.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            table.addView(labels);

            TextView labelName = new TextView(this);
            labelName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f));
            labelName.setText("Articles");
            labelName.setTextColor(Color.parseColor("#FFFFFF"));
            labelName.setGravity(Gravity.CENTER);
            labels.addView(labelName);

            TextView labelprice = new TextView(this);
            labelprice.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f));
            labelprice.setText("Prix");
            labelprice.setTextColor(Color.parseColor("#FFFFFF"));
            labelprice.setGravity(Gravity.CENTER);
            labels.addView(labelprice);

            TextView labeldelete = new TextView(this);
            labeldelete.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f));
            labeldelete.setText("Sup.");
            labeldelete.setGravity(Gravity.CENTER);
            labeldelete.setTextColor(Color.parseColor("#FFFFFF"));

            labels.addView(labeldelete);


        if (nOfSelectedElements > 0) {

            splitedOrder = order.split(",");
            n = 1;
            for (int row = 0; row < nOfSelectedElements; row++) {
                final int line=n;
                orderElementInfos = splitedOrder[n].split(":");
                final String name = orderElementInfos[0];
                final  double price = Double.parseDouble(orderElementInfos[1]);
                add+=(price*100);

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                tableRow.setGravity(Gravity.CENTER);
                table.addView(tableRow);


                TextView articleName= new TextView (this);
                articleName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f));
                articleName.setText(name);
                articleName.setTextColor(Color.parseColor("#FFFFFF"));
                articleName.setPadding(10, 10, 10, 10);
                tableRow.addView(articleName);


                TextView priceView = new TextView(this);
                priceView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f));
                priceView.setText(df2.format(price) + "€");
                priceView.setTextColor(Color.parseColor("#FFFFFF"));
                priceView.setPadding(10, 10, 10, 10);
                priceView.setGravity(Gravity.RIGHT);
                tableRow.addView(priceView);

                ImageButton delButton = new ImageButton(this);
                delButton.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f));
                delButton.setImageResource(R.drawable.delete);
                delButton.setBackgroundColor(Color.TRANSPARENT);
                delButton.setPadding(10, 10, 10, 10);

                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeElement(line);
                    }
                });
                tableRow.addView(delButton);
                n++;

            }
        }
        TableRow totalRow=new TableRow(this);
        totalRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        table.addView(totalRow);

        TextView total = new TextView(this);
        total.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f));
        total.setText("Prix Total");
        total.setTextColor(Color.parseColor("#FFFFFF"));
        total.setGravity(Gravity.RIGHT);
        totalRow.addView(total);

        TextView totalPrice = new TextView(this);
        totalPrice.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f));
        add=(double)add/100;
        totalPrice.setText(add+"€");
        totalPrice.setTextColor(Color.parseColor("#FFFFFF"));
        totalPrice.setGravity(Gravity.RIGHT);
        totalRow.addView(totalPrice);

        TextView vide = new TextView(this);
        vide.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f));
        totalRow.addView(vide);
    }


    private void removeElement ( int number){
        newOrder="";
        for (int i = 1; i <= nOfSelectedElements; i++) {
            if (i != number) {
                newOrder += "," + splitedOrder[i];
            }
        }
        nOfSelectedElements -= 1;

        showOrder(newOrder, nOfSelectedElements);

    }

    private void uploadOrder(String order){

        queue = Volley.newRequestQueue(this);
        String URL=orderURL+"?android_id="+com.app.twentyseven.Settings.getAndroidId(this)+"&querry="+ URLEncoder.encode(order);


        StringRequest SRequest = new StringRequest(Request.Method.GET, ""+URL+"", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.substring(0, response.indexOf(' ')).equals("ok")) {
                    orderSaved(response);




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
    private void orderSaved(String response){

        newOrder="";
        nOfSelectedElements=0;
        TextView mainText = (TextView) findViewById(R.id.mainText);
        mainText.setText("Commande " + response.substring(response.indexOf(' ')) + " Enregistré");
        showOrder(newOrder,0);
    }
    private void tmsg(String msg){

        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
    private static DecimalFormat df2 = new DecimalFormat("0.00");
}