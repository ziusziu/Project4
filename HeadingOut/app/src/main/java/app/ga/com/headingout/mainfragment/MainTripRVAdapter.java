package app.ga.com.headingout.mainfragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.model.TripDestination;

/**
 * Created by samsiu on 4/29/16.
 */
public class MainTripRVAdapter extends RecyclerView.Adapter<MainTripRVAdapter.TripViewHolder>{

    private static final String TAG = MainTripRVAdapter.class.getSimpleName();
    private final List<TripDestination> tripList;
    private final OnMainCardViewClickListener listener;
    private static Context mContext;

    /**
     * Create listener that returns TripDestination object on click
     */
    public interface OnMainCardViewClickListener{
        void onMainCardViewClick(TripDestination tripDestination);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tripNameTextView;
        ImageView tripOriginImageView;

        TripViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.main_trip_item_cardView);
            tripNameTextView = (TextView)itemView.findViewById(R.id.main_card_tripItem_textView);
            tripOriginImageView = (ImageView)itemView.findViewById(R.id.main_card_city_imageView);
        }
    }

    public MainTripRVAdapter(List<TripDestination> tripList, OnMainCardViewClickListener listener){
        this.tripList = tripList;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_trip_item_cardview, parent, false);
        TripViewHolder tripViewHolder = new TripViewHolder(view);
        mContext = parent.getContext();
        return tripViewHolder;
    }

    /**
     * Load city images and set clickListeners to all CardViews
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(TripViewHolder holder, final int position) {
        holder.tripNameTextView.setText(tripList.get(position).getLocation());
        holder.tripOriginImageView.setVisibility(View.VISIBLE);

        Picasso.with(mContext)
                .load(tripList.get(position).getUrl())  // Load image from URL
                .placeholder(R.mipmap.ic_headingout)    // PlaceHolder Image
                .resize(500,350)                        // Resize Image
                .into(holder.tripOriginImageView);      // Load image to view

        //When cardView is clicked send use interface to send TripDestination object to MainFragment
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: CARD CLICKED NUMBER ===>> " + position);
                //Gets the TripDestination at position and sends to MainFragment
                listener.onMainCardViewClick(tripList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

}
