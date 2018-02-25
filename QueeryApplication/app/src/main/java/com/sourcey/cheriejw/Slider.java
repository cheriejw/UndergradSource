package com.sourcey.cheriejw;

import android.transition.Slide;

/**
 * Created by Cherie Woo on 3/7/2016.
 *
 * This slider class is used for constructing our values for each slider.
 * 9 values overall that we can store and receive data from the userLocalStore class.
 */

public class Slider {
    //Initialize Global variables.
    public int pGen, pExpress, pOrient;
    public int sGenMax, sGenMin, sExMax, sExMin, sOriMax, sOriMin;

    //Default values.
    public Slider(){
        pGen = -1;
        pExpress = -1;
        pOrient = -1;
        sGenMax = -1;
        sGenMin = -1;
        sExMax = -1;
        sExMin = -1;
        sOriMax = -1;
        sOriMin = -1;
    }

    //Slider Class for Personal Sliders only. All others will be set to null.
    public Slider(int pGen, int pExpress, int pOrient) {
        this.pGen = pGen;
        this.pExpress = pExpress;
        this.pOrient = pOrient;

        sGenMax = -1;
        sGenMin= -1;
        sExMax= -1;
        sExMin= -1;
        sOriMax= -1;
        sOriMin = -1;
    }

    //Slider Class Seeking Sliders only. All others will be set to null.
    public Slider(int sGenMin, int sGenMax, int sExMin, int sExMax, int sOriMin, int sOriMax){
        this.sGenMax = sGenMax;
        this.sGenMin = sGenMin;
        this.sExMax = sExMax;
        this.sExMin = sExMin;
        this.sOriMax = sOriMax;
        this.sOriMin = sOriMin;

        pGen = -1;
        pExpress = -1;
        pOrient = -1;
    }
}
