package com.sourcey.cheriejw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import android.content.Context;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameObj, passwordObj;
    private Button getPicBtn, addPicBtn, updatePicBtn, choosePicBtn, returnHomeButton;
    private ImageView profilePicBox;
    private String userName, password;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Bitmap selectedImage = null;
    private String command;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userNameObj = (EditText) findViewById(R.id.userNameTXT);
        //passwordObj = (EditText) findViewById(R.id.passwordTXT);
        //resetBtn = (Button) findViewById(R.id.resetBtn);
        choosePicBtn = (Button) findViewById(R.id.choosePicBtn);
        getPicBtn = (Button) findViewById(R.id.getPicBtn);
        addPicBtn = (Button) findViewById(R.id.addPicBtn);
        addPicBtn.setEnabled(false);
        updatePicBtn = (Button) findViewById(R.id.updatePicBtn);
        updatePicBtn.setEnabled(false);
        returnHomeButton = (Button) findViewById(R.id.doneButton);

        profilePicBox = (ImageView)findViewById(R.id.proPic);

        getPicBtn.setOnClickListener(this);
        addPicBtn.setOnClickListener(this);
        updatePicBtn.setOnClickListener(this);
        choosePicBtn.setOnClickListener(this);
        returnHomeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        UserLocalStore user = new UserLocalStore(MyProfile.this);
        String usern = user.getUserName();

        //userName = userNameObj.getText().toString();
        userName = usern;
        //password = passwordObj.getText().toString();

        switch(view.getId()){
            case R.id.getPicBtn:
                new getPic().execute();
                break;

            case R.id.addPicBtn:
                command = "addPic, " + userName;
                new addPic().execute();
                break;

            case R.id.updatePicBtn:
                command = "updatePic, " + userName;
                new addPic().execute();
                break;

            case R.id.doneButton:
                Intent ret = new Intent(getApplicationContext(), Homepage.class);
                startActivity(ret);
                break;

            case R.id.choosePicBtn:
                addPic();
                Log.i("IMAGE", "got picture");
                updatePicBtn.setEnabled(true);
                addPicBtn.setEnabled(true);
                break;
        }
    }

    private void addPic(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //
                profilePicBox.setImageBitmap(selectedImage);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class getPic extends AsyncTask<Void,Void,Bitmap> {
        Socket sock;
        DataInputStream din;
        DataOutputStream dout;
        String command = "getPic, " + userName;
        Bitmap bitMap;

        protected Bitmap doInBackground(Void... args) {
            try {
                Log.i("SOCKET", "created Socket");
                sock = new Socket("108.23.32.15", 6066);
                //sock = new Socket("10.0.2.2", 6066);
                din = new DataInputStream(sock.getInputStream());
                dout = new DataOutputStream(sock.getOutputStream());

                dout.writeUTF("getPic, " + userName);//server
                Log.i("SOCKET", "send command: " + "getPic, " + userName);
                String returned = din.readUTF();
                Log.i("IMAGE",returned);
                int size = Integer.valueOf(returned);

                bitMap = BitmapFactory.decodeStream(din);

                //Convert bitmap to String to save into the database.
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//is the bitmap object
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);

                //Saves casted bitmap to string into database.
                UserLocalStore userLocalStore = new UserLocalStore(MyProfile.this);
                userLocalStore.storePicture(encoded);

                /*if(size != 0) {
                    byte[] bytes = new byte[size];
                    din.read(bytes);
                    bitMap = BitmapFactory.decodeByteArray(bytes,0,size);
                    if(bitMap == null)
                        Log.i("IMAGE", "Image is null");
                } else {
                    Log.i("IMAGE", "Well, shit");
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (sock != null) {
                        sock.close();
                        din.close();
                        dout.close();
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            return bitMap;
        }

        protected void onPostExecute(Bitmap bitMap){

            profilePicBox.setImageBitmap(bitMap);
        }
    }

    class addPic extends AsyncTask<Void,Void,Void> {
        Socket sock;
        DataInputStream din;
        DataOutputStream dout;

        protected Void doInBackground(Void... args) {
            try {
                Log.i("SOCKET", "making socket");
                //sock = new Socket("10.0.2.2", 6066);
                sock = new Socket("108.23.32.15", 6066);
                din = new DataInputStream(sock.getInputStream());
                dout = new DataOutputStream(sock.getOutputStream());

                Log.i("SOCKET", "sending command " + command);
                dout.writeUTF(command);//Server
                String response = din.readUTF();
                Log.i("SOCKET", "recieved response " + response);

                if(response.equals("true")){
                    Log.i("SOCKET", "sending Image");
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, dout);
                    dout.close();
                } else {
                    Log.i("SOCKET", response);
                }

            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_profile);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }

}
