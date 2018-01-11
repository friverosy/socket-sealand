package com.axxezo.MobileReader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloseTrip extends AppCompatActivity {
    private TextView title;
    private EditText total;
    private EditText pendings;
    private CircularProgressButton closeTripButton;
    private Vibrator mVibrator;
    private boolean onclick = false;
    private String status;
    private AlertDialog.Builder dialogo2;
    private static String AxxezoAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_trip);

        AxxezoAPI = "http://production-axxezo.brazilsouth.cloudapp.azure.com:5002/api";

        title = (TextView) findViewById(R.id.textView_title);
        total = (EditText) findViewById(R.id.editTextTotal);
        pendings = (EditText) findViewById(R.id.editTextPendings);
        closeTripButton = (CircularProgressButton) findViewById(R.id.buttoCloseTrip);

        // First dialog to inflate intent.
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Advertencia");
        dialogo1.setIcon(R.drawable.warning);
        dialogo1.setMessage("Este proceso cierra el viaje, SOLO EJECUTAR CUANDO ESTEN TODOS LOS PASAJEROS DESEMBARCADOS en el último puerto de la ruta.");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });

        // Second dialog to execute closeTrip PUT when there are pending passengers.
        dialogo2 = new AlertDialog.Builder(this);
        dialogo2.setTitle("Advertencia");
        dialogo2.setIcon(R.drawable.warning);
        dialogo2.setMessage("Aun no se registra el desembarque total de pasajeros.\n\n¿Está seguro de cerrar viaje?");
        dialogo2.setCancelable(false);

        dialogo2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onclick = true;
                new closeTripTask().execute();
                closeTripButton.setClickable(false);
                setResult(RESULT_OK, null);
            }
        });
        dialogo2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelar();
            }
        });


        dialogo1.show();
    }

    public void aceptar() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        title.setText("Cierre del viaje " + db.selectFirst("select route_id from config") +
                "\n"+db.selectFirst("select r.name from routes as r left join config as c on c.route_id=r.id where c.route_id=(select route_id from config)"));
        total.setText(db.selectFirst("select count(*) from manifest"));
        pendings.setText(String.format("%d", Integer.parseInt(db.selectFirst("select count(*) from manifest where is_inside=1"))));

        closeTripButton.setIndeterminateProgressMode(true);
        closeTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(pendings.getText().toString()) < 1) {
                    onclick = true;
                    new closeTripTask().execute();
                    closeTripButton.setClickable(false);
                    setResult(RESULT_OK, null);
                } else {
                    dialogo2.show();
                }
            }
        });
    }

    public void cancelar() {
        finish();
    }

    public class closeTripTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            final OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .build();
            return PUT(client);
        }

        /*@Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            int progress = Integer.parseInt(String.valueOf(values));
            closeTripButton.setProgress(progress);
        }*/

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ssssssss",s);
            if (s.startsWith("20") || s.equals("OK")) runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    closeTripButton.setProgress(100);
                }
            });
            else runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    closeTripButton.setProgress(-1);
                    Toast.makeText(getApplicationContext(), "Error al cerrar el viaje, intente nuevamente", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String PUT(OkHttpClient client) {
        String result = "";
        String json = "";
        JSONObject jsonObject = new JSONObject();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        try {
            String url = AxxezoAPI+"/itineraries/" + db.selectFirst("select r.id_mongo from routes as r left join config as c on c.route_id=r.id where c.route_id=(select route_id from config)");
            jsonObject.accumulate("active", false);
            json = jsonObject.toString();
            Log.d("json to PUT",json);

            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-type", "application/json")
                    .put(body)
                    .build();

            //PUT using okhttp
            Response response = client.newCall(request).execute();

            String tmp = response.body().string(); //Response{protocol=http/1.1, code=401, message=Unauthorized, url=http://axxezo-test.brazilsouth.cloudapp.azure.com:9001/api/registers}
            // 10. convert inputstream to string

            if (tmp != null && response.isSuccessful()) {
                result = response.message();
            } else {
                result = String.valueOf(response.code());
            }
        } catch (JSONException | IOException e){
            e.printStackTrace();
        }

        // 11. return result
        return result;
    }
}

