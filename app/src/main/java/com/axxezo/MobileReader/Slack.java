package com.axxezo.MobileReader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by axxezo on 07/08/2017.
 * Simple slack integration
 */

public class Slack {
    private Context context;

    public Slack(Context context){
        this.context=context;
    }


    public void sendMessage(String title, String message) {
        new sendTask(title, message).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class sendTask extends AsyncTask<Void, Void, Void> {
        private String title;
        private String message;

        public sendTask(String title, String message) {
            this.title = title;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Post(title, message);
            return null;
        }
    }


    private void Post(String title, String message) {
        OkHttpClient client = new OkHttpClient();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        DatabaseHelper db=DatabaseHelper.getInstance(context);
        String route=db.selectFirst("select route_name from config");
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        try {

            jsonObject.put("title", "Stacktrace PDA " + Build.SERIAL);
            jsonObject.put("color", "#FA5858");
            jsonObject.put("author_name",route);
            jsonObject.put("pretext", title);
            jsonObject.put("text", message);
            jsonObject.put("footer","Naviera Austral");
            jsonArray.put(jsonObject);

            JSONObject mainObj = new JSONObject();
            mainObj.put("attachments", jsonArray);

            // Convert JSONObject to JSON to String
            json = mainObj.toString();
            //Log.i("json to POST", json);

            RequestBody body = RequestBody.create(JSON, json);

            // Create object okhttp
            Request request = new Request.Builder()
                    .url("https://hooks.slack.com/services/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")
                    .addHeader("Content-type", "application/json")
                    .post(body)
                    .build();

            // Execute POST request to the given URL
            Response response = client.newCall(request).execute();
            String bodyResponse = response.body().string();
            if (response.isSuccessful()) {
                // Do something.
            } else Log.e(response.message(), bodyResponse);
        } catch (HttpHostConnectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
