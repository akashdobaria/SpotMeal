package com.spotmeal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.spotmeal.models.Event;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by parneet on 12/4/17.
 */

public class UpcomingEventsListAdapter extends BaseAdapter {
    ArrayList<Event> mItems;
    private List<View> mEvents;
    private static final String TAG = UpcomingEventsListAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private boolean mLocationPermissionGranted = false;


    UpcomingEventsListAdapter(Context context, ArrayList<Event> events){
        this.mItems = events;
        this.mLayoutInflater = LayoutInflater.from(context);
        mEvents = new ArrayList<>(this.mItems.size());

    }
    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            Log.i(TAG, "********************** getView Guest Event List **********************");
            if (convertView == null) {
            /*Inflate imageView from a layout file.*/
                convertView = this.mLayoutInflater.inflate(R.layout.upcoming_guest_event_list_item, null);
            }
            ((TextView) convertView.findViewById(R.id.event_view_title)).setText(this.mItems.get(position).getTitle());
            ((TextView) convertView.findViewById(R.id.tags)).setText(this.mItems.get(position).getCuisine());
            ((TextView) convertView.findViewById(R.id.cost)).setText("$" + this.mItems.get(position).getCostPerPerson());
            ((TextView) convertView.findViewById(R.id.seats)).setText("Max guests " + this.mItems.get(position).getMaxGuests());

            Log.i(TAG, "~~~~~~~ Set event date ~~~~~~~");
            ((TextView) convertView.findViewById(R.id.date)).setText(this.mItems.get(position).getEventDate());
            Log.i(TAG, "~~~~~~~ Set event time ~~~~~~~");

            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date startTimeDateObj, endTimeDateObj;
            try {
                startTimeDateObj = sdf.parse(this.mItems.get(position).getStartTime());
                endTimeDateObj = sdf.parse(this.mItems.get(position).getEndTime());
                ((TextView) convertView.findViewById(R.id.event_time)).setText(new SimpleDateFormat("K:mm aa").format(startTimeDateObj) + " to " + new SimpleDateFormat("K:mm aa").format(endTimeDateObj));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String address = getAddress(Double.valueOf(this.mItems.get(position).getLatitude()), Double.valueOf(this.mItems.get(position).getLongitude()), convertView);
            ((TextView) convertView.findViewById(R.id.hostAddress)).setText(address);
            Log.i(TAG, "~~~~~~~ Set event location ~~~~~~~");
            LocationManager locationManager = (LocationManager) convertView.getContext().getSystemService(Context.LOCATION_SERVICE);
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
            if (mLocationPermissionGranted && currentLocation != null) {
                Log.i(TAG, "~~~~~~ upldating distance to the event ~~~~~~~" + result[0] * 0.000621371180556);

                LatLng eventLocation = new LatLng(Double.valueOf(this.mItems.get(position).getLatitude()), Double.valueOf(this.mItems.get(position).getLongitude()));
                android.location.Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                        Double.valueOf(this.mItems.get(position).getLatitude()), Double.valueOf(this.mItems.get(position).getLongitude()), result);
                ((TextView) convertView.findViewById(R.id.distance)).setText(String.valueOf(decimalFormat.format(result[0] * 0.000621371180556)) + " miles");
            }

        /* Set click listener for the grid items */
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.i(TAG, "************************list onClick key **********************" + mItems.get(position).getKey());
//                    Intent intent = new Intent(view.getContext(), EventDetails.class);
//                    EventDetails.currentEvent = mItems.get(position);
//                    view.getContext().startActivity(intent);
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

   public String  getAddress(double lat, double lng, View view) {
        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);

            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return null;
   }
}
