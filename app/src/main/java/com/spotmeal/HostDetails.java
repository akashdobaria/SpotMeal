package com.spotmeal;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.spotmeal.models.Event;

/**
 * Created by Khushbu on 11/13/2017.
 */

    public class HostDetails extends Fragment{
    private static final String TAG = HostDetails.class.getSimpleName();

    @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.framehost, container, false);
            ((TextView)view.findViewById(R.id.host_details)).setText(EventDetails.host.getAboutMe());

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

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gesture.onTouchEvent(event);
                }
            });*/


            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }



    }


