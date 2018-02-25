package com.sourcey.cheriejw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Cherie Woo on 3/7/2016.
 * This is where our information that the user inputs is stored. From any class,
 * we are able to call the sharedPreferences variable and either save or gather our data.
 * We can also replace it with new data each time we want to.
 */
public class UserLocalStore {
    //For debugging purposes.
    public  static final String SP_NAME = "userDetails";
    private static final String TAG = "SignupActivity";
    private static final String TAG2 = "RegisterSliders";

    SharedPreferences userLocalDatabase;//name of the variable.

    public UserLocalStore(Context context) {//Context
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) { //saves everything the user inputs.
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("name", user.name);
        userLocalDatabaseEditor.putString("username", user.username);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.putString("birthdate", user.birthdate);
        userLocalDatabaseEditor.putInt("personalGender", user.personal.pGen);
        userLocalDatabaseEditor.putInt("personalExpress", user.personal.pExpress);
        userLocalDatabaseEditor.putInt("personalOrient", user.personal.pOrient);
        userLocalDatabaseEditor.putInt("seekGenMin", user.personal.sGenMin);
        userLocalDatabaseEditor.putInt("seekGenMax", user.personal.sGenMax);
        userLocalDatabaseEditor.putInt("seekExprMin", user.personal.sExMin);
        userLocalDatabaseEditor.putInt("seekExprMax", user.personal.sExMax);
        userLocalDatabaseEditor.putInt("seekOrientMin", user.personal.sOriMin);
        userLocalDatabaseEditor.putInt("seekOrientMax", user.personal.sOriMax);
        userLocalDatabaseEditor.putString("profilePicture", user.profilePicture);
        userLocalDatabaseEditor.commit(); //save changes
    }

    public void storeSeeking(Slider seeking){//Stores the Seeking Sliders
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("seekGenMin", seeking.sGenMin);
        userLocalDatabaseEditor.putInt("seekGenMax", seeking.sGenMax);
        userLocalDatabaseEditor.putInt("seekExprMin", seeking.sExMin);
        userLocalDatabaseEditor.putInt("seekExprMax", seeking.sExMax);
        userLocalDatabaseEditor.putInt("seekOrientMin", seeking.sOriMin);
        userLocalDatabaseEditor.putInt("seekOrientMax", seeking.sOriMax);
        userLocalDatabaseEditor.commit();
    }

    public void storePersonal(Slider personal){//Stores the Personal Sliders.
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("personalGender", personal.pGen);
        userLocalDatabaseEditor.putInt("personalExpress", personal.pExpress);
        userLocalDatabaseEditor.putInt("personalOrient", personal.pOrient);
        userLocalDatabaseEditor.commit();
    }

//Saves information for Profile Picture
    public void storePicture(String profilePicture){
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("profilePicture", profilePicture);
        userLocalDatabaseEditor.commit();
    }


    public boolean isNull(){
        return (userLocalDatabase.getString("password", null) == null); //returns true it is null
    } //when logged out delete password if has username saved then login screen

    //User(String name, int age, String username, String password)
    public User getUserData(){
        User retVal = new User(
                userLocalDatabase.getString("name", null),
                userLocalDatabase.getString("birthdate", null), //-1 is value to return if it dne.
                userLocalDatabase.getString("username", null),
                userLocalDatabase.getString("password", null));
        retVal.setPersonal(getPSliders());
        retVal.setSeeking(getSSliders());
        retVal.setProfilePic(getProfilePicture());
        return retVal;
    }

    //Returns username when called from a class.
    public String getUserName(){
        return userLocalDatabase.getString("username", null);
    }


    public Slider getPSliders(){
        Slider setPersonal = new Slider(
                userLocalDatabase.getInt("personalGender", -99),
                userLocalDatabase.getInt("personalExpress", -99),
                userLocalDatabase.getInt("personalOrient", -99));
        return setPersonal;
    }

    public Slider getSSliders(){
        Slider setPreferred = new Slider(
                userLocalDatabase.getInt("seekGenMin", -99),
                userLocalDatabase.getInt("seekGenMax", -99),
                userLocalDatabase.getInt("seekExprMin", -99),
                userLocalDatabase.getInt("seekExprMax", -99),
                userLocalDatabase.getInt("seekOrientMin", -99),
                userLocalDatabase.getInt("seekOrientMax", -99));
        return setPreferred;
    }

    public String getProfilePicture(){
        return userLocalDatabase.getString("profilePicture", null);
    }

    //For logging out, we "destroy" our data.
    public void clearUserLocalStore(){
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("name", null);
        userLocalDatabaseEditor.putString("username", null);
        userLocalDatabaseEditor.putString("password", null);
        userLocalDatabaseEditor.putString("birthdate", null);
        storePersonal(new Slider());
        storeSeeking(new Slider());
        storePicture(null);
        userLocalDatabaseEditor.commit(); //save changes

    }
}
