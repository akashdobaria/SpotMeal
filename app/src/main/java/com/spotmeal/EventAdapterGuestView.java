package com.spotmeal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.spotmeal.models.Event;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by parneet on 11/12/17.
 */

public class EventAdapterGuestView extends BaseAdapter {
    private static final String TAG = EventAdapterGuestView.class.getSimpleName();

    private ArrayList<Event> mItems;
    private List<View> mEvents;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> eventImages;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;

    EventAdapterGuestView(Context context, ArrayList<Event> mEvents, ArrayList<Integer> eventImages){
        super();
        this.mItems = mEvents;
        this.eventImages = eventImages;
        /* Set layout Inflater */
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mEvents = new ArrayList<>(this.mItems.size());
    }

    @Override
    public int getCount() {

        return mItems.size();
    }

    @Override
    public Object getItem(int position) {

        return this.mEvents.get(position);
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Log.i(TAG,"********************** getView Guest Event List **********************");


        if(convertView == null){
            /*Inflate imageView from a layout file.*/
            convertView = this.mLayoutInflater.inflate(R.layout.guest_event_list_item,null);
        }
        ((TextView)convertView.findViewById(R.id.event_view_title)).setText(this.mItems.get(position).getTitle());
        ((TextView)convertView.findViewById(R.id.tags)).setText(this.mItems.get(position).getCuisine());
        ((TextView)convertView.findViewById(R.id.cost)).setText("$"+ this.mItems.get(position).getCostPerPerson());
        ((TextView)convertView.findViewById(R.id.seats)).setText("Max guests "+this.mItems.get(position).getMaxGuests());
        ((ImageView)convertView.findViewById(R.id.event_image)).setImageResource(this.eventImages.get(position%3));
        if(this.mItems.get(position).getHost().getRatings_recieved() != null){
            ((TextView)convertView.findViewById(R.id.no_of_ratings)).setText(this.mItems.get(position).getHost().getRatings_recieved().size());
        }

        Log.i(TAG, "~~~~~~~ Set event date ~~~~~~~");
        /*DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");*/
//        ((TextView)convertView.findViewById(R.id.date)).setText(dateFormat.format(new Date(this.mItems.get(position).getEventDate())));

        ((TextView)convertView.findViewById(R.id.date)).setText(this.mItems.get(position).getEventDate());


        Log.i(TAG, "~~~~~~~ Set event time ~~~~~~~");

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        final Date startTimeDateObj, endTimeDateObj;
        try {
            startTimeDateObj = sdf.parse(this.mItems.get(position).getStartTime());
            endTimeDateObj = sdf.parse(this.mItems.get(position).getEndTime());
            ((TextView)convertView.findViewById(R.id.event_time)).setText(new SimpleDateFormat("K:mm aa").format(startTimeDateObj)+" to "+new SimpleDateFormat("K:mm aa").format(endTimeDateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "~~~~~~~ Set event location ~~~~~~~");
        LocationManager locationManager = (LocationManager)  convertView.getContext().getSystemService(Context.LOCATION_SERVICE);
        Location currentLocation = null;
        MapViewActivity map = new MapViewActivity();
        if (ContextCompat.checkSelfPermission(convertView.getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ACCESS_FINE_LOCATION permission already granted");
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLocationPermissionGranted = true;
        }
        float[] result = new float[1];
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if(mLocationPermissionGranted && currentLocation!= null){
            Log.i(TAG, "~~~~~~ upldating distance to the event ~~~~~~~"+result[0]*0.000621371180556);

            LatLng eventLocation = new LatLng(Double.valueOf(this.mItems.get(position).getLatitude()),Double.valueOf(this.mItems.get(position).getLongitude()));
            android.location.Location.distanceBetween(currentLocation.getLatitude(),currentLocation.getLongitude(),
                    Double.valueOf(this.mItems.get(position).getLatitude()),Double.valueOf(this.mItems.get(position).getLongitude()),result);
            ((TextView)convertView.findViewById(R.id.distance)).setText(String.valueOf(decimalFormat.format(result[0]*0.000621371180556))+" miles");
        }

        /* Set click listener for the grid items */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"************************list onClick key **********************"+mItems.get(position).getKey());
                Intent intent = new Intent(view.getContext(), EventDetails.class);
                EventDetails.currentEvent = mItems.get(position);
                view.getContext().startActivity(intent);
            }
        });
            /* Add grid item to items */
        this.mEvents.add(convertView);

            /* Set on long click listener for the grid items */
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        return convertView;
    }

}
