package com.kisanseva.kisanseva.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kisanseva.kisanseva.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarketSellingPrices extends Activity {
    public String URL = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd00000192e33804351e486047a6c8ba98c6eed6&format=json&offset=0&limit=10";
    TableLayout tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_selling_prices);
        try {
            String urltobecalled = URL;
            run(urltobecalled);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header("Content-type","application/json")
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MarketSellingPrices.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(myResponse);
                        TextView text=(TextView)findViewById(R.id.result);

                        try {
                            JSONObject j=new JSONObject(myResponse);
                            String label=j.get("desc").toString();
                            text.setText(label+"\n");

                            JSONArray results=j.getJSONArray("records");
                            tb=(TableLayout)findViewById(R.id.testTable);

                            for (int i=0;i<results.length();i++){
                                JSONObject eachresult=(JSONObject) results.get(i);
                                String commodity=(eachresult.getString("state")+eachresult.getString("district")+eachresult.getString("market")+
                                        eachresult.getString("commodity")+eachresult.getString("variety"));

                                createTableRow(eachresult.getString("commodity"),eachresult.getString("min_price"),eachresult.getString("max_price"));

                            }

                        } catch (Exception ex) {
                            System.out.println("run Exception " + ex.getMessage());
                        }
                    }
                });

            }
        });
    }
    public void createTableRow(String c1,String c2,String c3){
        TableRow tr=new TableRow(MarketSellingPrices.this);
        tr.setBackground(new ColorDrawable(Color.BLUE));
        TextView t1=new TextView(MarketSellingPrices.this);
        t1.setText(c1);
        TextView t2=new TextView(MarketSellingPrices.this);
        t2.setText(c2);
        TextView t3=new TextView(MarketSellingPrices.this);
        t3.setText(c3);

        tr.addView(t1);
        tr.addView(t2);
        tr.addView(t3);

        tb.addView(tr);

    }
}
