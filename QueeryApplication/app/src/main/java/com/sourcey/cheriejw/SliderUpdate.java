package com.sourcey.cheriejw;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Range;
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
import java.lang.Integer;
import java.lang.Number;
import java.net.UnknownHostException;
import org.florescu.android.rangeseekbar.RangeSeekBar;

public class SliderUpdate extends AppCompatActivity {//The Sliders

    //Global Variables for all values for each category.
    private int pGenderUpdateValue, pExpressionUpdateValue, pOrientationUpdateValue,
            sGenderUpdateMin, sGenderUpdateMax, sExpressionUpdateMin, sExpressionUpdateMax,
            sOrientationUpdateMin, sOrientationUpdateMax;


    //3 Sliders for the Personal Slider
    RangeSeekBar pGender, pExpression, pOrientation;

    //3 RangeSeekBars for the Seeking Slider
    RangeSeekBar sGender, sExpression, sOrientation;

    //Button will cause the clickListener to activate.
    Button UpdateButton;

    //Validate that information is being read.
    boolean pGood, sGood = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_update);

        /*Initialize values*/

        /*Range SeekBar represents all our sliders.*/
        pGender = (RangeSeekBar) findViewById(R.id.updatePersonalGender);
        pExpression = (RangeSeekBar) findViewById(R.id.updatePersonalExpress);
        pOrientation = (RangeSeekBar) findViewById(R.id.updatePersonalOrient);
        sGender = (RangeSeekBar) findViewById(R.id.updateSeekingGender);
        sExpression = (RangeSeekBar) findViewById(R.id.updateSeekingExpression);
        sOrientation = (RangeSeekBar) findViewById(R.id.updateSeekingOrientation);


        //Button to save updates and values.
        UpdateButton = (Button) findViewById(R.id.bUpdateButton);

        //Receives information from UserLocalStore.
        //Receives values saved into the UserLocalStore.
        UserLocalStore userLocalStore = new UserLocalStore(SliderUpdate.this);
        Slider personal = userLocalStore.getPSliders();
        Slider seeking = userLocalStore.getSSliders();

        //Casting int to Number object for RangeSeekBar.
        //Saves previous values into the RangeSeekBar.
        Number pGen = personal.pGen;
        Number pExpress = personal.pExpress;
        Number pOrient = personal.pOrient;
        Number sGenMax = seeking.sGenMax;
        Number sGenMin = seeking.sGenMin;
        Number sExMin = seeking.sExMin;
        Number sExMax = seeking.sExMax;
        Number sOriMin = seeking.sOriMin;
        Number sOriMax = seeking.sOriMax;

        //Sets the given rangeSeekBar to its previous value, if the user wants to change it.
        //All values are gathered from the userLocalStore.
        pGender.setSelectedMaxValue(pGen);
        pExpression.setSelectedMaxValue(pExpress);
        pOrientation.setSelectedMaxValue(pOrient);
        sGender.setSelectedMinValue(sGenMin);
        sGender.setSelectedMaxValue(sGenMax);
        sExpression.setSelectedMinValue(sExMin);
        sExpression.setSelectedMaxValue(sExMax);
        sOrientation.setSelectedMinValue(sOriMin);
        sOrientation.setSelectedMaxValue(sOriMax);

        //Defines the color of each thumb. In this case, we used blue.
        pGender.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        pExpression.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        pOrientation.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        sGender.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        sExpression.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);
        sOrientation.setTextAboveThumbsColorResource(android.R.color.holo_blue_light);

        //Sets values from null to a given number in case user unchanges.
        pGenderUpdateValue = personal.pGen;
        pExpressionUpdateValue = personal.pExpress;
        pOrientationUpdateValue = personal.pOrient;
        sGenderUpdateMax = seeking.sGenMax;
        sExpressionUpdateMax = seeking.sExMax;
        sOrientationUpdateMax = seeking.sOriMax;
        sGenderUpdateMin = seeking.sGenMin;
        sExpressionUpdateMin = seeking.sExMin;
        sOrientationUpdateMin = seeking.sOriMin;
        updateSliders();

        //Click Listener for the user's interaction with the button.
        UpdateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //Save storage in UserLocalStore for personal
                Slider newUpdatePSlider = new Slider(pGenderUpdateValue, pExpressionUpdateValue,
                        pOrientationUpdateValue);
                UserLocalStore instance1 = new UserLocalStore(SliderUpdate.this);
                instance1.storePersonal(newUpdatePSlider);

                //Save storage in UserLocalStore for seeking
                Slider newUpdateSSlider = new Slider(sGenderUpdateMin, sGenderUpdateMax,
                        sExpressionUpdateMin, sExpressionUpdateMax,
                        sOrientationUpdateMin, sOrientationUpdateMax);
                UserLocalStore instance2 = new UserLocalStore(SliderUpdate.this);
                instance2.storeSeeking(newUpdateSSlider);

                //Calls the client sockets to send values to Server.
                new sendUpdatePSliders().execute();
                new sendUpdateSSliders().execute();
            }
        });
    }

    /**
     * Main Class for updating sliders. This section contains 6 methods that provide us with
     * setting up the seekbar that represents our sliders. In the first 3, we will only focus on one
     * thumb, for personal sliders (here, a single thumb will represent the maxValue variable.)
     */
    public void updateSliders(){

///////////////The following methods contribute to the Personal Slider Values.//////////////////////
        pGender.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pGenderUpdateValue = (Integer) maxValue;
            }
        });

        //Seeking Expression RangeBar Slider. Gets Maximum and Minimum values on same slider.
        pExpression.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pExpressionUpdateValue = (Integer) maxValue;
            }

        });

        //Seeking Orientation RangeBar Slider. Gets Maximum and Minimum values on same slider.
        pOrientation.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pOrientationUpdateValue = (Integer) maxValue;
            }

        });


