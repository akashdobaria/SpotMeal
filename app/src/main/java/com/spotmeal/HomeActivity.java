package com.spotmeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public static ListView eventsListView;
    private DatabaseReference mDatabase;
    private DatabaseReference mEventsReference;
    private ValueEventListener mEventsListener;
    private DatabaseReference mCurrentUserReference;
    private ValueEventListener mCurrentUserListener;
    private DatabaseReference mUsersReference;
    private ValueEventListener mUsersListener;

    private DatabaseReference mReviewsGivenReference;
    private DatabaseReference mReviewsReceivedReference;
    private ValueEventListener mReviewsGivenListener;
    private ValueEventListener mReviewsReceivedListener;
    public static ArrayList<Review> reviewsGiven = new ArrayList<>();
    public static ArrayList<Review> reviewsReceived = new ArrayList<>();


    private final static int CUSTOMIZE_AND_RESERVE = 1;

    public static ArrayList<Event>  backupEvents = new ArrayList<Event>();
    public static ArrayList<Event>  mEvents = new ArrayList<Event>();
    public static HashMap<String,User>  mUsers = new HashMap<String, User>();
    public static User mCurrentUser = new User();
    public static boolean guestMode = true;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private ArrayList<Integer> eventsThumbnail = new ArrayList<Integer>(
            Arrays.asList(R.drawable.event_image, R.drawable.event_image2, R.drawable.event_image3) );

    public static boolean american = false;
    public static boolean indian = false;
    public static boolean italian = false;
    public static boolean mexican = false;
    public static String minPrice = "-1";
    public static String maxPrice = "-1";
    public static String startTime = "-1";
    public static String endTime = "-1";

    LinearLayout linearLayout_FilterStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_guest_mode_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout_FilterStatus = (LinearLayout) findViewById(R.id.linearayout_FilterStatus);

        this.mLocationPermissionGranted = false;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.mEventsReference = mDatabase.child("events_p");
        this.mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("users_p").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.mUsersReference = FirebaseDatabase.getInstance().getReference().child("users_p");

        mReviewsGivenReference = mCurrentUserReference.child("ratings_given");
        mReviewsReceivedReference = mCurrentUserReference.child("ratings_recieved");
        getLocationPermission();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         /* Get list view */
        this.eventsListView = (ListView) findViewById(android.R.id.list );
        Log.i(TAG, "*** events size ** "+ mEvents.size());
        /* Set Array Adapter for the ListView */
        this.eventsListView.setAdapter( new EventAdapterGuestView(this, mEvents , this.eventsThumbnail));

        Button filters = (Button) findViewById(R.id.filters);
        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FiltersPopUpWindow.class);
                startActivity(intent);
            }
        });
        FloatingActionButton viewMap = (FloatingActionButton) findViewById(R.id.view_map);
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MapViewActivity.class);
                startActivity(intent);
            }
        });

        Log.i(TAG, "****************** Adding Listeners ****************");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting events - Guest Mode ****************");
                HomeActivity.mEvents.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    HomeActivity.mEvents.add(child.getValue(Event.class));
                }
                Log.i(TAG, HomeActivity.mEvents.size()+"****************** events found ****************");

                eventsListView.invalidateViews();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mEventsListener = eventListener;
        this.mEventsReference.addValueEventListener(mEventsListener);

        ValueEventListener currentUserListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting Current user details ****************");

                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String ,Object>map = dataSnapshot.getValue(t);
