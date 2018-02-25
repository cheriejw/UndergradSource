package com.sourcey.cheriejw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import gms.drive.*;

import com.sourcey.cheriejw.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


//displays all matches with image and name link to p

public class Matches extends AppCompatActivity {

    // ** Check if user has no saved match then create a link to change the matches.

    private static final String TAG = "MatchesActivity";
    private ArrayList<String> results = new ArrayList<String>(); //some really long string
    @Bind(R.id.btn_matches)
    Button _matchButton;


//    profilePicBox = (ImageView)findViewById(R.id.proPic);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        _matchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new updateLocation().execute(); //send to server
                //should get the matches back and display
            }
        });
    }

    UserLocalStore instance = new UserLocalStore(this);
    String username = instance.getUserName();
    Slider seeking = instance.getSSliders();
    LocListener location = new LocListener();

    double longitude = location.getLon();
    double latitude = location.getLat();
    String locStatus = "";

    int pGenderMin = seeking.sGenMin;
    int pGenderMax = seeking.sGenMax;
    int pExpressionMin = seeking.sExMin;
    int pExpressionMax = seeking.sExMax;
    int pOrientationMin = seeking.sOriMin;
    int pOrientationMax = seeking.sOriMax;

    // for locations
    public class LocListener implements LocationListener
    {
        private double lat =0.0;
        private double lon = 0.0;

        public double getLat()
        {
            return lat;
        }

        public double getLon()
        {
            return lon;
        }

        @Override
        public void onLocationChanged(Location location)
        {
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.d(TAG, "latitude is : " + lat + " and longitude is : " + lon);
        }

        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    // end location data

    class updateLocation extends AsyncTask<Void, Void, Void>{ //passing stuff in params
        @Override
        protected Void doInBackground(Void... voids) {
            Socket sock;
            PrintWriter out;
            BufferedReader in;

            try{
                //sock = new Socket("108.23.32.15", 4910); //to James server
                sock = new Socket("10.0.2.2", 4910); //local host

                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String command = "updateLocation ," + username + longitude + latitude;
                out.println(command);

                locStatus = in.readLine();
                Log.d("RESPONSE", locStatus);
                /*while (in.ready()) {
                    response = in.readLine();
                    Log.i("SOCKET",response);
                    usernameStatus = response; //should be either exists or free
                }*/
                sock.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new getMatches().execute();
        }
    }


    class getMatches extends AsyncTask<Void, Void, Void> { //passing stuff in params
        @Override
        protected Void doInBackground(Void... voids) {
            Socket sock;
            PrintWriter out;
            BufferedReader in;

            try{
                sock = new Socket("108.23.32.15", 4910); //to yames server

                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String command = "getMatches, " + username + ", " + longitude + ", " + latitude +
                        ", " + pGenderMin + ", " + pGenderMax +  ", " + pExpressionMin + ", " +
                        pExpressionMax + ", " + pOrientationMin + ", " + pOrientationMax;
                Log.d("command value: ", command);

                String testcommand = "getMatches, user2138, 33.500, -118.531, 0, 20, 0, 20, 0, 20";
                Log.d("testcommand value: ", testcommand);

                String response;

//                String[] temp = new String[5];
                int counter = 0;
                while (in.ready()) {
                    response = in.readLine(); //one line - one user
                    Log.d("response value: ", response);
                    //username, pgender, pexpression, porientation, miles distance
                    //array separate by ", "
//                    temp = response.split("\\s*, \\s*");
                    results.add(response); //adding all the user matches into response
                }
                sock.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            return null;

        }
    }

//    class getPic extends AsyncTask<Void,Void,Bitmap> {
//        Socket sock;
//        DataInputStream din;
//        DataOutputStream dout;
//        String command = "getPic, " + userName;
//        Bitmap bitMap;
//
//        protected Bitmap doInBackground(Void... args) {
//            try {
//                Log.i("SOCKET", "created Socket");
//                sock = new Socket("108.23.32.15", 6066);
//                //sock = new Socket("10.0.2.2", 6066);
//                din = new DataInputStream(sock.getInputStream());
//                dout = new DataOutputStream(sock.getOutputStream());
//
//                dout.writeUTF("getPic, " + userName);
//                Log.i("SOCKET", "send command: " + "getPic, " + userName);
//                String returned = din.readUTF();
//                Log.i("IMAGE",returned);
//                int size = Integer.valueOf(returned);
//
//                bitMap = BitmapFactory.decodeStream(din);
//
//                /*if(size != 0) {
//                    byte[] bytes = new byte[size];
//                    din.read(bytes);
//                    bitMap = BitmapFactory.decodeByteArray(bytes,0,size);
//                    if(bitMap == null)
//                        Log.i("IMAGE", "Image is null");
//                } else {
//                    Log.i("IMAGE", "Well, shit");
//                }*/
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (sock != null) {
//                        sock.close();
//                        din.close();
//                        dout.close();
//                    }
//
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//            return bitMap;
//        }
//
//        protected void onPostExecute(Bitmap bitMap){
//
//            profilePicBox.setImageBitmap(bitMap);
//        }
//    }

    private static String getFrom(String str, String value){
        //username, pgender, pexpression, porientation, miles distance
        //array separate by ", "
        String[] temp = new String[5];
        temp = str.split("\\s*, \\s*");
        switch (value) {
            case "username":
                return temp [0];
            case "pgender":
                return temp [1];
            case "pexpression":
                return temp [2];
            case "porientation":
                return temp [3];
            case "miles":
                return temp [4];
            default:
                return null; //if nothing
        } //first item in array is username
    }
}
