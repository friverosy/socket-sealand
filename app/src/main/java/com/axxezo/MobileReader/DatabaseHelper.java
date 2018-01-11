package com.axxezo.MobileReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by axxezo on 14/11/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    //context
    private Context context;
    private static DatabaseHelper sInstance;
    private SQLiteDatabase db;

    //create a unique instance of DB

    public static synchronized DatabaseHelper getInstance(Context context) {
        //one single instance of DB
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Table names
    private static final String TABLE_PEOPLE = "PEOPLE";
    private static final String TABLE_RECORDS = "RECORDS";
    private static final String TABLE_ROUTES = "ROUTES";
    private static final String TABLE_PORTS = "PORTS";
    private static final String TABLE_SHIPS = "SHIPS";
    private static final String TABLE_HOURS = "HOURS";
    private static final String TABLE_CONFIG = "CONFIG";
    private static final String TABLE_MANIFEST = "MANIFEST";

    // Manifest Table
    private static final String MANIFEST_ID = "id";
    private static final String MANIFEST_PEOPLE_ID = "id_people";
    private static final String MANIFEST_ORIGIN = "origin";
    private static final String MANIFEST_DESTINATION = "destination";
    private static final String MANIFEST_ISINSIDE = "is_inside";
    private static final String MANIFEST_PORT = "port";
    private static final String MANIFEST_BOLETUS = "boletus";
    private static final String MANIFEST_MANUAL_SELL = "is_manual_sell";
    private static final String MANIFEST_RESERVATION_STATUS = "reservation_status";
    private static final String MANIFEST_MONGO_ID = "people_mongo_id";

    // People Table
    private static final String PERSON_ID = "id";
    private static final String PERSON_MONGO_ID = "id_mongo";
    private static final String PERSON_DOCUMENT = "document";
    private static final String PERSON_NAME = "name";
    private static final String PERSON_NATIONALITY = "nationality";
    private static final String PERSON_AGE = "age";
    private static final String PERSON_REGISTER_ID = "id_register";

    // Routes Table
    private static final String ROUTE_ID = "id";
    private static final String ROUTE_NAME = "name";
    private static final String ROUTE_SAILING_DATE = "sailing_date";
    private static final String ROUTE_MONGO_ID = "id_mongo";

    // Ports Table
    private static final String PORT_ID = "id";
    private static final String PORT_ID_MONGO = "id_mongo";
    private static final String PORT_ID_API = "id_api";
    private static final String PORT_NAME = "name";
    private static final String PORT_IS_IN_MANIFEST = "is_in_manifest";

    // Transports Table
    private static final String SHIP_ID = "id";
    private static final String SHIP_NAME = "name";

    // Records Table
    private static final String RECORD_ID = "id";
    private static final String RECORD_DATETIME = "datetime";
    private static final String RECORD_PERSON_DOC = "person_document";
    private static final String RECORD_PERSON_NAME = "person_name";
    private static final String RECORD_ORIGIN = "origin";
    private static final String RECORD_DESTINATION = "destination";
    private static final String RECORD_PORT_REGISTRY = "port_registry";
    private static final String RECORD_IS_INPUT = "input";
    private static final String RECORD_SYNC = "sync";
    private static final String RECORD_IS_PERMITTED = "permitted";
    private static final String RECORD_TICKET = "ticket";
    private static final String RECORD_REASON = "reason";
    private static final String RECORD_MONGO_ID_MANIFEST = "mongo_id_manifest";
    private static final String RECORD_MONGO_ID_REGISTER = "mongo_id_register";

    // Hours Table
    private static final String HOUR_ID = "id";
    private static final String HOUR_NAME = "name";

    // Config Table
    private static final String CONFIG_ROUTE_ID = "route_id";
    private static final String CONFIG_ROUTE_NAME = "route_name";
    private static final String CONFIG_DATE = "date";
    private static final String CONFIG_MANIFEST_ID = "manifest_id";
    private static final String CONFIG_DATE_LAST_UPDATE = "date_last_update";


    // Set table colums
    private static final String[] PEOPLE_COLUMS = {PERSON_ID, PERSON_DOCUMENT, PERSON_NAME, PERSON_NATIONALITY, PERSON_AGE, PERSON_REGISTER_ID};
    private static final String[] RECORDS_COLUMNS = {RECORD_ID, RECORD_DATETIME, RECORD_PERSON_DOC, PERSON_MONGO_ID, RECORD_PERSON_NAME, RECORD_ORIGIN, RECORD_DESTINATION, RECORD_PORT_REGISTRY, RECORD_IS_INPUT, RECORD_SYNC, RECORD_IS_PERMITTED, RECORD_TICKET, RECORD_REASON, RECORD_MONGO_ID_MANIFEST, RECORD_MONGO_ID_REGISTER};
    private static final String[] MANIFEST_COLUMNS = {MANIFEST_ID, MANIFEST_PEOPLE_ID, MANIFEST_ORIGIN, MANIFEST_DESTINATION, MANIFEST_ISINSIDE};
    private static final String[] ROUTES_COLUMNS = {ROUTE_ID, ROUTE_NAME,ROUTE_MONGO_ID};
    private static final String[] PORTS_COLUMNS = {PORT_ID, PORT_NAME};
    private static final String[] TRANSPORTS_COLUMNS = {SHIP_ID, SHIP_NAME};
    private static final String[] HOURS_COLUMNS = {HOUR_ID, HOUR_NAME};
    private static final String[] CONFIG_COLUMNS = {CONFIG_ROUTE_ID, CONFIG_ROUTE_NAME, CONFIG_DATE, CONFIG_MANIFEST_ID};

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NavieraAustral";

    // SQL statement to create the differents tables
    private String CREATE_PEOPLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PEOPLE + " ( " +
            PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PERSON_DOCUMENT + " TEXT NOT NULL, " +
            PERSON_MONGO_ID + " TEXT, " +
            PERSON_NAME + " TEXT, " +
            PERSON_NATIONALITY + " TEXT, " +
            PERSON_AGE + " INTEGER, " +
            PERSON_REGISTER_ID + " TEXT);";

    // "CONSTRAINT "+PERSON_DOCUMENT+" UNIQUE ("+PERSON_DOCUMENT+")); ";

    private String CREATE_MANIFEST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MANIFEST + " ( " +
            MANIFEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MANIFEST_PEOPLE_ID + " TEXT NOT NULL, " +
            MANIFEST_MONGO_ID + " TEXT DEFAULT 0, " +
            MANIFEST_ORIGIN + " TEXT, " +
            MANIFEST_DESTINATION + " TEXT," +
            MANIFEST_ISINSIDE + " INTEGER DEFAULT 0, " +
            MANIFEST_PORT + " INTEGER, " +
            MANIFEST_BOLETUS + " TEXT, " +
            MANIFEST_MANUAL_SELL + " INTEGER DEFAULT 0, " +
            MANIFEST_RESERVATION_STATUS + " INTEGER DEFAULT 1 );";

    private String CREATE_ROUTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTES + " ( " +
            ROUTE_ID + " INTEGER PRIMARY KEY, " +
            ROUTE_NAME + " TEXT, " +
            ROUTE_SAILING_DATE + " TEXT," +
            ROUTE_MONGO_ID + " TEXT);";

    private String CREATE_PORTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PORTS + " ( " +
            PORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PORT_ID_API + " INTEGER, " +
            PORT_ID_MONGO + " TEXT, " +
            PORT_NAME + " TEXT); ";

    private String CREATE_TRANSPORTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SHIPS + " ( " +
            SHIP_ID + " INTEGER PRIMARY KEY, " +
            SHIP_NAME + " TEXT);";

    private String CREATE_RECORDS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_RECORDS + " ( " +
            RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RECORD_DATETIME + " TEXT, " +
            RECORD_PERSON_DOC + " INTEGER, " +
            PERSON_MONGO_ID + " TEXT, " +
            RECORD_PERSON_NAME + " TEXT, " +
            RECORD_ORIGIN + " INTEGER, " +
            RECORD_DESTINATION + " INTEGER, " +
            RECORD_PORT_REGISTRY + " TEXT, " +
            RECORD_IS_INPUT + " INTEGER, " +  //input of switch in main 1 embark, 2 landed
            RECORD_SYNC + " INTEGER, " +
            RECORD_IS_PERMITTED + " INTEGER," +
            RECORD_TICKET + " TEXT, " +
            RECORD_MONGO_ID_MANIFEST + " TEXT, " +
            RECORD_REASON + " INTEGER, " +
            RECORD_MONGO_ID_REGISTER + " TEXT); ";

    private String CREATE_HOURS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HOURS + " ( " +
            HOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HOUR_NAME + " TEXT);";

    private String CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIG + " ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CONFIG_ROUTE_ID + " INTEGER, " +
            CONFIG_ROUTE_NAME + " INTEGER, " +
            CONFIG_DATE + " TEXT, " +
            CONFIG_MANIFEST_ID + " TEXT, " +
            CONFIG_DATE_LAST_UPDATE + " TEXT); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void DatabaseHelper() {
        db.enableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //first create the tables
        db.execSQL(CREATE_PEOPLE_TABLE);
        db.execSQL(CREATE_ROUTES_TABLE);
        db.execSQL(CREATE_PORTS_TABLE);
        db.execSQL(CREATE_TRANSPORTS_TABLE);
        db.execSQL(CREATE_RECORDS_TABLE);
        db.execSQL(CREATE_HOURS_TABLE);
        db.execSQL(CREATE_CONFIG_TABLE);
        db.execSQL(CREATE_MANIFEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if it existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        //create fresh tables
        this.onCreate(db);
    }

    /**
     * CRUD operations (create "add", read "get", update, delete)
     */

    public void insertJSON(String json, String table) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        Slack slack=new Slack(context);
        JSONObject objectJson;
        JSONArray jsonArray;
        switch (table) {
            case "routes":
                if (json.isEmpty() || json.equals("[]"))
                    db.execSQL("delete from routes");
                if (!json.isEmpty() && json.length() > 3) {
                    jsonArray = new JSONArray(json);
                    try {
                        db.beginTransactionNonExclusive();
                        db.delete(TABLE_ROUTES, null, null);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ContentValues values = new ContentValues();
                            if (jsonArray.getJSONObject(i).getBoolean("active")) {
                                Routes routes = new Routes(jsonArray.getJSONObject(i).getInt("refId"),
                                        jsonArray.getJSONObject(i).getString("name").toUpperCase(),
                                        jsonArray.getJSONObject(i).getString("depart"),
                                        jsonArray.getJSONObject(i).getString("_id"));
                                values.put(ROUTE_ID, routes.getID());
                                values.put(ROUTE_NAME, routes.getName().trim());
                                values.put(ROUTE_SAILING_DATE, routes.getSailing_date());
                                values.put(ROUTE_MONGO_ID, routes.getId_mongo());
                                db.insert(TABLE_ROUTES, // table
                                        null, //nullColumnHack
                                        values); // key/value -> keys = column names/ values = column values
                            }
                        }
                        db.setTransactionSuccessful();
                    } catch (android.database.SQLException e) {
                        slack.sendMessage("cannot insert routes",e.getMessage() + "\nDatabaseHelper Line: " +
                                new Throwable().getStackTrace()[0].getLineNumber());
                    } finally {
                        db.endTransaction();
                    }

                } /*else
                    Log.i("json content", json);*/
                break;
            case "manifest":
                if (!json.isEmpty() && json.length() > 3) {
                    jsonArray = new JSONArray(json);
                    try {
                        db.beginTransactionNonExclusive();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                People people = new People(jsonArray.getJSONObject(i).getString("documentId").trim(), jsonArray.getJSONObject(i).getString("name").toUpperCase(), " ", 0, jsonArray.getJSONObject(i).getString("personId"), jsonArray.getJSONObject(i).getString("registerId"));
                                navieraManifest manifest = new navieraManifest(jsonArray.getJSONObject(i).getString("documentId"),jsonArray.getJSONObject(i).getString("personId"), jsonArray.getJSONObject(i).getString("origin"), jsonArray.getJSONObject(i).getString("destination"), 0, jsonArray.getJSONObject(i).getBoolean("isOnboard") ? 1 : 0, jsonArray.getJSONObject(i).getInt("reservationStatus"));
                                String doc;
                                doc = people.getDocument().toUpperCase();
                                if (people.getDocument().contains("-"))
                                    doc = doc.substring(0, doc.length() - 2);
                                String name = removeAccent(people.getName().toUpperCase());
                                Cursor cursor = null;
                                cursor = db.rawQuery("select origin,destination from manifest WHERE id_people='" + doc + "'", null);
                                if (cursor.getCount() > 0) { //when person is in manifest with origin and destination, only insert in case that one or another is different to origin/destination inserted
                                    cursor.moveToFirst();
                                    if (!cursor.getString(0).equals(manifest.getOrigin()) && !cursor.getString(1).equals(manifest.getDestination())) {
                                        db.execSQL("insert  into manifest(" + MANIFEST_PEOPLE_ID + "," + MANIFEST_MONGO_ID + "," + MANIFEST_ORIGIN + "," + MANIFEST_DESTINATION + "," + MANIFEST_ISINSIDE + "," + MANIFEST_MANUAL_SELL +","+MANIFEST_RESERVATION_STATUS+ ") VALUES('" +
                                                doc + "','" + manifest.getManifest_people_id() + "','" + manifest.getOrigin() + "','" + manifest.getDestination() + "','" + manifest.getIsInside() + "','" + manifest.getIsManualSell() + "','" + manifest.getReservationStatus() + "')");
                                        db.execSQL("insert or ignore into people(" + PERSON_DOCUMENT + "," + PERSON_MONGO_ID + "," + PERSON_NAME + "," + PERSON_NATIONALITY + "," + PERSON_AGE + "," + PERSON_REGISTER_ID + ") VALUES ('" +
                                                doc + "','" + people.getMongo_documentID() + "','" + name + "','" + people.getNationality().toUpperCase() + "'," + people.getAge() + ",'" + people.getMongo_registerID() + "')");
                                    } else if (manifest.getReservationStatus() == -1) {
                                        db.execSQL("delete from manifest where id_people='" + doc + "'");
                                    }
                                    /*else if(cursor.getString(0).equals(manifest.getOrigin()) && cursor.getString(1).equals(manifest.getDestination())) {
                                        db.execSQL("update people set id='"+people.getMongo_documentID()+"'");
                                        db.execSQL("update people set mongo_id='"+people.getMongo_documentID()+"'");
                                    }*/
                                } else if (cursor.getCount() == 0 && manifest.getReservationStatus() != -1) {
                                    db.execSQL("insert into manifest(" + MANIFEST_PEOPLE_ID + "," + MANIFEST_MONGO_ID + "," + MANIFEST_ORIGIN + "," + MANIFEST_DESTINATION + "," + MANIFEST_ISINSIDE + "," + MANIFEST_MANUAL_SELL +","+MANIFEST_RESERVATION_STATUS+ ") VALUES('" +
                                            doc + "','" + manifest.getManifest_people_id()+ "','" +  manifest.getOrigin() + "','" + manifest.getDestination() + "','" + manifest.getIsInside() + "','" + manifest.getIsManualSell() + "','" + manifest.getReservationStatus() + "')");
                                    db.execSQL("insert or ignore into people(" + PERSON_DOCUMENT + "," + PERSON_MONGO_ID + "," + PERSON_NAME + "," + PERSON_NATIONALITY + "," + PERSON_AGE + "," + PERSON_REGISTER_ID + ") VALUES ('" +
                                            doc + "','" + people.getMongo_documentID() + "','" + name + "','" + people.getNationality().toUpperCase() + "'," + people.getAge() + ",'" + people.getMongo_registerID() + "')");
                                }
                                if (cursor != null)
                                    cursor.close();
                            } catch (Exception e) {
                                slack.sendMessage("ERROR",e.getMessage() + "\nDatabaseHelper Line: " +
                                        new Throwable().getStackTrace()[0].getLineNumber());
                            }
                        }
                        // finnaly insert fill config table
                        db.setTransactionSuccessful();
                    } catch (android.database.SQLException e) {
                        e.printStackTrace();
                        slack.sendMessage("cannot insert manifest",e.getMessage() + "\nDatabaseHelper Line: " +
                                new Throwable().getStackTrace()[0].getLineNumber());
                    } finally {
                        db.endTransaction();
                    }
                } /*else
                    Log.i("error", "Json empty!");*/
                break;
            case "ports":
                if (!json.isEmpty() && json.length() > 3) {
                    jsonArray = new JSONArray(json);
                    try {
                        db.beginTransactionNonExclusive();
                        db.execSQL("delete from ports");
                        ContentValues values = new ContentValues();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                Ports port = new Ports(jsonArray.getJSONObject(i).getString("_id"),
                                        jsonArray.getJSONObject(i).getInt("locationId"),
                                        jsonArray.getJSONObject(i).getString("locationName"));
                                values.put(PORT_ID_MONGO, port.getId_mongo());
                                values.put(PORT_ID_API, port.getId_api());
                                values.put(PORT_NAME, port.getName().trim().toUpperCase());
                                db.insert(TABLE_PORTS, // table
                                        null, //nullColumnHack
                                        values); // key/value -> keys = column names/ values = column values
                            } catch (Exception e) {
                                slack.sendMessage("ERROR",e.getMessage() + "\nLine: " +
                                        new Throwable().getStackTrace()[0].getLineNumber());
                            }
                            values.clear();
                        }
                        db.setTransactionSuccessful();
                    } catch (android.database.SQLException e) {
                        slack.sendMessage("cannot insert ports ",e.getMessage() + "\nDatabaseHelper Line: " +
                                new Throwable().getStackTrace()[0].getLineNumber());
                    } finally {
                        db.endTransaction();
                    }
                } /*else
                    Log.i("error", "Json empty!");*/
                break;
        }
    }

    public String selectFirst(String Query) {
        String firstElement = "";
        Slack slack=new Slack(context);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            db.beginTransactionNonExclusive();
            cursor = db.rawQuery(Query, null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0)
                return Query = "";
            else
                firstElement = cursor.getString(0);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot execute query",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            db.endTransaction();
        }
        if (cursor != null) {
            cursor.close();
        }
        return firstElement;
    }

    public String removeAccent(String str) {
        String texto = Normalizer.normalize(str, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        texto = texto.replaceAll("[|?*<\":>+\\[\\]/'`¨´]", "");
        return texto;
    }

    public Cursor validatePerson(String rut) {
        //return the person data if this person is in manifest table
        SQLiteDatabase db = this.getWritableDatabase();
        Slack slack =new Slack(context);
        Cursor cursor = null;
        String row = "";
        try {
            cursor = db.rawQuery("select m.id_people,p.name,m.origin,m.destination," +
                    "m.boletus,p.id_mongo," + PERSON_REGISTER_ID +
                    " from manifest as m left join people as p on m.id_people=p.document where m.id_people='" + rut + "'", null);
            cursor.moveToFirst();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot validate person",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        }
        return cursor;
    }

    public void add_record(Record record) {
        Slack slack=new Slack(context);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(RECORD_ID, record.getId());
        values.put(RECORD_PERSON_DOC, record.getPerson_document());
        values.put(PERSON_MONGO_ID, record.getMongo_id_person());
        values.put(RECORD_PERSON_NAME, record.getPerson_name());
        values.put(RECORD_ORIGIN, record.getOrigin());
        values.put(RECORD_DESTINATION, record.getDestination());
        values.put(RECORD_PORT_REGISTRY, record.getPort_registry());
        values.put(RECORD_IS_INPUT, record.getInput());
        values.put(RECORD_SYNC, record.getSync());
        values.put(RECORD_DATETIME, record.getDatetime());
        values.put(RECORD_IS_PERMITTED, record.getPermitted());
        values.put(RECORD_TICKET, record.getTicket());
        values.put(RECORD_REASON, record.getReason());
        values.put(RECORD_MONGO_ID_MANIFEST, record.getMongo_id_manifest());
        values.put(RECORD_MONGO_ID_REGISTER, record.getMongo_id_register());

        try {
            db.beginTransactionNonExclusive();
            db.insert(TABLE_RECORDS, null, values);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot insert records in db",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        }
        finally {
            db.endTransaction();
            //updatePeopleManifest(record.getPerson_document(), record.getInput());
        }
    }

    public List<Record> get_desynchronized_records() {
        Slack slack=new Slack(context);
        Cursor cursor = null;
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor =
                    db.query(TABLE_RECORDS, // a. table
                            RECORDS_COLUMNS, // b. column names
                            RECORD_SYNC + "=0", // c. selections
                            null, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex(RECORD_ID)));
                record.setDatetime(cursor.getString(cursor.getColumnIndex(RECORD_DATETIME)));
                record.setPerson_document(cursor.getString(cursor.getColumnIndex(RECORD_PERSON_DOC)));
                record.setMongo_id_person(cursor.getString(cursor.getColumnIndex(PERSON_MONGO_ID)));
                record.setPerson_name(cursor.getString(cursor.getColumnIndex(RECORD_PERSON_NAME)));
                record.setOrigin(cursor.getString(cursor.getColumnIndex(RECORD_ORIGIN)));
                record.setDestination(cursor.getString(cursor.getColumnIndex(RECORD_DESTINATION)));
                record.setPort_registry(cursor.getString(cursor.getColumnIndex(RECORD_PORT_REGISTRY)));
                record.setInput(cursor.getInt(cursor.getColumnIndex(RECORD_IS_INPUT)));
                record.setSync(cursor.getInt(cursor.getColumnIndex(RECORD_SYNC)));
                record.setPermitted(cursor.getInt(cursor.getColumnIndex(RECORD_IS_PERMITTED)));
                record.setTicket(cursor.getInt(cursor.getColumnIndex(RECORD_TICKET)));
                record.setReason(cursor.getInt(cursor.getColumnIndex(RECORD_REASON)));
                record.setMongo_id_manifest(cursor.getString(cursor.getColumnIndex(RECORD_MONGO_ID_MANIFEST)));
                record.setMongo_id_register(cursor.getString(cursor.getColumnIndex(RECORD_MONGO_ID_REGISTER)));

                records.add(record);
                cursor.moveToNext();
            }
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            slack.sendMessage("cannot obtain desync registers",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return records;
    }

    public void update_record(int id) {
        Slack slack=new Slack(context);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int i = 0;
        try {
            db.beginTransactionNonExclusive();
            values.put(RECORD_SYNC, 1);

            i = db.update(TABLE_RECORDS, //table
                    values, // column/value
                    RECORD_ID + "=" + id, // where
                    null);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot update record",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            db.endTransaction();
        }
    }

    public void updatePeopleManifest(String rut, String origin, String destination, int input) {
        SQLiteDatabase db = this.getWritableDatabase();
        Slack slack=new Slack(context);
        try {
            db.beginTransactionNonExclusive();
            db.execSQL("update manifest set is_inside=" + input + " where id_people='" + rut +
                    "' and origin='" + origin + "' and destination='" + destination + "'");
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot update people",e.getMessage() + "\nDatabaseHelper Line: "
                    + new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            db.endTransaction();
        }
    }

    public Cursor select(String select) {
        SQLiteDatabase db = this.getWritableDatabase();
        Slack slack= new Slack(context);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(select, null);
            if (cursor != null && cursor.getCount() > 0)
                cursor.moveToFirst();
            //cursor.moveToFirst();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot execute query",e.getMessage() + "\nDatabaseHelper Line: "
                    + new Throwable().getStackTrace()[0].getLineNumber());
        }
        return cursor;
    }

    public void insert(String insert) {
        SQLiteDatabase db = this.getWritableDatabase();
        Slack slack=new Slack(context);
        try {
            db.beginTransactionNonExclusive();
            db.execSQL(insert);
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("ERROR",e.getMessage() + "\nDatabaseHelper Line: "
                    + new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            db.endTransaction();
        }

    }

    public ArrayList<String> selectAsList(String qry, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        Slack slack=new Slack(context);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(qry, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                list.add(cursor.getString(position));
            }
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot convert to ArrayList",e.getMessage() + "\nDatabaseHelper Line: "
                    + new Throwable().getStackTrace()[0].getLineNumber());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public int record_desync_count() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + RECORD_ID + " FROM " + TABLE_RECORDS +
                " WHERE " + RECORD_SYNC + "=0;", null);
        int count = cursor.getCount();
        if (cursor != null)
            cursor.close();
        return count;
    }


    public void updateConfig(String route) {
        SQLiteDatabase db = this.getWritableDatabase();
        Slack slack=new Slack(context);
        try {
            db.beginTransactionNonExclusive();
            db.execSQL("update config set route_id=" + route + " where id=1");
            db.setTransactionSuccessful();
        } catch (android.database.SQLException e) {
            slack.sendMessage("cannot update route_id",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            db.endTransaction();
        }
    }
    public ArrayList<Routes> getRoutes(){
        ArrayList<Routes> routesList=new ArrayList<Routes>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=null;
        Slack slack=new Slack(context);
        try {
            routesList.add(new Routes(0, "<ELIJA UNA RUTA>"));
            cursor =
                    db.query(TABLE_ROUTES, // a. table
                            ROUTES_COLUMNS, // b. column names
                            null, // c. selections
                            null, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Routes routes = new Routes();
                routes.setID(cursor.getInt(cursor.getColumnIndex(ROUTE_ID)));
                routes.setName(cursor.getString(cursor.getColumnIndex(ROUTE_NAME)));
                routes.setId_mongo(cursor.getString(cursor.getColumnIndex(ROUTE_MONGO_ID)));
                routesList.add(routes);
                cursor.moveToNext();
            }
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            slack.sendMessage("cannot obtain desync registers",e.getMessage() + "\nDatabaseHelper Line: " +
                    new Throwable().getStackTrace()[0].getLineNumber());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return routesList;
    }
}