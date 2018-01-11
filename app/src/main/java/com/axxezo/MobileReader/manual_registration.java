package com.axxezo.MobileReader;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class manual_registration extends AppCompatActivity {
    private EditText ticket_no;
    private CustomEditText dni;
    private EditText name;
    private Spinner origin;
    private Spinner destination;
    private Button save_manual_registration;
    private String selected_origin;
    private String selected_destination;
    private Vibrator mVibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DatabaseHelper db = DatabaseHelper.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_registration);
        ticket_no = (EditText) findViewById(R.id.registration_input_ticket);
        dni = (CustomEditText) findViewById(R.id.registration_input_DNI);
        name = (EditText) findViewById(R.id.registration_input_name);
        origin = (Spinner) findViewById(R.id.registration_spinner_origin);
        destination = (Spinner) findViewById(R.id.registration_spinner_destination);
        save_manual_registration = (Button) findViewById(R.id.button_manual_registration_commit);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Fill information in combobox
        Cursor getOriginandDestination = db.select("select name from ports");
        ArrayList<String> listOriginDestination = new ArrayList<String>();
        if (getOriginandDestination != null)
            while (!getOriginandDestination.isAfterLast()) {
                listOriginDestination.add(getOriginandDestination.getString(0));
                getOriginandDestination.moveToNext();
            }
        if (listOriginDestination == null || listOriginDestination.isEmpty())
            listOriginDestination.add("");

        // Adapter spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listOriginDestination);
        //set adapter to spinner
        origin.setAdapter(adapter);
        destination.setAdapter(adapter);

        // dni suggest
        Drawable errorIcon = getResources().getDrawable(R.drawable.ic_menu_info_details);
        errorIcon.setBounds(new Rect(0, 0, 50, 50));
        dni.setError("RUT sin guion ni Digito Verificador",errorIcon);
        dni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Drawable errorIcon = getResources().getDrawable(R.drawable.ic_menu_info_details);
                errorIcon.setBounds(new Rect(0, 0, 50, 50));
                if(s.length()==0)
                    dni.setError("RUT sin guion ni Digito Verificador",errorIcon);

            }
        });


        // Listeners of combobox
        origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_origin = origin.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_destination = destination.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Finally listener of button
        save_manual_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.vibrate(100);
                if (ticket_no.getText().toString().isEmpty()) {
                    ticket_no.setError("Falta ingresar Número de Boleta");
                    ticket_no.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(ticket_no, InputMethodManager.SHOW_IMPLICIT);
                }
                if (dni.getText().toString().isEmpty()) {
                    dni.setError("Falta ingresar DNI");
                    dni.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(dni, InputMethodManager.SHOW_IMPLICIT);
                }
                if (dni.getText().toString().contains("-")) {
                    dni.setError("Ingrese DNI sin digito verificador");
                    dni.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(dni, InputMethodManager.SHOW_IMPLICIT);
                }
                if (name.getText().toString().isEmpty()) {
                    name.setError("Falta ingresar Nombre");
                    name.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
                }
                if (!origin.getSelectedItem().toString().trim().equals(destination.getSelectedItem().toString().trim())){
                    if (!name.getText().toString().isEmpty() && !dni.getText().toString().contains("-") && !dni.getText().toString().isEmpty() && !ticket_no.getText().toString().isEmpty()) {
                        String origin_mongo_id = db.selectFirst("select id_mongo from ports where name='" + origin.getSelectedItem().toString().trim() + "'");
                        String destination_mongo_id = db.selectFirst("select id_mongo from ports where name='" + destination.getSelectedItem().toString().trim() + "'");
                        String port = db.selectFirst("select id_api from ports where name='" + selected_origin + "'");
                        String dniStr = dni.getText().toString().toUpperCase();
                        db.insert("insert into people(document,name) values('" + dniStr + "','" + name.getText().toString().toUpperCase() + "')");
                        Cursor cursor = db.select("select origin,destination from manifest WHERE id_people='" + dni.getText().toString() + "'");
                        if (cursor.getCount() > 0) { //when person is in manifest with origin and destination, only insert in case that one or another is different to origin/destination inserted
                            if (!cursor.getString(0).equals(origin_mongo_id) || !cursor.getString(1).equals(destination_mongo_id)) {
                                db.insert("insert into manifest(id_people,origin,destination,port,boletus,is_inside,is_manual_sell) values('" + dni.getText().toString().toUpperCase() + "','" + origin_mongo_id + "','" + destination_mongo_id + "','" + port + "','" + Integer.parseInt(ticket_no.getText().toString()) + "','" + 1 + "','" + 1 + "')");
                                Toast.makeText(getApplicationContext(), "Persona Registrada Correctamente", Toast.LENGTH_SHORT).show();
                            } else if (cursor.getString(0).equals(origin_mongo_id) || cursor.getString(1).equals(destination_mongo_id))
                                Toast.makeText(getApplicationContext(), "Persona ya se encuentra en Manifiesto", Toast.LENGTH_LONG).show();
                        } else if (cursor.getCount() == 0) {
                            db.insert("insert into manifest(id_people,origin,destination,port,boletus,is_inside,is_manual_sell) values('" + dni.getText().toString().toUpperCase() + "','" + origin_mongo_id + "','" + destination_mongo_id + "','" + port + "','" + Integer.parseInt(ticket_no.getText().toString()) + "','" + 1 + "','" + 1 + "')");
                            Toast.makeText(getApplicationContext(), "Persona Registrada Correctamente", Toast.LENGTH_SHORT).show();
                        }


                        if (cursor != null)
                            cursor.close();

                        Record record = new Record();
                        record.setDatetime(getCurrentDateTime("yyy-MM-dd HH:mm:ss.S"));
                        record.setPerson_document(dni.getText().toString().toUpperCase());
                        record.setPerson_name(name.getText().toString().toUpperCase());
                        record.setOrigin(origin_mongo_id);
                        record.setDestination(destination_mongo_id);
                        record.setTicket(Integer.parseInt(ticket_no.getText().toString()));
                        record.setPermitted(0);
                        record.setMongo_id_manifest(db.selectFirst("select id_mongo from routes where id=(select route_id from config)"));
                        db.add_record(record);
                        finish();
                        //new RegisterTask(record).execute();
                    } else
                        Toast.makeText(getApplicationContext(), "Verifique los campos solicitados e intente nuevamente", Toast.LENGTH_SHORT).show();
            }
                else {
                    TextView errorTextorigin = (TextView)origin.getSelectedView();
                    TextView errorTextdestination = (TextView)destination.getSelectedView();
                    errorTextorigin.setError("anything here, just to add the icon");
                    errorTextorigin.setTextColor(Color.RED);//just to highlight that this is an error
                    errorTextdestination.setError("anything here, just to add the icon");
                    errorTextdestination.setTextColor(Color.RED);//just to highlight that this is an error
                    Toast.makeText(getApplicationContext(),"Porfavor Seleccione origen/destino diferentes",Toast.LENGTH_LONG).show();
                    //errorText.setText("Seleccione origen y destino diferentes");//changes the selected item text to th
                }
            }
        });
        if (getOriginandDestination != null)
            getOriginandDestination.close();

    }

    public String getCurrentDateTime(String format) {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat(format);
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

}
