package com.spotmeal;

/**
 * Created by Khushbu on 11/25/2017.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.spotmeal.models.Event;
import com.spotmeal.models.Review;
import com.spotmeal.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventDetails extends AppCompatActivity {

    private static final String TAG = EventDetails.class.getSimpleName();

    FrameLayout frameRel;
    Button evtButton;
    Button hstButton;
    Button rwButton;
    Event event;
    private DatabaseReference mDatabase;
    private DatabaseReference mEventHostReference;
    private ValueEventListener mEventHostListener;
    private ValueEventListener mhostReviewsListener;

    public static Event currentEvent;
    public static ArrayList<Review> hostReviews = new ArrayList<Review>();

    public static User host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_details);
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mEventHostReference = this.mDatabase.child("users_p").child(EventDetails.currentEvent.getUid());

        frameRel = (FrameLayout) findViewById(R.id.titles);
        evtButton = (Button) findViewById(R.id.eventButton);
        hstButton = (Button) findViewById(R.id.hostButton);
        rwButton = (Button) findViewById(R.id.reviewsButton);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        EventFragment event1 = new EventFragment();
        fragmentTransaction.replace(R.id.titles,event1);
        fragmentTransaction.commit();
        evtButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ((ImageButton)findViewById(R.id.close_event_details)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((Button)findViewById(R.id.view_on_map)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetails.this, MapViewActivity.class);
                intent.putExtra("lat", currentEvent.getLatitude());
                intent.putExtra("long", currentEvent.getLongitude());
                intent.putExtra("event_title", currentEvent.getHost().getName());
                startActivity(intent);
            }
        });
        ValueEventListener eventHostListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting Current user details ****************");

                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String ,Object>map = dataSnapshot.getValue(t);
                System.out.println("Getting User events map "+ map.toString());
                host = new User();
                host.setAboutMe((String) map.get("aboutMe"));
                host.setName((String)map.get("name"));
                host.setEmail((String)map.get("email"));
                host.setAddressLine1((String)map.get("addressLine1"));
                host.setAddressLine2((String)map.get("addressLine2"));
                host.setContact((String) map.get("contact"));
                host.setCity((String)map.get("city"));
                host.setCountry((String)map.get("country"));
                host.setState((String)map.get("state"));
                host.setDateOfBirth((String)map.get("dateOfBirth"));
                host.setEvents_attending((HashMap<String,Event>) map.get("events_attending"));
                host.setEvents_hosting((HashMap<String,Event>) map.get("events_hosting"));
                host.setGender((String)map.get("gender"));
                host.setUid((String)map.get("uid"));
                host.setRatings_given((HashMap<String,Review>) map.get("ratings_given"));
                host.setRatings_recieved((HashMap<String,Review>) map.get("ratings_recieved"));
                ((TextView)findViewById(R.id.user_profile_name)).setText(host.getName());
                ((TextView)findViewById(R.id.userAddress)).setText(host.getAddressLine1()+" "+host.getAddressLine2());
                ((TextView)findViewById(R.id.userCity)).setText(host.getCity()+" "+host.getState()+" "+host.getCountry());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mEventHostListener = eventHostListener;
        mEventHostReference.addValueEventListener(mEventHostListener);

        ValueEventListener currentUserReviewsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting events - Host Mode ****************");
                EventDetails.hostReviews.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    EventDetails.hostReviews.add(child.getValue(Review.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mhostReviewsListener = currentUserReviewsListener;
        mEventHostReference.child("ratings_recieved").getRef().addValueEventListener(mhostReviewsListener);

    }

    public void onRadioButtonClick(View view) {
        // Is the button now checked?
       // boolean checked = ((Button) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.eventButton:
               // if (checked){
                evtButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                hstButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                rwButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EventFragment event1 = new EventFragment();
                fragmentTransaction.replace(R.id.titles,event1,"EVENT");
                fragmentTransaction.commit();
                break;
            case R.id.hostButton:

                evtButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                hstButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                rwButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                HostDetails hosts = new HostDetails();
                fragmentTransaction2.replace(R.id.titles,hosts,"HOST");
                fragmentTransaction2.commit();
                break;

            case R.id.reviewsButton:
                evtButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                hstButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                rwButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                ReviewsFragment reviews = new ReviewsFragment();
                fragmentTransaction3.replace(R.id.titles,reviews,"REVIEWS");
                fragmentTransaction3.commit();


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventHostReference.removeEventListener(mEventHostListener);
    }
}
