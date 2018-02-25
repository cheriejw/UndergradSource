package com.sourcey.cheriejw;

/**
 * Created by Cherie Woo on 3/7/2016.
 */
public class User {
    public String name, username, password;
    public String birthdate;
    //slider values
    public Slider personal, seeking;
    //can call slider for ints to parse to server
    public String profilePicture;

    public User(String name, String birthdate, String username, String password) {
        this.name = name;
        this.birthdate = birthdate;
        this.username = username;
        this.password = password;
        setDefaultSlider();
        setDefaultPicture();
    }

    public User(String username, String password) {
        this("", "", username, password);
        setDefaultSlider();
        setDefaultPicture();
    }

    public void setDefaultSlider(){
        personal = new Slider();
        seeking = new Slider();
    }

    public void setDefaultPicture(){
        profilePicture = null;
    }

    public void setPersonal(int gender, int expression, int orientation) {
        personal.pGen = gender;
        personal.pExpress = expression;
        personal.pOrient = orientation;

    }

    public void setPersonal(Slider newSlider) {
        personal.pGen = newSlider.pGen;
        personal.pExpress = newSlider.pExpress;
        personal.pOrient = newSlider.pOrient;
    }

    //give it three sliders with max and mins
    public void setSeeking(int gMin, int gMax, int eMin, int eMax, int oMin, int oMax) {
        seeking.sGenMin = gMin;
        seeking.sGenMax = gMax;
        seeking.sExMin = eMin;
        seeking.sExMax = eMax;
        seeking.sOriMax = oMax;
        seeking.sOriMin = oMin;
    }

    public void setSeeking(Slider newSlider){
        seeking.sGenMin = newSlider.sGenMin;
        seeking.sGenMax = newSlider.sGenMax;
        seeking.sExMin = newSlider.sExMin;
        seeking.sExMax = newSlider.sExMax;
        seeking.sOriMax = newSlider.sOriMax;
        seeking.sOriMin = newSlider.sOriMin;
    }

    public void setProfilePic(String pic){
        profilePicture = pic;
    }

}

