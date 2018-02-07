package com.spotmeal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotmeal.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ProfileUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Spinner spinner_state;
    Spinner spinner_country;
    Spinner spinner_gender;

    EditText editText_name;
    EditText editText_addressLine1;
    EditText editText_addressLine2;
    EditText editText_dob;
    EditText editText_aboutMe;

    String name;
    String addressLine1;
    String addressLine2;
    String state;
    String country;
    String dob;
    String gender;
    String aboutMe;

    TextView characterCount;

    Button button_saveChanges;
    ImageButton button_Close;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
//    private User currentUser = HomeActivity.mCurrentUser;
//    private User updatedInfo = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mUsersReference = mDatabase.child("users_p");

        editText_name = (EditText) findViewById(R.id.edittext_ProfileUpdate_Data_Name);
        name = this.getIntent().getExtras().getString("name");
        editText_name.setText(name);

        editText_addressLine1 = (EditText) findViewById(R.id.edittext_ProfileUpdate_Data_Address1);
        addressLine1 = this.getIntent().getExtras().getString("addressLine1");
        editText_addressLine1.setText(addressLine1);

        editText_addressLine2 = (EditText) findViewById(R.id.edittext_ProfileUpdate_Data_Address2);
        addressLine2 = this.getIntent().getExtras().getString("addressLine2");
        editText_addressLine2.setText(addressLine2);
        button_Close = (ImageButton) findViewById(R.id.button_Close_update);
        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinner_state = (Spinner) findViewById(R.id.spinner_ProfileUpdate_Data_State);
        final ArrayAdapter<CharSequence> adapter_state = ArrayAdapter.createFromResource(this,R.array.states_usa, android.R.layout.simple_spinner_item);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter_state);
        state = this.getIntent().getExtras().getString("state");
        spinner_state.setSelection(adapter_state.getPosition(state));

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                spinner_state.setSelection(pos);
                state = adapter_state.getItem(pos).toString();
                Log.i("State Selected", state.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        spinner_country = (Spinner) findViewById(R.id.spinner_ProfileUpdate_Data_Country);
        final ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_country.setAdapter(adapter_country);
        country = this.getIntent().getExtras().getString("country");
        spinner_country.setSelection(adapter_country.getPosition(country));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                spinner_country.setSelection(pos);
                country = adapter_country.getItem(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        spinner_gender = (Spinner) findViewById(R.id.spinner_ProfileUpdate_Data_Gender);
        final ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,R.array.array_gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        gender = this.getIntent().getExtras().getString("gender");
        spinner_gender.setSelection(adapter_gender.getPosition(gender));
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                spinner_gender.setSelection(pos);
                gender = adapter_gender.getItem(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        editText_dob = (EditText) findViewById(R.id.edittext_ProfileUpdate_Data_DOB);
        dob = this.getIntent().getExtras().getString("dob");
        editText_dob.setText(dob);
        editText_dob.setInputType(InputType.TYPE_NULL);;
        editText_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        editText_aboutMe = (EditText) findViewById(R.id.edittext_ProfileUpdate_Data_AboutMe);
        aboutMe = this.getIntent().getExtras().getString("aboutMe");
        editText_aboutMe.setText(aboutMe);

        characterCount = (TextView) findViewById(R.id.textView_ProfileUpdate_CharacterCount);
        characterCount.setText(aboutMe.length()+"/500");

        final TextWatcher txwatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                characterCount.setText(String.valueOf(s.length())+"/500");
            }

            public void afterTextChanged(Editable s) {
            }
        };

        editText_aboutMe.addTextChangedListener(txwatcher);

        button_saveChanges = (Button) findViewById(R.id.button_SaveProfile);
        button_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText_name.getText().toString();
                addressLine1 = editText_addressLine1.getText().toString();
                addressLine2 = editText_addressLine2.getText().toString();
                aboutMe = editText_aboutMe.getText().toString();

                HomeActivity.mCurrentUser.setName(name);
                HomeActivity.mCurrentUser.setContact(HomeActivity.mCurrentUser.getContact());
                HomeActivity.mCurrentUser.setAddressLine1(addressLine1);
                HomeActivity.mCurrentUser.setAddressLine2(addressLine2);
                HomeActivity.mCurrentUser.setState(state);
                HomeActivity.mCurrentUser.setCountry(country);
                HomeActivity.mCurrentUser.setDateOfBirth(dob.toString());
                HomeActivity.mCurrentUser.setGender(gender);
                HomeActivity.mCurrentUser.setAboutMe(aboutMe);
                HomeActivity.mCurrentUser.setUid(HomeActivity.mCurrentUser.getUid());
                mUsersReference.child(HomeActivity.mCurrentUser.getUid()).setValue(HomeActivity.mCurrentUser);
                finish();

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Warning");
            alertbox.setMessage("Discard changes?");

            alertbox.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });

            alertbox.setNeutralButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertbox.show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        dob = (month+1) + "/" + day + "/" + year;
        editText_dob.setText(dob);
    }

}
