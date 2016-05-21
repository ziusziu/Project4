package siu.example.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.hotels.HotWireHotels;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabHotelRVAdapter extends RecyclerView.Adapter<InputTabHotelRVAdapter.HotelViewHolder>{

    public static final String PLACESPREFERENCES = "placesPreferences";
    public static final String HOTELPOSITION = "hotelPosition";

    private static SharedPreferences mSharedPref;

    public static Bundle hotelBundle;
    private static final String TAG = InputTabHotelRVAdapter.class.getSimpleName();
    private static ArrayList<Integer> hotelPositions;

    HotWireHotels hotels;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        CardView hotelCardView;
        TextView hotelCheckInDateTextView, hotelCheckOutDateTextView, hotelNightsTextView,
                hotelCurrencyCodeTextView, hotelTotalPriceTextView;
        RatingBar hotelRatingBar;

        TextView hotelNameTextView;

        HotelViewHolder(View itemView) {
            super(itemView);

            hotelPositions = new ArrayList<>();
            hotelCardView = (CardView) itemView.findViewById(R.id.input_tab_hotel_fragment_cardView);
            hotelNameTextView = (TextView) itemView.findViewById(R.id.input_tab_hotel_name_textView);
            hotelCheckInDateTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_checkInDate_textView);
            hotelCheckOutDateTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_checkOutDate_textView);
            hotelNightsTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_nights_textView);
            hotelCurrencyCodeTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_currencyCode_textView);
            hotelTotalPriceTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_totalPrice_textView);
            hotelRatingBar = (RatingBar) itemView.findViewById(R.id.input_tab_hotel_ratingBar);
        }
    }

    public InputTabHotelRVAdapter(HotWireHotels hotels){
        this.hotels = hotels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_hotel_cardview, parent, false);
        HotelViewHolder HotelViewHolder = new HotelViewHolder(view);
        mSharedPref = parent.getContext().getSharedPreferences(PLACESPREFERENCES, Context.MODE_PRIVATE);
        return HotelViewHolder;
    }

    @Override
    public void onBindViewHolder(final HotelViewHolder holder, final int position) {
        holder.hotelNameTextView.setText("HotWireRef: " + hotels.getResult().get(position).getHWRefNumber());
        holder.hotelCheckInDateTextView.setText(hotels.getResult().get(position).getCheckInDate());
        holder.hotelCheckOutDateTextView.setText(hotels.getResult().get(position).getCheckOutDate());
        holder.hotelNightsTextView.setText(" (" + hotels.getResult().get(position).getNights() + " Nights)");
        holder.hotelCurrencyCodeTextView.setText(hotels.getResult().get(position).getCurrencyCode());
        holder.hotelTotalPriceTextView.setText(hotels.getResult().get(position).getTotalPrice());
        Log.d(TAG, "onBindViewHolder:Star Rating " + hotels.getResult().get(position).getStarRating());
        holder.hotelRatingBar.setRating(Float.parseFloat(hotels.getResult().get(position).getStarRating()));

        holder.hotelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.hotelCardView.setCardBackgroundColor(R.color.colorPrimaryLight);

                hotelPositions.add(position);
                hotelBundle.putInt(HOTELPOSITION, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotels.getResult().size();
    }
}
