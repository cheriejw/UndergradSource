package com.sourcey.cheriejw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Bind;

//ADD A FIRSTNAME LASTNAME THING

public class SignupActivity extends AppCompatActivity {
    LocListener location = new LocListener();
    private static final String TAG = "SignupActivity";
    private static final String jamesServer = "108.23.32.15";
    private String usernameStatus = "exists";
    private String locStatus = "nope";
    private double longitude = location.getLon();
    private double latitude = location.getLat();
    @Bind(R.id.input_username) EditText _usernameText;
    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_birthdate) EditText _birthdateText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // signup();
                new userCheck().execute();
//                Intent intent = new Intent(getApplicationContext(), RegisterSliders.class);
//                startActivityForResult(intent, 0);
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        String name = _nameText.getText().toString();
        String birthdate = _birthdateText.getText().toString();
        String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

        User newUser = new User(name, birthdate, username, password);
        UserLocalStore instance = new UserLocalStore(this);
        instance.storeUserData(newUser);

        SendRegister temp = new SendRegister(); //send to server

        temp.execute();
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        new setLocation().execute();

        finish();
        Intent intent = new Intent(getApplicationContext(), RegisterSliders.class);
        startActivityForResult(intent, 0);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    //FROM JAMES
    class SendRegister extends AsyncTask<Void,Void,Void> {

        Boolean complete = false;
        //Socket sock = null;
        //InetAddress host;
        PrintWriter out;
        BufferedReader in;

        protected Void doInBackground(Void... args) {
            Socket sock = null;
            try {
                sock = new Socket("10.0.2.2", 4910); //connect to local host server
                //sock = new Socket(jamesServer, 4910); //to yames server

                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

//                out.println("addUser, cherie, sdalkfj, laskdjf, alskdjf, alskjdf@kdfaj.asdfkj, 12331324");
                String command = "addUser, " + getETtext(_usernameText) + ", " + getETtext(_passwordText) +
                        ", " + getETtext(_nameText) + ", " + getETtext(_nameText) + ", " +
                        getETtext(_emailText) + ", " + getETtext(_birthdateText);
                out.println(command);
//                out.println("addUser, cherie, sdalkfj, laskdjf, alskdjf, alskjdf@kdfaj.asdfkj, 12339324");
                Log.i("Register Command", command);
                String temp;
                while (in.ready()) {
                    temp = in.readLine();
                    Log.d("Register Command", temp);
                    System.out.println(temp);
                }
                sock.close();
            } catch (UnknownHostException e) {
                Log.d(TAG, "UnknownHostException");
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (sock != null){
                        sock.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //return null;
            return null;
        }
    }



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

    private String getETtext(EditText et) {
        String temp = et.getText().toString().trim();
        if (temp.equals("") || temp == null) {
            return "";
        } else return temp;
    }
    //TO HERE

    public boolean validate() {
        boolean valid = true;
        String username = _usernameText.getText().toString();
        String name = _nameText.getText().toString();
        String birthdate = _birthdateText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        //new userCheck().execute();

        if (birthdate.length() != 8) { // || validDate(birthdate)
            _birthdateText.setError("invalid birthdate.");
            valid = false;
        } else if (!(validOfAge(birthdate))) {
            _birthdateText.setError("must be 18 or older to register.");
            valid = false;
        }
        else
        {
            _birthdateText.setError(null);
        }

        if (username.isEmpty() || username.length() < 4) {
            _usernameText.setError("at least 4 characters");
            valid = false;
        }
        /*else if (usernameStatus.equals("exists")) {
            _usernameText.setError("sorry username already exists");
            valid = false;
        }*/
        else
        {
            _usernameText.setError(null);
        }

        if (name.isEmpty() || name.length() < 2) {
            _nameText.setError("at least 2 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }

    //BONUS TO CHECK FOR BIRTHDAY

    class setLocation extends AsyncTask<Void, Void, Void>{ //passing stuff in params
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

                String command = "setLocation ," + _usernameText + longitude + latitude;
                out.println(command);

                locStatus = in.readLine();
                Log.d("RESPONSE", locStatus);
                /*while (in.ready()) {
                    response = in.readLine();
                    Log.i("SOCKET",response);
                    usernameStatus = response; //should be either exists or free
                }*/
                if (!(locStatus.equals("true"))){ //if not true
                    command = "setLocation ," + _usernameText + 0.0 + 0.0; //will set to 0.0 if user does not allow
                    out.println(command);
                }
                sock.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    class userCheck extends AsyncTask<Void, Void, Boolean>{ //passing stuff in params
        @Override
        protected Boolean doInBackground(Void... voids) {
            Socket sock;
            PrintWriter out;
            BufferedReader in;

            try{
                //sock = new Socket("108.23.32.15", 4910); //to James server
                sock = new Socket("10.0.2.2", 4910); //local host

                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String command = "testUserName ," + _usernameText;
                out.println(command);

                usernameStatus = in.readLine();
                Log.d("RESPONSE", usernameStatus);
                /*while (in.ready()) {
                    response = in.readLine();
                    Log.i("SOCKET",response);
                    usernameStatus = response; //should be either exists or free
                }*/
                sock.close();
            } catch(IOException e){
                e.printStackTrace();
            }

            if (usernameStatus.equals("free")){
                return true;
            }
            else return false;
        }

        @Override
        protected void onPostExecute(Boolean passed) {
            super.onPostExecute(passed);

            if(passed){
                Log.d("SOCKET", "calling signup from post execute");
                signup();
            } else {
                _usernameText.setError("sorry username already exists");
            }
        }
    }

    public boolean validOfAge(String birthdate){
        Calendar c = Calendar.getInstance();
        //check if year is -18
        //System.out.println(c.YEAR);
        Log.d(TAG, "birthday check");
        try {
            int month = Integer.parseInt(birthdate.substring(0, 2)); //gets 0 & 1
            int day = Integer.parseInt(birthdate.substring(2, 4)); //2 is "/" get 3 and 4 //get 2 and 3
            int year = Integer.parseInt(birthdate.substring(4, 8)); //5 is "/" get 6,7,8,9 //get 4,5,6,7
            Log.d(TAG, "birthday check year inputed year : " + year + " inputed month : " + month);

            if (year > ((c.get(Calendar.YEAR)) - 18)) { //if year is greater than 1998 -> 18 then youre not older than 18
                Log.d(TAG, "birthday check year" + ((c.get(Calendar.YEAR)) - 18) + " " + (c.get(Calendar.YEAR)));
                return false;
            }
            else if (year == ((c.get(Calendar.YEAR)) - 18)) { //only if you're in the min year. 1998
                //year is greater than or equal to...
                if (month > (c.get(Calendar.MONTH))) { //same month is okay month is less than 4
                    Log.d(TAG, "birthday check month" + (c.get(Calendar.MONTH)) + "mine" + month);
                    return false; //youre not 18 yet
                }
                else if (month == (c.get(Calendar.MONTH))) { //if month is 4 you might be 18
                    if (day > (c.get(Calendar.DATE))) { //if the day is after 13 (today) then youre not 18 yet.
                        Log.d(TAG, "birthday check date" + (c.get(Calendar.DATE)) + "mine: " + day);
                        return false;
                    }
                }
                else { //month is < min month so youre 18 already!
                    return true;
                }
            }
            else { //year is < min year so youre 18 already!
                return true;
            }
            return false; //dont know why i need this...
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
