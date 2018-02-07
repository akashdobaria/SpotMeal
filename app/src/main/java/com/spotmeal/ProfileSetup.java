package com.spotmeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotmeal.models.Event;
import com.spotmeal.models.Review;
import com.spotmeal.models.User;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ProfileSetup extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setup);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mUsersReference = mDatabase.child("users_p");
        ((Button)findViewById(R.id.skip_setup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileSetup.this, HomeActivity.class));
                finish();

            }
        });

        Spinner state = (Spinner) findViewById(R.id.user_state);
        final ArrayAdapter<CharSequence> adapter_state = ArrayAdapter.createFromResource(this,R.array.states_usa, android.R.layout.simple_spinner_item);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter_state);
        state.setSelection(adapter_state.getPosition("IL"));


        Spinner country = (Spinner) findViewById(R.id.user_country);
        final ArrayAdapter<CharSequence> adapter_country = ArrayAdapter.createFromResource(this,R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter_country);
        country.setSelection(adapter_country.getPosition("United States"));


        Spinner gender = (Spinner) findViewById(R.id.user_gender);
        final ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,R.array.array_gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter_gender);
        gender.setSelection(adapter_gender.getPosition("Male"));

        ((Button)findViewById(R.id.update_setup_details)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                user.setGender(((Spinner)findViewById(R.id.user_gender)).getSelectedItem().toString());
                user.setCity(((TextView)findViewById(R.id.user_city)).getText().toString());
                user.setState(((Spinner)findViewById(R.id.user_state)).getSelectedItem().toString());
                user.setCountry(((Spinner)findViewById(R.id.user_country)).getSelectedItem().toString());
                user.setAddressLine1(((TextView)findViewById(R.id.user_address1)).getText().toString());
                user.setAddressLine2(((TextView)findViewById(R.id.user_address2)).getText().toString());
                user.setName(((TextView)findViewById(R.id.user_name)).getText().toString());
                user.setAboutMe(((TextView)findViewById(R.id.user_about)).getText().toString());
                user.setContact(((TextView)findViewById(R.id.user_contact)).getText().toString());
                user.setDateOfBirth(((TextView)findViewById(R.id.user_dob)).getText().toString());

                mUsersReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                startActivity(new Intent(ProfileSetup.this, HomeActivity.class));
                finish();

            }
        });


    }
}
