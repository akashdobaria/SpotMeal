
package com.spotmeal;

import android.content.Intent;
import android.os.HardwarePropertiesManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FiltersPopUpWindow extends AppCompatActivity {

    private static final String TAG = FiltersPopUpWindow.class.getSimpleName();

    CheckBox checkBox_Italian;
    CheckBox checkBox_Indian;
    CheckBox checkBox_American;
    CheckBox checkBox_Mexican;
    CheckBox checkBox_Rating1;
    CheckBox checkBox_Rating2;
    CheckBox checkBox_Rating3;
    CheckBox checkBox_Rating4;
    CheckBox checkBox_Rating5;

    SeekBar seekBar_MinValue;
    SeekBar seekBar_MaxValue;
    SeekBar seekBar_StartTime;
    SeekBar seekBar_EndTime;

    EditText editText_MinValue;
    EditText editText_MaxValue;
    EditText editText_StartTime;
    EditText editText_EndTime;

    Button button_ApplyFilters;
    Button button_ClearFilters;
    ImageButton button_Close;

    int maxPrice=100;
    int minPrice=0;
    int startTimehour=0;
    int startTimemin=0;
    int endTimehour=23;
    int endTimemin=30;
    boolean cuisine_American = true;
    boolean cuisine_Indian = true;
    boolean cuisine_Italian = true;
    boolean cuisine_Mexican = true;
    boolean rating1 = true;
    boolean rating2 = true;
    boolean rating3 = true;
    boolean rating4 = true;
    boolean rating5 = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_pop_up_window);

        checkBox_American = (CheckBox) findViewById(R.id.checkBox_American);
        checkBox_Indian = (CheckBox) findViewById(R.id.checkBox_Indian);
        checkBox_Italian = (CheckBox) findViewById(R.id.checkBox_Italian);
        checkBox_Mexican = (CheckBox) findViewById(R.id.checkBox_Mexican);
        checkBox_Rating1 = (CheckBox) findViewById(R.id.star1);
        checkBox_Rating2 = (CheckBox) findViewById(R.id.star2);
        checkBox_Rating3 = (CheckBox) findViewById(R.id.star3);
        checkBox_Rating4 = (CheckBox) findViewById(R.id.star4);
        checkBox_Rating5 = (CheckBox) findViewById(R.id.star5);

        seekBar_MinValue = (SeekBar) findViewById(R.id.min_price_seekBar);
        seekBar_MaxValue = (SeekBar) findViewById(R.id.max_price_seekBar);
        seekBar_StartTime = (SeekBar) findViewById(R.id.starttime_seekBar);
        seekBar_EndTime = (SeekBar) findViewById(R.id.endtime_seekBar);

        editText_MinValue = (EditText) findViewById(R.id.min_price_editText);
        editText_MaxValue = (EditText) findViewById(R.id.max_price_editText);
        editText_StartTime = (EditText) findViewById(R.id.starttime_editText);
        editText_EndTime = (EditText) findViewById(R.id.endtime_editText);

        button_ApplyFilters = (Button) findViewById(R.id.apply_filters);
        button_ClearFilters = (Button) findViewById(R.id.clear_filters);
        button_Close = (ImageButton) findViewById(R.id.button_Close_Filters);

        checkBox_American.setChecked(true);
        checkBox_Mexican.setChecked(true);
        checkBox_Italian.setChecked(true);
        checkBox_Indian.setChecked(true);

        if(!(!HomeActivity.indian && !HomeActivity.italian && !HomeActivity.mexican && !HomeActivity.american)) {
            if (HomeActivity.indian == false) {
                checkBox_Indian.setChecked(false);
                cuisine_Indian = false;
            }
            if (HomeActivity.italian == false) {
                checkBox_Italian.setChecked(false);
                cuisine_Italian = false;
            }
            if (HomeActivity.mexican == false) {
                checkBox_Mexican.setChecked(false);
                cuisine_Mexican = false;
            }
            if (HomeActivity.american == false) {
                checkBox_American.setChecked(false);
                cuisine_American = false;
            }
        }
        if(!HomeActivity.minPrice.equals("-1")){
            editText_MinValue.setText(HomeActivity.minPrice);
            seekBar_MinValue.setProgress(Integer.parseInt(HomeActivity.minPrice));
            minPrice = Integer.parseInt(HomeActivity.minPrice);
        }
        if(!HomeActivity.maxPrice.equals("-1")){
            editText_MaxValue.setText(HomeActivity.maxPrice);
            seekBar_MaxValue.setProgress(Integer.parseInt(HomeActivity.maxPrice));
            maxPrice = Integer.parseInt(HomeActivity.maxPrice);
        }
        if(!HomeActivity.startTime.equals("-1")){
            editText_StartTime.setText(HomeActivity.startTime);
            seekBar_StartTime.setProgress(mapStringToSeekbar(HomeActivity.startTime));
            String[] start = stringToHoursDate(HomeActivity.startTime);
            startTimehour = Integer.parseInt(start[0]);
            startTimemin = Integer.parseInt(start[1]);
        }
        if(!HomeActivity.endTime.equals("-1")){
            editText_EndTime.setText(HomeActivity.endTime);
            seekBar_EndTime.setProgress(mapStringToSeekbar(HomeActivity.endTime));
            String[] end = stringToHoursDate(HomeActivity.endTime);
            endTimehour = Integer.parseInt(end[0]);
            endTimemin = Integer.parseInt(end[1]);
        }


        seekBar_MinValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrice = progress;
                editText_MinValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_MaxValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrice = progress;
                editText_MaxValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_StartTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startTimehour = progress/2;
                startTimemin = 30 * (progress%2);
                editText_StartTime.setText(mapSeekbarToTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_EndTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                endTimehour = progress/2;
                endTimemin = 30 * (progress%2);
                editText_EndTime.setText(mapSeekbarToTime(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkBox_American.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_American.isChecked()){
                    cuisine_American = true;
                }
                else {
                    cuisine_American = false;
                }
            }
        });

        checkBox_Italian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Italian.isChecked()){
                    cuisine_Italian = true;
                }
                else {
                    cuisine_Italian = false;
                }
            }
        });

        checkBox_Indian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Indian.isChecked()){
                    cuisine_Indian = true;
                }
                else {
                    cuisine_Indian = false;
                }
            }
        });

        checkBox_Mexican.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Mexican.isChecked()){
                    cuisine_Mexican = true;
                }
                else {
                    cuisine_Mexican = false;
                }
            }
        });

        checkBox_Rating1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Rating1.isChecked()){
                    rating1 = true;
                }
            }
        });

        checkBox_Rating2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Rating2.isChecked()){
                    rating2 = true;
                }
            }
        });
        checkBox_Rating3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Rating3.isChecked()){
                    rating3 = true;
                }
            }
        });
        checkBox_Rating4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Rating4.isChecked()){
                    rating4 = true;
                }
            }
        });
        checkBox_Rating5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox_Rating5.isChecked()){
                    rating5 = true;
                }
            }
        });

        editText_MinValue.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                //s is the current character in the eddittext after it is changed
                if(!s.toString().isEmpty()) {
                    seekBar_MinValue.setProgress(Integer.parseInt(s.toString()));
                }
                editText_MinValue.setSelection(editText_MinValue.getText().length());
            }
        });

        editText_MaxValue.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                //s is the current character in the eddittext after it is changed
                if(!s.toString().isEmpty()) {
                    seekBar_MaxValue.setProgress(Integer.parseInt(s.toString()));
                }
                editText_MaxValue.setSelection(editText_MaxValue.getText().length());
            }
        });

        editText_StartTime.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                //s is the current character in the eddittext after it is changed
                if(!s.toString().isEmpty()) {
                    seekBar_StartTime.setProgress(mapStringToSeekbar(s.toString()));
                }
                editText_StartTime.setSelection(editText_StartTime.getText().length());
            }
        });

        editText_EndTime.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                //s is the current character in the eddittext after it is changed
                if(!s.toString().isEmpty()) {
                    seekBar_EndTime.setProgress(mapStringToSeekbar(s.toString()));
                }
                editText_EndTime.setSelection(editText_EndTime.getText().length());
            }
        });

        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_ApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(cuisine_Indian && cuisine_Italian && cuisine_Mexican && cuisine_American)){
                    if(cuisine_American){
                        HomeActivity.american = true;
                    }
                    if(cuisine_Mexican){
                        HomeActivity.mexican = true;
                    }
                    if(cuisine_Italian){
                        HomeActivity.italian = true;
                    }
                    if(cuisine_Indian){
                        HomeActivity.indian = true;
                    }
                }else{
                    HomeActivity.american = false;
                    HomeActivity.italian = false;
                    HomeActivity.indian = false;
                    HomeActivity.mexican = false;
                }

                if(Integer.parseInt(editText_MinValue.getText().toString()) != 0){
                    HomeActivity.minPrice = editText_MinValue.getText().toString();
                }
                else {
                    HomeActivity.minPrice = "-1";
                }
                if(Integer.parseInt(editText_MaxValue.getText().toString()) != 100){
                    HomeActivity.maxPrice = editText_MaxValue.getText().toString();
                }
                else {
                    HomeActivity.maxPrice = "-1";
                }
                if(editText_StartTime.getText().toString().equals("0:0")){
                    HomeActivity.startTime = "-1";
                }
                else {
                    HomeActivity.startTime = editText_StartTime.getText().toString();
                }
                if(editText_EndTime.getText().toString().equals("23:30")){
                    HomeActivity.endTime = "-1";
                }
                else {
                    HomeActivity.endTime = editText_EndTime.getText().toString();
                }
                Log.i("Filters 0",""+HomeActivity.mEvents.size());
                Log.i("Filters backup 0",""+HomeActivity.backupEvents.size());
                if(HomeActivity.backupEvents.isEmpty()){
                    HomeActivity.backupEvents = (ArrayList) HomeActivity.mEvents.clone();
                    Log.i("Filters backup created ",""+HomeActivity.backupEvents.size());
                }

                HomeActivity.mEvents.clear();
                Log.i("Filters Clear",""+HomeActivity.mEvents.size());
                if (cuisine_American){
                    for(int i=0; i< HomeActivity.backupEvents.size(); i++){
                        if(HomeActivity.backupEvents.get(i).getCuisine().equals("American")){
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }
                    }
                }
                if (cuisine_Indian){
                    for(int i=0; i< HomeActivity.backupEvents.size(); i++){
                        if(HomeActivity.backupEvents.get(i).getCuisine().equalsIgnoreCase("Indian")){
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }
                    }
                }
                if (cuisine_Italian){
                    for(int i=0; i< HomeActivity.backupEvents.size(); i++){
                        if(HomeActivity.backupEvents.get(i).getCuisine().equalsIgnoreCase("Italian")){
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }
                    }
                }
                if (cuisine_Mexican){
                    for(int i=0; i< HomeActivity.backupEvents.size(); i++){
                        if(HomeActivity.backupEvents.get(i).getCuisine().equalsIgnoreCase("Mexican")){
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }
                    }
                }

                if(HomeActivity.mEvents.size() != 0) {
                    for (int i = 0; i < HomeActivity.mEvents.size(); i++) {
                        if (HomeActivity.mEvents.get(i).getCostPerPerson() <= minPrice || HomeActivity.mEvents.get(i).getCostPerPerson() >= maxPrice) {
                            HomeActivity.mEvents.remove(i);
                        }
                    }
                }

                if(HomeActivity.mEvents.size() == 0){

                    for (int i = 0; i < HomeActivity.backupEvents.size(); i++) {

                        if (HomeActivity.backupEvents.get(i).getCostPerPerson() >= minPrice && HomeActivity.backupEvents.get(i).getCostPerPerson() <= maxPrice) {
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }

                    }
                }
                if(HomeActivity.mEvents.size() != 0) {
                    for (int i = 0; i < HomeActivity.mEvents.size(); i++) {
                        Log.i ("startdate", HomeActivity.backupEvents.get(i).getStartTime());
                        String[] start = stringToHoursDate(HomeActivity.mEvents.get(i).getStartTime());
                        Log.i ("startdate0", start[0]);
                        Log.i ("startdate1", start[1]);
                        String[] end = stringToHoursDate(HomeActivity.mEvents.get(i).getEndTime());
                        if (Integer.parseInt(start[0]) <= startTimehour || Integer.parseInt(end[0]) >= endTimehour) {
                            HomeActivity.mEvents.remove(i);
                        }
                    }
                }
                if(HomeActivity.mEvents.size() == 0){
                    for (int i = 0; i < HomeActivity.backupEvents.size(); i++) {
                        String[] start = stringToHoursDate(HomeActivity.backupEvents.get(i).getStartTime());
                        String[] end = stringToHoursDate(HomeActivity.backupEvents.get(i).getEndTime());
                        if (Integer.parseInt(start[0]) >= startTimehour && Integer.parseInt(end[0]) <= endTimehour) {
                            HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
                        }
                    }
                }

                Log.i("Filters", ""+HomeActivity.mEvents.size());
                HomeActivity.eventsListView.invalidateViews();
                finish();

            }
        });

        button_ClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxPrice=100;
                minPrice=0;
                startTimehour=0;
                startTimemin=0;
                endTimehour=23;
                endTimemin=30;
                cuisine_American = true;
                cuisine_Indian = true;
                cuisine_Italian = true;
                cuisine_Mexican = true;
                rating1 = true;
                rating2 = true;
                rating3 = true;
                rating4 = true;
                rating5 = true;

                checkBox_American.setChecked(true);
                checkBox_Indian.setChecked(true);
                checkBox_Italian.setChecked(true);
                checkBox_Mexican.setChecked(true);
                editText_MaxValue.setText(String.valueOf(maxPrice));
                seekBar_MaxValue.setProgress(maxPrice);
                editText_MinValue.setText(String.valueOf(minPrice));
                seekBar_MinValue.setProgress(minPrice);
                editText_StartTime.setText(String.valueOf(startTimehour)+":"+String.valueOf(startTimemin));
                seekBar_StartTime.setProgress(mapStringToSeekbar(String.valueOf(startTimehour)+":"+String.valueOf(startTimemin)));
                editText_EndTime.setText(String.valueOf(endTimehour)+":"+String.valueOf(endTimemin));
                seekBar_EndTime.setProgress(mapStringToSeekbar(String.valueOf(endTimehour)+":"+String.valueOf(endTimemin)));

//                if(HomeActivity.backupEvents.size()==0){
//                    finish();
//                }
//                else {
//                    HomeActivity.mEvents.clear();
//                    for (int i = 0; i < HomeActivity.backupEvents.size(); i++) {
//                        HomeActivity.mEvents.add(HomeActivity.backupEvents.get(i));
//                    }
//                    HomeActivity.eventsListView.invalidateViews();
//                    finish();
//                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    String mapSeekbarToTime(int i){
        int minHour = i / 2;
        int minMinute = 30 * (i % 2);
        return ""+minHour+":"+minMinute;
    }

    int mapStringToSeekbar(String s){
        String[] a = new String[2];
        int progress;
        a = s.split(":");
        progress = Integer.parseInt(a[0])*2;
        progress = progress + (Integer.parseInt(a[1])/30);
        return  progress;
    }

    String[] stringToHoursDate(String s){
        String[] a = new String[2];
        a = s.split(":");
        return a;
    }
}