////////////////The following methods contribute to the Seeker Slider Values.///////////////////

        //Seeking Gender RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sGender.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sGenderUpdateMax = (Integer) maxValue;
                sGenderUpdateMin = (Integer) minValue;
            }
        });

        //Seeking Expression RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sExpression.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sExpressionUpdateMax = (Integer) maxValue;
                sExpressionUpdateMin = (Integer) minValue;
            }

        });

        //Seeking Orientation RangeBar Slider. Gets Maximum and Minimum values on same slider.
        sOrientation.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener(){
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sOrientationUpdateMax = (Integer) maxValue;
                sOrientationUpdateMin = (Integer) minValue;
            }

        });
    }

    /**
     * Client Sockets pass values to the App Server, which then transcribes it to SQL language
     * to save into the database. 2 sockets are made; one for the personal sliders, and one for
     * the seeking sliders.
     */
    class sendUpdatePSliders extends AsyncTask<Void,Void,Void> {

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
                UserLocalStore userLocalStore = new UserLocalStore(SliderUpdate.this);
                String command = "updatePSlider, " + userLocalStore.getUserName();
                Slider personal = userLocalStore.getPSliders();

                command += ", " + personal.pGen + ", " + personal.pExpress +
                        ", " + personal.pOrient;

                out.println(command);//Server
                Log.i("Register Command", command);
                String temp;
                while ((temp = in.readLine()) != null) {
                    if(temp == "true"){
                        System.out.println(temp);
                        pGood = true;
                    } else {
                        Log.d("Error", command);
                    }
                    Log.d("Register Command, ", temp);
                    System.out.println(temp);
                }
                sock.close();
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


    //Client Socket for updating Seeking sliders will do the same as the previous socket.
    class sendUpdateSSliders extends AsyncTask<Void,Void,Void> {

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
                UserLocalStore userLocalStore = new UserLocalStore(SliderUpdate.this);
                String command = "updateSSlider, " + userLocalStore.getUserName();
                Slider seeking = userLocalStore.getSSliders();

                command += "," + seeking.sGenMin + ", " + seeking.sGenMax +
                        ", " + seeking.sExMin + ", " + seeking.sExMax
                        + "," + seeking.sOriMin + ", " + seeking.sOriMax;

                out.println(command);//Server
                Log.i("Register Command", command);
                String temp;
                while ((temp = in.readLine()) != null) {
                    if(temp == "true"){
                        System.out.println(temp);
                        sGood = true;
                    } else {
                        Log.d("Error", command);
                    }
                    Log.d("Register Command, ", temp);
                    System.out.println(temp);
                }
                sock.close();
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

            if (sGood && pGood) {
                //Intent object to pass values.
                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                startActivity(intent);

            } else {//Error pops up.
                Toast.makeText(getApplicationContext(), "Register Error",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
}