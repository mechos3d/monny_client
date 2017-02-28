package com.example.gorg.monny;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: make this save records list in a local file, updating it when needed
public class ThirdActivity extends AppCompatActivity {

    private static SharedPreferences app_settings;
    private static String URL;
    private static Context context;
    private static TextView recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        app_settings = PreferenceManager.getDefaultSharedPreferences(this);
        URL = app_settings.getString("records_list_adr","");
        context = this;

        recordsList=(TextView)findViewById(R.id.records_history);
        recordsList.setText("waiting...");
        getRecordsList();
    }

    private void getRecordsList() {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        updateRecordsList(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                });
        // Access the RequestQueue through singleton class.
        Singleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    private void updateRecordsList(JSONArray response){
        String fullString = "";
        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject record = (JSONObject) response
                        .get(i);
                //2016-11-11T11:11:11.000Z
                String time = record.getString("time").substring(5,16).replace("T","_");
                String sign = record.getString("sign");
                String amount = record.getString("amount");
                String category = record.getString("category");
                String author = record.getString("author");
                String text = record.getString("text");

                fullString += time + " | " + sign + " " + amount + " | " + category + " | " + author + " | " + text + "\n";
            }
        } catch (JSONException e) {
        e.printStackTrace();
        Toast.makeText(getApplicationContext(),
                "Error: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
         }
        recordsList.setText(fullString);
    }
}
