package com.sourcey.cheriejw;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Base64;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.*;
import java.lang.Integer;
import java.lang.*;
import android.widget.Button;
import java.io.ByteArrayOutputStream;
import java.io.*;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import org.florescu.android.rangeseekbar.RangeSeekBar;

public class Homepage extends ActionBarActivity {

    //EditText Username; //108.23.15
    UserLocalStore userLocalStore;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_matches) {
            Intent intent = new Intent(this, Matches.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Everything in this class will be stored. The user will not be able to change anything at all
     * This section is for display only. The only functions that the user is able to do here is
     * pressing buttons.
     */

    //Global variables for each category.
    RangeSeekBar HomePGenderx, HomePExpressx, HomePOrientx;
    RangeSeekBar HomeSGender, HomeSExpression, HomeSOrientation;

    Button EditSliders, FindMatchesButton, bLogout, EditPictureButton;

    ImageView ProfilePicture;

    private int pGenderValue, pExpressionValue, pOrientationValue,
            sGenderValueMax,sExpressionValueMax, sOrientationValueMax,
            sGenderValueMin, sExpressionValueMin, sOrientationValueMin;

    private TextView UsernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        UsernameText = (TextView) findViewById(R.id.usernameText);
        EditSliders = (Button) findViewById(R.id.editSliders);
        FindMatchesButton = (Button) findViewById(R.id.findMatchesButton);
        bLogout = (Button) findViewById(R.id.hLogout);
        EditPictureButton = (Button) findViewById(R.id.editPictureButton);

        HomePGenderx = (RangeSeekBar) findViewById(R.id.homePGender);
        HomePExpressx = (RangeSeekBar) findViewById(R.id.homePExpress);
        HomePOrientx = (RangeSeekBar) findViewById(R.id.homePOrient);
        HomeSGender = (RangeSeekBar) findViewById(R.id.homeSeekGenderBar);
        HomeSExpression = (RangeSeekBar) findViewById(R.id.homeSeekExpressBar);
        HomeSOrientation = (RangeSeekBar) findViewById(R.id.homeSeekOrientBar);

        ProfilePicture = (ImageView) findViewById(R.id.profileImage);

        //Sets the seekbars to its proper values as the user intended.
        userLocalStore = new UserLocalStore(Homepage.this);
        String username = userLocalStore.getUserName();
        Slider personal = userLocalStore.getPSliders();
        Slider seeking = userLocalStore.getSSliders();
        String picture = userLocalStore.getProfilePicture();

        //For picture, this is how we were supposed to gather the image. We convert it from bitmap
        //String since we cannot save a bitmap file to SharedPreferences. Afterwards, we would convert
        //the same string back to an original bitmap image.
        if(picture != null) {
            byte[] imageAsBytes = Base64.decode(picture, Base64.DEFAULT);
            ProfilePicture = (ImageView) this.findViewById(R.id.profileImage);
            ProfilePicture.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        //gets the username to display on the screen.
        UsernameText.setText(String.valueOf(username));

        //Gathers information from the userLocalStore and sets it to the Global Values.
        pGenderValue = personal.pGen;
        pExpressionValue = personal.pExpress;
        pOrientationValue = personal.pOrient;
        sGenderValueMax = seeking.sGenMax;
        sExpressionValueMax = seeking.sExMax;
        sOrientationValueMax = seeking.sOriMax;
        sGenderValueMin = seeking.sGenMin;
        sExpressionValueMin = seeking.sExMin;
        sOrientationValueMin = seeking.sOriMin;

        //RangeSeekBar has a number class that can be able to read any data type involving numbers
        //from int to double to float. Here we convert our int values to Number for its the only
        //way the rangeSeekBar is able to gather the values.
        Number pGen = personal.pGen;
        Number pExpress = personal.pExpress;
        Number pOrient = personal.pOrient;
        Number sGenMax = seeking.sGenMax;
        Number sGenMin = seeking.sGenMin;
        Number sExMin = seeking.sExMin;
        Number sExMax = seeking.sExMax;
        Number sOriMin = seeking.sOriMin;
        Number sOriMax = seeking.sOriMax;

        //Sets values to the rangeSeekBars to display.
        HomePGenderx.setSelectedMaxValue(pGen);
        HomePExpressx.setSelectedMaxValue(pExpress);
        HomePOrientx.setSelectedMaxValue(pOrient);

        HomeSGender.setSelectedMinValue(sGenMin);
        HomeSGender.setSelectedMaxValue(sGenMax);
        HomeSExpression.setSelectedMinValue(sExMin);
        HomeSExpression.setSelectedMaxValue(sExMax);
        HomeSOrientation.setSelectedMinValue(sOriMin);
        HomeSOrientation.setSelectedMaxValue(sOriMax);

        //Defines the color of each thumb. In this case, we used blue.
        HomePGenderx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        HomePExpressx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        HomePOrientx.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        HomeSGender.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        HomeSExpression.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);
        HomeSOrientation.setTextAboveThumbsColorResource(android.R.color.holo_blue_dark);

        //Disables seekbar from moving in order to display it instead.
        HomePGenderx.setEnabled(false);
        HomePExpressx.setEnabled(false);
        HomePOrientx.setEnabled(false);
        HomeSGender.setEnabled(false);
        HomeSExpression.setEnabled(false);
        HomeSOrientation.setEnabled(false);

        //Directs to main method, to display the Sliders. No calculation necessary.
        HomePage();

        //Button used to direct user to edit their sliders.
        EditSliders.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSliders = new Intent(getApplicationContext(), SliderUpdate.class);
                startActivity(intentSliders);

            }
        });

        //Button used to direct user to the Matches Activity to find their matches.
        FindMatchesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSliders = new Intent(getApplicationContext(), Matches.class);
                startActivity(intentSliders);

            }
        });

        //Button used to direct user to edit their personal picture.
        EditPictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSliders = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intentSliders);

            }
        });

        //Button used to direct user to the login screen. Also, we are cleaning up data.
        bLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSliders = new Intent(getApplicationContext(), LoginActivity.class);
                userLocalStore.clearUserLocalStore();
                startActivity(intentSliders);
                finish();

            }
        });

    }

    //Displays Sliders Only. No calculation will be made.
    public void HomePage() {

        /////////////////////////Values for Personal Sliders//////////////////////////
        HomePGenderx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pGenderValue = (Integer) maxValue;
            }
        });

        //Seeking Expression RangeBar Slider. Gets Maximum and Minimum values on same slider.
        HomePExpressx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pExpressionValue = (Integer) maxValue;
            }

        });

        //Seeking Orientation RangeBar Slider. Gets Maximum and Minimum values on same slider.
        HomePOrientx.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pOrientationValue = (Integer) maxValue;
            }

        });

        ///////The following methods contribute to the Seeker Values.////////
        HomeSGender.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sGenderValueMax = (Integer) maxValue;
                sGenderValueMin = (Integer) minValue;
            }
        });

        //Seeking Expression RangeBar Slider. Gets Maximum and Minimum values on same slider.
        HomeSExpression.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sExpressionValueMax = (Integer) maxValue;
                sExpressionValueMin = (Integer) minValue;
            }

        });

        //Seeking Orientation RangeBar Slider. Gets Maximum and Minimum values on same slider.
        HomeSOrientation.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                sOrientationValueMax = (Integer) maxValue;
                sOrientationValueMin = (Integer) minValue;
            }

        });
    }
}