//                System.out.println("Getting User events map "+ map.toString());
                HomeActivity.mCurrentUser = new User();
                HomeActivity.mCurrentUser.setAboutMe((String) map.get("aboutMe"));
                HomeActivity.mCurrentUser.setName((String)map.get("name"));
                HomeActivity.mCurrentUser.setEmail((String)map.get("email"));
                HomeActivity.mCurrentUser.setAddressLine1((String)map.get("addressLine1"));
                HomeActivity.mCurrentUser.setAddressLine2((String)map.get("addressLine2"));
                HomeActivity.mCurrentUser.setContact((String) map.get("contact"));
                HomeActivity.mCurrentUser.setCountry((String)map.get("country"));
                HomeActivity.mCurrentUser.setState((String)map.get("state"));
                HomeActivity.mCurrentUser.setDateOfBirth((String)map.get("dateOfBirth"));
                HomeActivity.mCurrentUser.setEvents_attending((HashMap<String,Event>) map.get("events_attending"));
                HomeActivity.mCurrentUser.setEvents_hosting((HashMap<String,Event>) map.get("events_hosting"));
                HomeActivity.mCurrentUser.setGender((String)map.get("gender"));
                HomeActivity.mCurrentUser.setUid((String)map.get("uid"));
                HomeActivity.mCurrentUser.setRatings_given((HashMap<String,Review>) map.get("ratings_given"));
                HomeActivity.mCurrentUser.setRatings_recieved((HashMap<String,Review>) map.get("ratings_recieved"));

                View hView =  ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
                ((TextView)hView.findViewById(R.id.header_user_name_guest)).setText(mCurrentUser.getName());
                ((TextView)hView.findViewById(R.id.user_email_guest)).setText(mCurrentUser.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserListener = currentUserListener;
        mCurrentUserReference.addValueEventListener(mCurrentUserListener);


        ValueEventListener usersListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting All user details ****************");

                HomeActivity.mUsers.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    User user = child.getValue(User.class);
                    HomeActivity.mUsers.put(user.getUid(),user);
//                    Log.i(TAG, "****************** user ****************");
//                    Log.i(TAG, "****************** user ****************"+user.getRatings_recieved());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUsersListener = usersListener;
        mUsersReference.addValueEventListener(mUsersListener);

        ValueEventListener currentUserReviewsGivenListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting Reviews Given ****************");
                reviewsGiven.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    reviewsGiven.add(child.getValue(Review.class));
                }
                Log.i(TAG, "****************** got reviews! **************** "+reviewsGiven.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReviewsGivenListener = currentUserReviewsGivenListener;
        mReviewsGivenReference.addValueEventListener(mReviewsGivenListener);



        ValueEventListener currentUserReviewsReceivedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting Reviews Received ****************");
                reviewsReceived.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    reviewsReceived.add(child.getValue(Review.class));
                }
                Log.i(TAG, "****************** got reviews! **************** "+reviewsReceived.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReviewsReceivedListener = currentUserReviewsReceivedListener;
        mReviewsReceivedReference.addValueEventListener(mReviewsReceivedListener);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            // Handle the camera action
            Intent intent = new Intent(HomeActivity.this, ProfileViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.reviews) {
            Intent intent = new Intent(HomeActivity.this, DisplayReviews.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.upcoming_events) {
            startActivity(new Intent(this, UpcomingEventsGuest.class));

        } else if (id == R.id.switch_mode) {
            if(HomeActivity.guestMode){
                startActivity(new Intent(this, HostModeHomeActivity.class));
                HomeActivity.guestMode = false;

            }else {
                startActivity(new Intent(this, HomeActivity.class));
                HomeActivity.guestMode = true;
            }
            finish();
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        linearLayout_FilterStatus.removeAllViews();
        if(!(american && indian && mexican && italian)) {
            if (american != false) {
                Button button = new Button(HomeActivity.this);
                button.setText("American");
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
                buttonLayoutParams.setMargins(1, 1, 1, 1);
                button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mr_dialog_close_light, 0);
                linearLayout_FilterStatus.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parentView = (ViewGroup) view.getParent();
                        parentView.removeView(view);
                        american = false;
                    }
                });
            }
            if (indian != false) {
                Button button = new Button(HomeActivity.this);
                button.setText("Indian");
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
                buttonLayoutParams.setMargins(1, 1, 1, 1);
                button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mr_dialog_close_light, 0);
                linearLayout_FilterStatus.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parentView = (ViewGroup) view.getParent();
                        parentView.removeView(view);
                        indian = false;
                    }
                });
            }
            if (mexican != false) {
                Button button = new Button(HomeActivity.this);
                button.setText("Mexican");
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
                buttonLayoutParams.setMargins(1, 1, 1, 1);
                button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mr_dialog_close_light, 0);
                linearLayout_FilterStatus.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parentView = (ViewGroup) view.getParent();
                        parentView.removeView(view);
                        mexican = false;
                    }
                });
            }
            if (italian != false) {
                Button button = new Button(HomeActivity.this);
                button.setText("Italian");
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
                buttonLayoutParams.setMargins(1, 1, 1, 1);
                button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mr_dialog_close_light, 0);
                linearLayout_FilterStatus.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parentView = (ViewGroup) view.getParent();
                        parentView.removeView(view);
                        italian = false;
                    }
                });
            }
        }
        if(!minPrice.equals("-1")){
            Button button = new Button(HomeActivity.this);
            button.setText("Min Price("+minPrice+")");
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
            buttonLayoutParams.setMargins(1, 1, 1, 1);
            button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mr_dialog_close_light,0);
            linearLayout_FilterStatus.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parentView = (ViewGroup) view.getParent();
                    parentView.removeView(view);
                    minPrice = "-1";
                }
            });
        }
        if(!maxPrice.equals("-1")){
            Button button = new Button(HomeActivity.this);
            button.setText("Max Price("+maxPrice+")");
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
            buttonLayoutParams.setMargins(1, 1, 1, 1);
            button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mr_dialog_close_light,0);
            linearLayout_FilterStatus.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parentView = (ViewGroup) view.getParent();
                    parentView.removeView(view);
                    maxPrice = "-1";
                }
            });
        }
        if(!startTime.equals("-1")){
            Button button = new Button(HomeActivity.this);
            button.setText("Start time("+startTime+")");
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
            buttonLayoutParams.setMargins(1, 1, 1, 1);
            button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mr_dialog_close_light,0);
            linearLayout_FilterStatus.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parentView = (ViewGroup) view.getParent();
                    parentView.removeView(view);
                    startTime = "-1";
                }
            });
        }
        if(!endTime.equals("-1")){
            Button button = new Button(HomeActivity.this);
            button.setText("End time("+endTime+")");
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
            buttonLayoutParams.setMargins(1, 1, 1, 1);
            button.setLayoutParams(buttonLayoutParams);
//            Drawable img = HomeActivity.this.getResources().getDrawable(R.drawable.mr_dialog_close_light);
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.mr_dialog_close_light,0);
            linearLayout_FilterStatus.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parentView = (ViewGroup) view.getParent();
                    parentView.removeView(view);
                    endTime = "-1";
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "****************** Removing Listeners ****************");

        mCurrentUserReference.removeEventListener(mCurrentUserListener);
        mEventsReference.removeEventListener(mEventsListener);
        mUsersReference.removeEventListener(mUsersListener);
        mReviewsGivenReference.removeEventListener(mReviewsGivenListener);
        mReviewsReceivedReference.removeEventListener(mReviewsReceivedListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CUSTOMIZE_AND_RESERVE){
            if(resultCode == RESULT_OK){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your reservation has been made!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ACCESS_FINE_LOCATION permission already granted");

            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
            break;
        }

    }
}
