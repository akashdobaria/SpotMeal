package com.spotmeal;

        import android.app.AlertDialog;
        import android.app.Fragment;
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.app.ListFragment;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.design.widget.FloatingActionButton;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.RatingBar;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.spotmeal.models.Event;
        import com.spotmeal.models.Review;
        import com.spotmeal.models.User;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by Akash on 11/13/2017.
 */

public class ReviewsFragment extends Fragment{

    private static final String TAG = ReviewsFragment.class.getSimpleName();

    public static ListView reviewListView;
/*
    private ArrayList<Review> reviews;
*/
    User host_user = new User();
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
    private DatabaseReference mCurrentUserReference;
    private ValueEventListener mCurrentUserListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get database reference
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        //get reference to events
        this.mUsersReference = this.mDatabase.child("users_p");
        //get database reference
        this.mCurrentUserReference = this.mUsersReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View resultView =  inflater.inflate(R.layout.framereviews, container, false);
        ValueEventListener currentUserEventsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "****************** getting events - Host Mode ****************");
                HostModeHomeActivity.currentUserEvents.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    HostModeHomeActivity.currentUserEvents.add(child.getValue(Event.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mCurrentUserListener = currentUserEventsListener;
        mCurrentUserReference.child("events_hosting").getRef().addValueEventListener(mCurrentUserListener);

        /*final GestureDetector gesture = new GestureDetector(getActivity(),
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

        resultView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });*/


        return resultView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((FloatingActionButton)getActivity().findViewById(R.id.add_review)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Write Review");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_review_layout, (ViewGroup) getView(), false);
                // Set up the input
                final EditText reviewComments = (EditText) viewInflated.findViewById(R.id.review_comment);
                final RatingBar ratingBar = (RatingBar) viewInflated.findViewById(R.id.ratingBar);
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String reviewComment = reviewComments.getText().toString();
                        float rating = ( ratingBar.getRating());
                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                        Calendar cal = Calendar.getInstance();
                        String  date = dateFormat.format(new Date());
//                        System.out.println(dateFormat.format(cal));
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("rating", rating);
                        map.put("comment", reviewComment);
                        map.put("givenBy", HomeActivity.mCurrentUser.getUid());
                        map.put("givenBy_Name", HomeActivity.mCurrentUser.getName());
                        map.put("givenTo", EventDetails.currentEvent.getUid());
                        map.put("givenTo_Name", EventDetails.host.getName());
                        map.put("dateOfReview",date);
                        mUsersReference.child(HomeActivity.mCurrentUser.getUid()).child("ratings_given").push().setValue(map);
                        mUsersReference.child(EventDetails.currentEvent.getUid()).child("ratings_recieved").push().setValue(map);

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        if(EventDetails.host.getUid().equalsIgnoreCase(HomeActivity.mCurrentUser.getUid())){
            Log.i(TAG, "~~~~~~~~~~~~~ add_review hide ~~~~~~~~~~~~~" +EventDetails.host.getUid()+"~~~"+HomeActivity.mCurrentUser.getUid());
            ((FloatingActionButton)getActivity().findViewById(R.id.add_review)).setVisibility(View.GONE);

        }else {
            Log.i(TAG, "~~~~~~~~~~~~~ add_review show ~~~~~~~~~~~~~" +EventDetails.host.getUid()+"~~~"+HomeActivity.mCurrentUser.getUid());
            ((FloatingActionButton)getActivity().findViewById(R.id.add_review)).setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "****************** onViewCreated****************");

        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, EventDetails.hostReviews.size()+" ~~~~~~~ Reviews found for user ~~~~~~~~~~");

        ((ListView)view.findViewById(R.id.review_list)).setAdapter(new ReviewAdapter(getActivity(), EventDetails.hostReviews));

    }
}
