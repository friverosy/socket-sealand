package com.axxezo.MobileReader;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client implements Runnable {

    String msg;
    String SERVERIP;
    int SERVERPORT;

    //Constructor
    public Client(String str, String SERVERIP, int SERVERPORT){
        this.msg = str;
        this.SERVERIP = SERVERIP;
        this.SERVERPORT = SERVERPORT;
    }
    @Override
    public void run() {

        try {
            Socket s = new Socket(SERVERIP, SERVERPORT);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            //send output msg
            String outMsg = msg + System.getProperty("line.separator");
            out.write(outMsg);
            out.flush();
            Log.d("Client", "sent: " + outMsg);
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            //LogApp.d("Client", "UnknownHost");
        } catch (IOException e) {
            e.printStackTrace();
            //LogApp.d("Client", "IO");
        }

    }
}