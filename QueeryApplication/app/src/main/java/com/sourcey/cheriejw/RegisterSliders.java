package com.sourcey.cheriejw;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.content.*;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.Integer;
import android.graphics.drawable.ColorDrawable;
import org.florescu.android.rangeseekbar.RangeSeekBar;

public class RegisterSliders extends AppCompatActivity {

    //3 Seekbar Objects for the Personal Slider
    RangeSeekBar pGenderx, pExpressionx, pOrientationx;

    //3 RangeSeekBars for the Seeking Slider
    RangeSeekBar sGender, sExpression, sOrientation;

    //Button saves and registers values.
    Button button;

    //Displays values.
    private int pGenderValue, pExpressionValue, pOrientationValue;
    private int sGenderValueMax, sExpressionValueMax, sOrientationValueMax,
            sGenderValueMin, sExpressionValueMin, sOrientationValueMin;

    //Validate that information is being read.
    boolean pGood, sGood = false;

    /**
     *
     * This section provides the important aspect of the program. Here, we first initialize
     * our global variables to their designated ids depending on what we called them in our XML
     * section. For example, if we called a button bLogout, we set whatever Button type we set, in
     * this case, "Button button" and cast it.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sliders);

        /*Initialize values*/

        //Seekbar represents our sliders.
        pGenderx = (RangeSeekBar) findViewById(R.id.pGenderBar);
        pExpressionx = (RangeSeekBar) findViewById(R.id.pExpressionBar);
        pOrientationx = (RangeSeekBar) findViewById(R.id.pOrientationBar);
        sGender = (RangeSeekBar) findViewById(R.id.sGenderBar);
        sExpression = (RangeSeekBar) findViewById(R.id.sExpressionBar);
        sOrientation = (RangeSeekBar) findViewById(R.id.sOrientationBar);

        //Button to save updates and values.
        button = (Button) findViewById(R.id.bSaveRegister);

        //Default values in case of non-movement by user.
        pGenderValue = 10; pExpressionValue = 10; pOrientationValue = 10;
        sGenderValueMax = 20; sGenderValueMin = 1; sExpressionValueMax = 20;
        sExpressionValueMin = 1; sOrientationValueMax = 20; sOrientationValueMin = 1;

        //Default values for personal Sliders.
        pGenderx.setSelectedMaxValue(10);
        pExpressionx.setSelectedMaxValue(10);
        pOrientationx.setSelectedMaxValue(10);

