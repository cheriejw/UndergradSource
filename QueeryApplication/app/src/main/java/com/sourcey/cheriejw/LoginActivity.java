package com.sourcey.cheriejw;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private boolean passValid = false;
    private String usernameStatus = "free";
    private String username = null;
    private String password = null;
//    private int checksum = 0;

    @Bind(R.id.input_email) EditText _usernameText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink; //xml names

    //108.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        UserLocalStore store = new UserLocalStore(this);
        _usernameText.setText(store.getUserName()); //populates if has username stored
        if (!(store.isNull())){
            //if there is a password saved... login to server to check and then go to homepage
            Log.d(TAG, "Local User Store says that there is a password stored so you should just go to Homepage.");
//            username = ;
//            password = ;
            username = store.getUserName();
            _usernameText.setText(username);
            password = store.getUserData().password;
            _passwordText.setText(password);
            new passCheck().execute();
        }
        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                new userCheck().execute(); //check username
                new passCheck().execute(); //send to server
//                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Sigrnup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");
//        checksum = 0;

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

//        String email = _usernameText.getText().toString();
       // username = _usernameText.getText().toString();
        //password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
//        Log.d(TAG, " " + checksum);
//        while (checksum != 2){
//            progressDialog.setMessage("Connecting to Server...");
//            progressDialog.show();
//        }

//        if (usernameStatus == "exists"){
//            if(passValid == true) {
//                Intent intent2 = new Intent(getApplicationContext(), Homepage.class);
//                startActivityForResult(intent2, 0); //fix
//            }
//            else { //password/username combination is wrong
//                _passwordText.setError("incorrect password");
//                onLoginFailed();
//                return;
//            }
//        }
//        else{
//            _usernameText.setError("please enter a username");
//            onLoginFailed();
//            return;
//        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

//    class userCheck extends AsyncTask<Void, Void, Void>{ //passing stuff in params
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Socket sock;
//            PrintWriter out;
//            BufferedReader in;
//
//            try{
//                sock = new Socket("108.23.32.15", 4910); //to James server
//                //sock = new Socket("10.0.2.2", 4910); //local host
//
//                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
//                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//
//                String command = "testUserName ," + _usernameText;
//                out.println(command);
//
//                usernameStatus = in.readLine();
//                Log.d("RESPONSE", usernameStatus);
//                checksum += 1;
//                sock.close();
//            } catch(IOException e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _usernameText.setError("please enter a username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    class passCheck extends AsyncTask<Void, Void, String>{ //passing stuff in params
        //pre and post can modify user interface. cant in bkgd. popup error done in post exe
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            login();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Socket sock;
            PrintWriter out;
            BufferedReader in;

            try{
                sock = new Socket("108.23.32.15", 4910);

                out = new PrintWriter(sock.getOutputStream(), true); //flushes after println
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String command = "login ," + username + ", " + password;
                out.println(command);

                String response = "";
//                while (in.ready()) {
                    response = in.readLine();
                    Log.d("Response: ", response);
                    if (response.equals("true")){
                        passValid = true;
                    }
                    else{

                    }
//                    checksum += 1;
//                }
                sock.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            Log.d("PostExecute", "PassValid: " + passValid + " Response: " + response);
            if (passValid) {
                Intent intent2 = new Intent(getApplicationContext(), Homepage.class);
                startActivityForResult(intent2, 0);
            } else {
                if (response.equals("pswdError")) { //password/username combination is wrong
                    _passwordText.setError("incorrect password");
                    onLoginFailed();
                    return;
                } else {
                    _usernameText.setError("please enter a username");
                    onLoginFailed();
                    return;
                }
            }
        }
    }
}
