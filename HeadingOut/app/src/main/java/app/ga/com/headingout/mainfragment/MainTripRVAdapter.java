package app.ga.com.headingout.mainfragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.model.TripDestination;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by samsiu on 4/29/16.
 */
public class MainTripRVAdapter extends RecyclerView.Adapter<MainTripRVAdapter.TripViewHolder>{

    private List<TripDestination> tripList;
    private OnMainCardViewClickListener clickListener;
    private static Context context;

    /**
     * Create a listener that returns TripDestination object on cardview click
     */
    public interface OnMainCardViewClickListener{
        void onMainCardViewClick(TripDestination tripDestination);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.main_trip_item_cardView) CardView cardView;
        @BindView(R.id.main_card_tripItem_textView) TextView tripNameTextView;
        @BindView(R.id.main_card_city_imageView) ImageView tripOriginImageView;

        TripViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MainTripRVAdapter(List<TripDestination> tripList, OnMainCardViewClickListener clickListener){
        this.tripList = tripList;
        this.clickListener = clickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_trip_item_cardview, parent, false);
        TripViewHolder tripViewHolder = new TripViewHolder(view);
        context = parent.getContext();
        return tripViewHolder;
    }

    /**
     * Load city images and set clickListeners to all CardViews
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        holder.tripNameTextView.setText(tripList.get(position).getLocation());
        holder.tripOriginImageView.setVisibility(View.VISIBLE);

        int defaultImage = R.mipmap.ic_headingout;
        int imageWidth = 500;
        int imageLength = 350;
        loadPicassoImage(holder, position, imageWidth, imageLength, defaultImage);

        setCardViewClickListener(holder, position);
    }

    private void loadPicassoImage(TripViewHolder holder, int position, int width, int length, int image){
        Picasso.with(context)
                .load(tripList.get(position).getUrl())  // Load image from URL
                .placeholder(image)                     // PlaceHolder Image
                .resize(width,length)                   // Resize Image
                .into(holder.tripOriginImageView);      // Load image to view
    }

    /**
     * When cardView is clicked, use interface to send TripDestination object to MainFragment
     *
     * @param holder
     * @param position
     */
    private void setCardViewClickListener(TripViewHolder holder, final int position){
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("onClick: CARD CLICKED NUMBER ===>> " + position);
                //Gets the TripDestination at position and sends to MainFragment
                clickListener.onMainCardViewClick(tripList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
