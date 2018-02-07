package com.spotmeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotmeal.models.User;

import org.w3c.dom.Text;

import java.util.Date;

public class ProfileViewActivity extends AppCompatActivity {

    Button updateProfile;
    ImageButton button_Close;

    TextView textView_name;
    TextView textView_address;
    TextView textView_dob;
    TextView textView_gender;
    TextView textView_aboutMe;
    TextView textView_userProfileName;

    String name;
    String addressLine1;
    String addressLine2;
    String state;
    String country;
    String dob;
    String gender;
    String aboutMe;

    //    private User currentUser = HomeActivity.mCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mUsersReference = mDatabase.child("users_p");

        loadUserData();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewActivity.this, ProfileUpdateActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("name",name);
                intent.putExtra("addressLine1", addressLine1);
                intent.putExtra("addressLine2", addressLine2);
                intent.putExtra("state", state);
                intent.putExtra("country", country);
                intent.putExtra("dob", dob.toString());
                intent.putExtra("gender", gender);
                intent.putExtra("aboutMe", aboutMe);
                startActivity(intent);

            }
        });

        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected void onResume(){
        super.onResume();
        loadUserData();

    }

    private void loadUserData(){



//        name = mUsersReference.child(currentUser.getGivenBy()).child("name").ge;

        name = HomeActivity.mCurrentUser.getName();
        addressLine1 = HomeActivity.mCurrentUser.getAddressLine1();
        addressLine2 = HomeActivity.mCurrentUser.getAddressLine2();
        state = HomeActivity.mCurrentUser.getState();
        country = HomeActivity.mCurrentUser.getCountry();
        dob = HomeActivity.mCurrentUser.getDateOfBirth();
        gender = HomeActivity.mCurrentUser.getGender();
        aboutMe = HomeActivity.mCurrentUser.getAboutMe();

//        if (this.getIntent().getFlags() != Intent.FLAG_ACTIVITY_NEW_TASK  && this.getIntent().getExtras()/*.getString("name")*/ != null){
//            name = this.getIntent().getExtras().getString("name");
//            addressLine1 = this.getIntent().getExtras().getString("addressLine1");
//            addressLine2 = this.getIntent().getExtras().getString("addressLine2");
//            state = this.getIntent().getExtras().getString("state");
//            country = this.getIntent().getExtras().getString("country");
//            dob = this.getIntent().getExtras().getString("dob");
//            gender = this.getIntent().getExtras().getString("gender");
//            aboutMe = this.getIntent().getExtras().getString("aboutMe");
//        }

        textView_userProfileName = (TextView) findViewById(R.id.user_profile_name);
        textView_userProfileName.setText(name);

        textView_name = (TextView) findViewById(R.id.textview_ProfileView_Data_Name);
        textView_name.setText(name);

        textView_address = (TextView) findViewById(R.id.textview_ProfileView_Data_Address);
        textView_address.setText(addressLine1 + "\n" + addressLine2 + "\n" + state + ", " + country);

        textView_dob = (TextView) findViewById(R.id.textview_ProfileView_Data_DOB);
        textView_dob.setText(dob);

        textView_gender = (TextView) findViewById(R.id.textview_ProfileView_Data_Gender);
        textView_gender.setText(gender);

        textView_aboutMe = (TextView) findViewById(R.id.textview_ProfileView_Data_AboutMe);
        textView_aboutMe.setText(aboutMe);

        updateProfile = (Button) findViewById(R.id.button_ChangeProfile);
        button_Close = (ImageButton) findViewById(R.id.button_Close);
    }

}
