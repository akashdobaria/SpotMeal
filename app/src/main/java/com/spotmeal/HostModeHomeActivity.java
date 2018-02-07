package com.spotmeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotmeal.models.Event;
import com.spotmeal.models.Review;

import java.util.ArrayList;
import java.util.Arrays;

public class HostModeHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HostModeHomeActivity.class.getSimpleName();

    private DatabaseReference mCurrentUserReference;
    private ValueEventListener mCurrentUserListener;
    private DatabaseReference mEventsReference;
    private ValueEventListener mEventsListener;
    private ListView userEventsListView;
    public static ArrayList<Event> currentUserEvents = new ArrayList<Event>();

    private ArrayList<Integer> eventsThumbnail = new ArrayList<Integer>(
            Arrays.asList(R.drawable.event_image, R.drawable.event_image2, R.drawable.event_image3) );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_host_mode_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        ((TextView)hView.findViewById(R.id.header_user_name_host)).setText(HomeActivity.mCurrentUser.getName());
        ((TextView)hView.findViewById(R.id.user_email_host)).setText(HomeActivity.mCurrentUser.getEmail());


        //get database reference
        this.mCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("users_p").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.mEventsReference = FirebaseDatabase.getInstance().getReference().child("events_p");

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

         /* Get list view */
        this.userEventsListView = (ListView) findViewById(android.R.id.list );
        Log.i(TAG, "*** events size ** "+currentUserEvents.size());

        /* Set Array Adapter for the ListView */
        this.userEventsListView.setAdapter( new EventAdapterHostView(this, HostModeHomeActivity.currentUserEvents , this.eventsThumbnail));

        FloatingActionButton createEvent = (FloatingActionButton) findViewById(R.id.add_event);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostModeHomeActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(HostModeHomeActivity.this, ProfileViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.reviews) {
            Intent intent = new Intent(HostModeHomeActivity.this, DisplayReviews.class);
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
        {
            super.onStart();
            Log.i(TAG, "****************** Adding Listeners ****************");

            ValueEventListener currentUserEventsListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "****************** getting events - Host Mode ****************");
                    HostModeHomeActivity.currentUserEvents.clear();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        HostModeHomeActivity.currentUserEvents.add(child.getValue(Event.class));
                    }
                    userEventsListView.invalidateViews();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrentUserListener = currentUserEventsListener;
            mCurrentUserReference.child("events_hosting").getRef().addValueEventListener(mCurrentUserListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "****************** Removing Listeners ****************");

        mCurrentUserReference.removeEventListener(mCurrentUserListener);

    }
}
