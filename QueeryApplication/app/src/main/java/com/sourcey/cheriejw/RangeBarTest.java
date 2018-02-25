package com.sourcey.cheriejw;

/*
Copyright 2015 Alex Florescu
Copyright 2014 Yahoo Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class RangeBarTest extends Activity {
    private EditText min, max;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rangebar_test);
        min = (EditText) findViewById(R.id.MinVal);//min value on the XML
        max = (EditText) findViewById(R.id.max);//max value on the XML

        //Declare Range Bar
        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) findViewById(R.id.rangeSeekBarTextColorWithCode);
        //set text color, play around with values
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(android.R.color.holo_red_light);

        //set Min and Max values when the user changes the values
        rangeSeekBarTextColorWithCode.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                max.setText(String.valueOf(maxValue));
                min.setText(String.valueOf(minValue));

            }
        });
    }
}