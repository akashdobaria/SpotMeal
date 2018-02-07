package com.spotmeal;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotmeal.models.Event;
import com.spotmeal.models.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Khushbu on 11/13/2017.
 */

public class EventFragment extends Fragment {
    private static final String TAG = EventFragment.class.getSimpleName();

    Button customizeButton;
    Button reserveButton;
    private DatabaseReference mCurrentEventHostReference;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
    EventFragment eventFragmentRef;
    private final static int CUSTOMIZE_AND_RESERVE = 1;
    private boolean mLocationPermissionGranted = false;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.frameevents, container, false);

        this.mCurrentEventHostReference = FirebaseDatabase.getInstance().getReference().child("users_p").child(EventDetails.currentEvent.getUid());
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mUsersReference = this.mDatabase.child("users_p");
        eventFragmentRef = this;
        List<Long> spinnerArray =  new ArrayList<Long>();
        for(long i = EventDetails.currentEvent.getMinGuests(); i<= EventDetails.currentEvent.getMaxGuests(); i++ ){
            spinnerArray.add(i);
        }
        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(
                view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner seatsToBeReserved = (Spinner) view.findViewById(R.id.select_no_of_guests);
        seatsToBeReserved.setAdapter(adapter);

        customizeButton = (Button) view.findViewById(R.id.customizeID);
        reserveButton = (Button) view.findViewById(R.id.reserveID);
        customizeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), CustomizeActivity.class);
                i.putExtra("seatsToBeReserved", (Long) seatsToBeReserved.getSelectedItem());
                startActivityForResult(i,CUSTOMIZE_AND_RESERVE);
                }
        });

        ((TextView)view.findViewById(R.id.event_details_event_title)).setText(EventDetails.currentEvent.getTitle());
        ((TextView)view.findViewById(R.id.event_details_cost)).setText(EventDetails.currentEvent.getCostPerPerson().toString());
        ((TextView)view.findViewById(R.id.event_details_cuisine)).setText(EventDetails.currentEvent.getCuisine());
        ((TextView)view.findViewById(R.id.event_details_numberOfGuests)).setText(EventDetails.currentEvent.getMaxGuests().toString());
        if(EventDetails.currentEvent.getHost().getRatings_recieved() != null){
            ((TextView)view.findViewById(R.id.event_details_no_of_ratings)).setText(EventDetails.currentEvent.getHost().getRatings_recieved().size());
        }
        Log.i(TAG, "~~~~~~~ Set event date ~~~~~~~");
//        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
//        ((TextView)view.findViewById(R.id.event_details_date)).setText(dateFormat.format(new Date(EventDetails.currentEvent.getEventDate())));
        ((TextView)view.findViewById(R.id.event_details_date)).setText(EventDetails.currentEvent.getEventDate());

        Log.i(TAG, "~~~~~~~ Set event time ~~~~~~~");

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        final Date startTimeDateObj, endTimeDateObj;
        try {
            startTimeDateObj = sdf.parse(EventDetails.currentEvent.getStartTime());
            endTimeDateObj = sdf.parse(EventDetails.currentEvent.getEndTime());
            ((TextView)view.findViewById(R.id.event_details_time)).setText(new SimpleDateFormat("K:mm aa").format(startTimeDateObj)+" to "+new SimpleDateFormat("K:mm aa").format(endTimeDateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "~~~~~~~ Set event location ~~~~~~~");
        LocationManager locationManager = (LocationManager)  view.getContext().getSystemService(Context.LOCATION_SERVICE);
        Location currentLocation = null;
        MapViewActivity map = new MapViewActivity();
        if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ACCESS_FINE_LOCATION permission already granted");
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLocationPermissionGranted = true;
        }

        ((TextView)view.findViewById(R.id.starter1)).setText(EventDetails.currentEvent.getStarters().get(0));
        ((TextView)view.findViewById(R.id.starter2)).setText(EventDetails.currentEvent.getStarters().get(1));
        ((TextView)view.findViewById(R.id.entree1)).setText(EventDetails.currentEvent.getMainCourse().get(0));
        ((TextView)view.findViewById(R.id.entree2)).setText(EventDetails.currentEvent.getMainCourse().get(1));
        ((TextView)view.findViewById(R.id.desert1)).setText(EventDetails.currentEvent.getDeserts().get(0));
        ((TextView)view.findViewById(R.id.desert2)).setText(EventDetails.currentEvent.getDeserts().get(1));
        ((TextView)view.findViewById(R.id.drink1)).setText(EventDetails.currentEvent.getDrinks().get(0));
        ((TextView)view.findViewById(R.id.drink2)).setText(EventDetails.currentEvent.getDrinks().get(1));

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new AlertDialog.Builder(view.getContext())
                        .setMessage("Proceed with reservation ?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> map = new HashMap<String, Object>();

                                map.put("contact", HomeActivity.mCurrentUser.getContact());
                                map.put("guestName",HomeActivity.mCurrentUser.getName());
                                map.put("noOfSeats",seatsToBeReserved.getSelectedItem());
                                map.put("customizations","None");
                                mCurrentEventHostReference.child("events_hosting").child(EventDetails.currentEvent.getKey()).child("reservations").push().setValue(map);
                                mUsersReference.child(HomeActivity.mCurrentUser.getUid()).child("events_attending").child(EventDetails.currentEvent.getKey()).setValue(EventDetails.currentEvent);
                                getActivity().setResult(RESULT_OK);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Your reservation has been made!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                getActivity().finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }).create().show();
            }
        });
       /* final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i(TAG, "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        FragmentManager fragmentManager = getFragmentManager();
                        EventFragment eventFragment = (EventFragment) fragmentManager.findFragmentByTag("EVENT");
                        HostDetails hostDetails = (HostDetails) fragmentManager.findFragmentByTag("HOST");
                        ReviewsFragment reviewsFragment = (ReviewsFragment) fragmentManager.findFragmentByTag("REVIEWS");

                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Right to Left");
                                if(eventFragment != null && eventFragment.isVisible()) {
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    HostDetails hosts = new HostDetails();
                                    fragmentTransaction.replace(R.id.titles,hosts,"HOST");
                                    fragmentTransaction.commit();

                                }else if(hostDetails != null && hostDetails.isVisible()){
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    ReviewsFragment reviewsFragment1 = new ReviewsFragment();
                                    fragmentTransaction.replace(R.id.titles,reviewsFragment1,"REVIEWS");
                                    fragmentTransaction.commit();
                                }else if(reviewsFragment != null && reviewsFragment.isVisible()){

                                }

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Left to Right");
                                if(eventFragment != null && eventFragment.isVisible()) {

                                }else if(hostDetails != null && hostDetails.isVisible()){

                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    EventFragment eventFragment1 = new EventFragment();
                                    fragmentTransaction.replace(R.id.titles,eventFragment1,"EVENT");
                                    fragmentTransaction.commit();


                                }else if(reviewsFragment != null && reviewsFragment.isVisible()){
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    HostDetails hostDetails1 = new HostDetails();
                                    fragmentTransaction.replace(R.id.titles,hostDetails,"HOST");
                                    fragmentTransaction.commit();
                                }

                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });*/

        return view;
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CUSTOMIZE_AND_RESERVE){
            if(resultCode == RESULT_OK){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Your reservation has been made!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
