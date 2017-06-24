package com.example.gorg.monny;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncService {

    private static SharedPreferences app_settings;
    private static String URL;
    private Context context;
    private File dataFile;
    private List<String> linesToSend;
    private JSONObject jsonToSend;

    public SyncService(File file, Context context) {
        dataFile = file;
        this.context = context;
        app_settings = PreferenceManager.getDefaultSharedPreferences(context);
        URL = app_settings.getString("server_adr","");
    }

    public void perform() {
        readFile();
        buildJson();
        sendJson();
    }

    private void readFile() {
        String syncedString = "synced";

        linesToSend  = new ArrayList<>();
        BufferedReader br = null;

        try {
            String sCurrentLine;
            // TODO: currently reading all the file from beginning to end, change it to reverse-order reading later
            // TODO: skip strange and empty lines
            br = new BufferedReader(new FileReader(dataFile));

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith(syncedString)){
                    linesToSend.clear();
                } else {
                    linesToSend.add(sCurrentLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void buildJson() {
        jsonToSend = new JSONObject();
        JSONObject syncJson = new JSONObject();
        JSONArray recordsJson = new JSONArray();

        try {
            for(String str : linesToSend) {
                String[] arr = str.split(";");
                JSONObject record = new JSONObject();

                record.put("time", arr[0]);
                record.put("sign", arr[1]);
                record.put("amount", arr[2]);
                record.put("category", arr[3]);
                if (arr.length > 4) {
                    record.put("text", arr[4]);
                }
                String author = app_settings.getString("username", "unset");
                record.put("author", author);
                recordsJson.put(record);
            }
            syncJson.put("records", recordsJson);
            jsonToSend.put("sync", syncJson);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendJson() {
        JsonObjectRequest req = new JsonObjectRequest(URL, jsonToSend,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject updated_sums = response.getJSONObject("updated_sums");
                            int created_objects = response.getInt("created");

                            updateCurrentTotalSums(updated_sums);

                            Toast.makeText(context, "Synced ! ", Toast.LENGTH_SHORT).show();
                            MainActivity.updateCurrentSumonNextScreenButton();
                            if (created_objects > 0) {
                                markSyncTimeInDataFile(updated_sums, created_objects);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        // Access the RequestQueue through singleton class.
        Singleton.getInstance(context).addToRequestQueue(req);
    }

    private void updateCurrentTotalSums(JSONObject updated_sums) throws JSONException {
        // TODO: Broken encapsulation, take settings to some Singleton maybe ?
        SharedPreferences.Editor editor = SecondActivity.settingsPersistence.edit();

        String authors_str = app_settings.getString("authors_list", "");
        String[] authors = authors_str.split(",");
            for(String author : authors ){
                int sum = updated_sums.getInt(author);
                editor.putInt (author, sum);
            }
        editor.commit();
    }

    private void markSyncTimeInDataFile(JSONObject updated_sums, int created_objects) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String string = "synced;" + timeStamp + "created: " + created_objects + " sum: " + updated_sums;

        PrintWriter pw = null;
        try {
            OutputStream os = new FileOutputStream(dataFile, true);
            pw = new PrintWriter(os);
            pw.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }
}