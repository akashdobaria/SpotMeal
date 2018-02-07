package com.spotmeal;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spotmeal.models.Event;
import com.spotmeal.models.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by parneet on 11/12/17.
 */

public class EventAdapterHostView extends BaseAdapter {
    private static final String TAG = EventAdapterHostView.class.getSimpleName();

    private ArrayList<Event> mItems;
    private List<View> mEvents;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Integer> eventImages;

    EventAdapterHostView(Context context, ArrayList<Event> mEvents, ArrayList<Integer> eventImages){
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
        Log.i(TAG,"********************** getView List **********************");

        if(convertView == null){
            /*Inflate imageView from a layout file.*/
            convertView = this.mLayoutInflater.inflate(R.layout.host_event_list_item,null);
        }

        ((TextView)convertView.findViewById(R.id.event_view_title_host)).setText(this.mItems.get(position).getTitle());
        ((TextView)convertView.findViewById(R.id.cost_host)).setText("$"+ this.mItems.get(position).getCostPerPerson());
        ((TextView)convertView.findViewById(R.id.date_host)).setText(this.mItems.get(position).getEventDate());
        ((TextView)convertView.findViewById(R.id.seats_host)).setText("Max Guests: "+this.mItems.get(position).getMaxGuests().toString());

        HashMap<String ,Reservation> reservations = this.mItems.get(position).getReservations();
        ((ViewGroup)convertView.findViewById(R.id.reservation_details_view)).removeAllViews();
        if(reservations != null){
            int index = 1;
            for(Reservation reservation: reservations.values()){
                LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());

                TextView guestName = new TextView(viewGroup.getContext());
                TextView guestContact = new TextView(viewGroup.getContext());
                TextView noOfGuests = new TextView(viewGroup.getContext());
                TextView customization = new TextView(viewGroup.getContext());
                guestName.setText("\n#"+index+"\nGuest Name: "+reservation.getGuestName());
                guestContact.setText("Contact: "+reservation.getContact());
                noOfGuests.setText("\n\nNo of guests:"+ reservation.getNoOfSeats());
                customization.setText("Customizations: "+reservation.getCustomizations());

                guestName.setTextSize(16);
                guestContact.setTextSize(16);
                noOfGuests.setTextSize(16);
                customization.setTextSize(16);

                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                linearLayout.setLayoutParams(param1);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout linearLayoutLeft = new LinearLayout(viewGroup.getContext());
                LinearLayout linearLayoutRight = new LinearLayout(viewGroup.getContext());
                linearLayoutLeft.setOrientation(LinearLayout.VERTICAL);
                linearLayoutRight.setOrientation(LinearLayout.VERTICAL);

                linearLayoutLeft.setLayoutParams(param2);
                linearLayoutRight.setLayoutParams(param2);
                linearLayoutLeft.addView(guestName);
                linearLayoutLeft.addView(guestContact);
                linearLayoutRight.addView(noOfGuests);
                linearLayoutRight.addView(customization);

                linearLayout.addView(linearLayoutLeft);
                linearLayout.addView(linearLayoutRight);
                ((ViewGroup)convertView.findViewById(R.id.reservation_details_view)).addView(linearLayout);
                index +=1;
            }
        }
            /* Add grid item to items */
        this.mEvents.add(convertView);

        return convertView;
    }
}
