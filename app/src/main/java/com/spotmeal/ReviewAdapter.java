package com.spotmeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.spotmeal.models.Review;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by parneet on 12/2/17.
 */

public class ReviewAdapter extends BaseAdapter {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> mReviews;
    private List<View> mItems;
    private LayoutInflater mLayoutInflater;
    ArrayList<Integer> reviewStarImages = new ArrayList<>(Arrays.asList( R.drawable.star1,
            R.drawable.star2, R.drawable.star3, R.drawable.star4, R.drawable.star5));

    ReviewAdapter(Context context, ArrayList<Review> mReviews){
        super();
        Log.i(TAG, "******* mReviews.size() ******** "+mReviews.size());

        this.mReviews = mReviews;
        /* Set layout Inflater */
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mItems = new ArrayList<>(this.mReviews.size());
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG,"********************** getView Review List **********************");
        ReviewViewHolder holder = null;
        if(convertView == null){
            holder = new ReviewViewHolder();
            convertView = this.mLayoutInflater.inflate(R.layout.review_list_item,null);
            holder.name = ((TextView)convertView.findViewById(R.id.eventView_Reviews_Username));
            holder.stars =(ImageView)convertView.findViewById(R.id.eventView_Reviews_Stars);
            holder.review = (TextView)convertView.findViewById(R.id.eventView_Reviews_Description);
            holder.date = (TextView)convertView.findViewById(R.id.eventView_Reviews_Date);
            holder.name.setText("For:"+(this.mReviews.get(position)).getGivenTo_Name()+" By: "+(this.mReviews.get(position)).getGivenBy_Name());
            holder.stars.setImageResource(reviewStarImages.get(Integer.valueOf(this.mReviews.get(position).getRating().toString())-1));
            holder.review.setText(this.mReviews.get(position).getComment());
            holder.date.setText(this.mReviews.get(position).getDateOfReview());
            convertView.setTag(holder);
        }else {
            holder = (ReviewViewHolder) convertView.getTag();
        }
        this.mItems.add(convertView);
        return convertView;
    }
}


