package com.spotmeal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.spotmeal.models.Event;
import com.spotmeal.models.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.spotmeal.HomeActivity.mEvents;

public class UpcomingEventsGuest extends AppCompatActivity {
    ListView upcomingEventsListView;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserReference;
    private ValueEventListener mGuestUpcomingEventsListener;
    ArrayList<Event> upcomingEvents = new ArrayList<Event>();
    private static final String TAG = UpcomingEventsGuest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_events_list_guest);
        ((TextView)findViewById(R.id.message_upcoming)).setVisibility(View.GONE);
       /* ((ImageButton)findViewById(R.id.button_Close_upcoming_events)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get database reference
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        //get reference to events
        this.mUserReference = this.mDatabase.child("users_p").child(HomeActivity.mCurrentUser.getUid());

        ValueEventListener upcomngEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting events - Host Mode ****************");
                upcomingEvents.clear();

                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Map<String ,Object>map = child.getValue(t);
                    Event event = new Event();
                    event = event.toEventObject((HashMap<String, Object>) map);
                    upcomingEvents.add(event);
                    Log.i(TAG, "****************** got event to attend ****************");

                }
                ((UpcomingEventsListAdapter)upcomingEventsListView.getAdapter()).notifyDataSetChanged();
                upcomingEventsListView.invalidateViews();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mGuestUpcomingEventsListener = upcomngEventsListener;
        this.mUserReference.child("events_attending").getRef().addValueEventListener(mGuestUpcomingEventsListener);


        Log.i(TAG,"~~~~~~~~~~~ upcomingEvents ~~~~~~~~~"+upcomingEvents.toString());
        this.upcomingEventsListView = (ListView) findViewById(android.R.id.list );
        this.upcomingEventsListView.setAdapter( new UpcomingEventsListAdapter(this, upcomingEvents ));

        if(upcomingEvents.size() == 0){
            ((TextView)findViewById(R.id.message_upcoming)).setVisibility(View.GONE);
        }else {
            ((TextView)findViewById(R.id.message_upcoming)).setVisibility(View.VISIBLE);

        }

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        super.onCreateView(parent, name, context, attrs);

        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.close_upcoming_events) {
            Intent intent = new Intent(UpcomingEventsGuest.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