        //Default color for RangeSeekbar
        pGenderx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        pExpressionx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        pOrientationx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        sGender.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        sExpression.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        sOrientation.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);

        //Directs to main method for calculating values.
        main();

        //Click Listener
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Debug
                String tStr = "personal Values are " + pGenderValue + " " + pExpressionValue + " "
                        + pOrientationValue;
                Log.d("VALUES",tStr);

                //Save storage in UserLocalStore
                Slider newPSlider = new Slider(pGenderValue, pExpressionValue, pOrientationValue);
                UserLocalStore instance1 = new UserLocalStore(RegisterSliders.this);
                instance1.storePersonal(newPSlider);

                //Debug
                newPSlider = instance1.getPSliders();
                tStr = "personal Values are " + newPSlider.pGen + " " + newPSlider.pExpress + " "
                        + newPSlider.pOrient;
                Log.d("VALUES", tStr);

                //Save storage in UserLocalStore
                Slider newSSlider = new Slider(sGenderValueMin, sGenderValueMax, sExpressionValueMin,
                        sExpressionValueMax, sOrientationValueMin, sOrientationValueMax);
                UserLocalStore instance2 = new UserLocalStore(RegisterSliders.this);
                instance2.storeSeeking(newSSlider);

                //Calls in the socket class to send values to Server.
                new sendPSliders().execute();
                Log.d("VALUES", "Personal Sliders sent.");//Debug
                new sendSSliders().execute();
                Log.d("VALUES", "Seeking Sliders sent.");//Debug
            }
        });
    }

    /**
     * Main Class for registering sliders. This section contains 6 methods that provide us with
     * setting up the seekbar that represents our sliders. In the first 3, we will only focus on one
     * thumb, for personal sliders (here, a single thumb will represent the maxValue variable.)
     */

    public void main(){
        ///////The following methods contribute to the Personal Slider Values.////////

        //Personal Gender Slider: Maximum represents one Thumb.
        pGenderx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pGenderValue = (Integer) maxValue;
            }
        });

        //Personal Expression Slider: Maximum represents one Thumb.
        pExpressionx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pExpressionValue = (Integer) maxValue;
            }

        });

        //Personal Orientation Slider: Maximum represents one Thumb.
        pOrientationx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pOrientationValue = (Integer) maxValue;
            }

        });

        ///////The following methods contribute to the Seeker Slider Values.////////

        //Seeking Gender RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sGender.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sGenderValueMax = (Integer) maxValue;
                sGenderValueMin = (Integer) minValue;
            }
        });

        //Seeking Expression RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sExpression.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sExpressionValueMax = (Integer) maxValue;
                sExpressionValueMin = (Integer) minValue;
            }

        });

        //Seeking Orientation RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sOrientation.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sOrientationValueMax = (Integer) maxValue;
                sOrientationValueMin = (Integer) minValue;
            }

        });
    }

    /**
     * Client Sockets pass values to the App Server, which then transcribes it to SQL language
     * to save into the database. 2 sockets are made; one for the personal sliders, and one for
     * the seeking sliders.
     */

    class sendPSliders extends AsyncTask<Void,Void,Void> {

        PrintWriter out;
        BufferedReader in;

        protected Void doInBackground(Void... args) {
            try {
                //Socket sock = new Socket("108.23.32.15", 4910);
                Socket sock = new Socket("10.0.2.2", 4910);

                out = new PrintWriter(sock.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //Command to pass for the server to read, then translate it to SQL command
                //to store in the database, determining on the first value in the parameter.
                UserLocalStore userLocalStore = new UserLocalStore(RegisterSliders.this);
                String command = "setPSlider, " + userLocalStore.getUserName();
                Slider personal = userLocalStore.getPSliders();

                command += ", " + personal.pGen + ", " + personal.pExpress +
                        ", " + personal.pOrient;

                out.println(command);//Sends command to the server.
                Log.i("Register Command, ", command);

                //Passes each line of the command string to the app server, if it contains
                // characters.
                String temp;
                while ((temp = in.readLine()) != null) {
                    if(temp.equals("true")){
                        Log.i("true", command);
                        pGood = true;
                    } else {
                        Log.i("Error", temp);
                    }
                    Log.d("Register Command, ", temp);
                    System.out.println(temp);
                }
                sock.close();//Closes socket
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    //Client Socket for Seeking sliders will do the same as the previous socket.
    class sendSSliders extends AsyncTask<Void,Void,Void> {
        PrintWriter out;
        BufferedReader in;

        protected Void doInBackground(Void... args) {
            try {
                //Socket sock = new Socket("108.23.32.15", 4910);
                Socket sock = new Socket("10.0.2.2", 4190);
                out = new PrintWriter(sock.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                //Command to pass for the server to read, then translate it to SQL command
                //to store in the database, determining on the first value in the parameter.
                UserLocalStore userLocalStore = new UserLocalStore(RegisterSliders.this);
                String command = "setSSlider, " + userLocalStore.getUserName();
                Slider seeking = userLocalStore.getSSliders();

                command += "," + seeking.sGenMin + ", " + seeking.sGenMax +
                        ", " + seeking.sExMin + ", " + seeking.sExMax
                        + "," + seeking.sOriMin + ", " + seeking.sOriMax;

                out.println(command);//Sends command to the server.
                Log.i("Register Command", command);

                //Passes each line of the command string to the app server, if it contains
                // characters.
                String temp;
                while ((temp = in.readLine()) != null) {
                    if (temp.equals("true")) {
                        Log.d("true", command);
                        sGood = true;
                    } else {
                        Log.d("Error", temp);
                    }
                    Log.d("Register Command, ", temp);
                    System.out.println(temp);
                }
                sock.close();//closes Socket
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        //After saving information to the database, we direct to the next screen.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (sGood && pGood) {//If values succeded in saving into the database.
                //Intent object to pass values.
                Intent intent = new Intent(getApplicationContext(), Homepage.class);//RegisterProfilePic
                startActivity(intent);
            } else {//Error pops up.
                Toast.makeText(getApplicationContext(), "Register Error",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
}