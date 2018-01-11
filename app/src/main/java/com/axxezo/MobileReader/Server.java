package com.axxezo.MobileReader;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class Server implements Runnable {

    //ServerSocket serverSocket;
    String SERVERIP = getIPAddress(true);
    ServerSocket serverSocket;
    int SERVERPORT = 8080;
    String line;
    String recivedMsg;
    Boolean connected = true;
    Boolean alive = true;
    BufferedReader in;
    Context AppContext;

    //Constructor
    public Server(Context context){
        this.AppContext=context;
    }
    final Handler handler = new Handler();
    public void run() {
        Log.d("Server",SERVERIP);
        try {
            if (SERVERIP != null) {

                //DatabaseHelper db = new DatabaseHelper(AppContext);//Db access

                serverSocket = new ServerSocket(SERVERPORT);//Open port.
                while (alive) {
                    //Wait for incoming clients
                    Socket client = serverSocket.accept();

                    Log.d("Server", "Connected");//Connected to client.
                    connected=true;

                    try {
                        line = null;
                        while (connected) {
                            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            if ((line = in.readLine())!=null) {
                                //LogApp.d("Server", line);

                                recivedMsg = line;
                                //if(recivedMsg!="200")

                                JSONObject json_received = new JSONObject(line);
                                //db.Asyntask_insertNewPeopleManifest(json_received.get("document").toString(),Integer.parseInt(json_received.get("input").toString()));
                                //db.updatePeopleManifest(json_received.get("document").toString(),json_received.getInt("status"));
                                //Client client1 = new Client("200", SERVERIP, SERVERPORT);
                                //client1.run();
                                in.close();
                                client.close();
                                connected=false;
                            } else {
                                recivedMsg = "empty";
                                in.close();
                                client.close();
                                connected=false;
                            }
                        }
                    } catch (Exception e) {
                        Log.d("Server","Connection interrupted");
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d("Server","No IP");
            }
        } catch (Exception e) {
            Log.d("Server","error "+ e);
            e.printStackTrace();
        }
    }

    /*******Stop Server*******/
    public void ServerStop(){

        connected=false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*******Kill Server*******/
    public void ServerKill(){

        alive = false;
        connected=false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Get IP Address*/
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); //Drop ip6 zone suffix, just in case.....
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("ServerGetIP","error "+ e);
            e.printStackTrace();
        }
        return "";
    }
}