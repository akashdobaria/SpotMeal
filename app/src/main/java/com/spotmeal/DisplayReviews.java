package com.spotmeal;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DisplayReviews extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static final String TAG = DisplayReviews.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_reviews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }
        tabLayout.getTabAt(0).setText("Review Received");
        tabLayout.getTabAt(1).setText("Review Given");


    }


    public static class ReviewsGivenFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static  int position= 0;
        public ReviewsGivenFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ReviewsGivenFragment newInstance(int sectionNumber) {
            ReviewsGivenFragment.position = sectionNumber;
            ReviewsGivenFragment fragment = new ReviewsGivenFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_given_reviews, container, false);
            ListView reviewListG = (ListView)rootView.findViewById(R.id.display_reviews_list_given);
            Log.i(TAG, "PlaceholderFragment.position"+ReviewsGivenFragment.position);
            Log.i(TAG, "~~~~~~~~~~~~~~~~ Setting Adapter ~~~~~~~ review count ~~~~~~~~~"+HomeActivity.reviewsGiven);
            reviewListG.setAdapter(new ReviewAdapter(getContext(), HomeActivity.reviewsGiven));
            return rootView;
        }
    }

    public static class ReviewsReceivedFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int position = 0;


        public ReviewsReceivedFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ReviewsReceivedFragment newInstance(int sectionNumber) {
            ReviewsReceivedFragment.position = sectionNumber;
            ReviewsReceivedFragment fragment = new ReviewsReceivedFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

             return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             super.onCreateView(inflater, container, savedInstanceState);

            View rootView =  inflater.inflate(R.layout.fragment_display_received_reviews, container, false);
            ListView reviewListR = (ListView) rootView.findViewById(R.id.display_reviews_list_received);
            Log.i(TAG, "~~~~~~~~~~~~~~~~ Setting Adapter ~~~~~~~ review count ~~~~~~~~~" + HomeActivity.reviewsReceived);
            reviewListR.setAdapter(new ReviewAdapter(getContext(), HomeActivity.reviewsReceived));
            return  rootView;
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ReviewsReceivedFragment.newInstance(position + 1);
                case 1:
                    return ReviewsGivenFragment.newInstance(position + 1);
                default:
                    return null;

            }

        }
        @Override
        public int getCount() {
            return 2;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_reviews, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.close_reviews) {
            Intent intent = new Intent(DisplayReviews.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
