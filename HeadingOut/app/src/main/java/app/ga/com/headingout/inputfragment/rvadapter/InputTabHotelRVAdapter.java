package app.ga.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.ga.com.headingout.R;
import app.ga.com.headingout.model.hotels.HotWireHotels;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabHotelRVAdapter extends RecyclerView.Adapter<InputTabHotelRVAdapter.HotelViewHolder>{

    public static final String HOTELPOSITION = "hotelPosition";

    private static SharedPreferences sharedPref;

    public static Bundle hotelBundle;
    private static ArrayList<Integer> hotelPositions;

    private HotWireHotels hotels;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        @Nullable @BindView(R.id.input_tab_hotel_fragment_cardView) CardView hotelCardView;
        @BindView(R.id.input_tab_hotel_name_textView) TextView hotelNameTextView;
        @BindView(R.id.input_tab_hotel_checkInDate_textView) TextView hotelCheckInDateTextView;
        @BindView(R.id.input_tab_hotel_checkOutDate_textView) TextView hotelCheckOutDateTextView;
        @BindView(R.id.input_tab_hotel_nights_textView) TextView hotelNightsTextView;
        @BindView(R.id.input_tab_hotel_currencyCode_textView) TextView hotelCurrencyCodeTextView;
        @BindView(R.id.input_tab_hotel_totalPrice_textView) TextView hotelTotalPriceTextView;
        @BindView(R.id.input_tab_hotel_ratingBar) RatingBar hotelRatingBar;

        HotelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            hotelPositions = new ArrayList<>();
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

        sharedPref = parent.getContext().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);

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
        holder.hotelRatingBar.setRating(Float.parseFloat(hotels.getResult().get(position).getStarRating()));

        Timber.d("onBindViewHolder:Star Rating " + hotels.getResult().get(position).getStarRating());

//        holder.hotelCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.hotelCardView.setCardBackgroundColor(R.color.colorPrimaryLight);
//
//                hotelPositions.add(position);
//                hotelBundle.putInt(HOTELPOSITION, position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return hotels.getResult().size();
    }
}
