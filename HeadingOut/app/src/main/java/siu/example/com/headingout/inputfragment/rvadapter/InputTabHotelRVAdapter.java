package siu.example.com.headingout.inputfragment.rvadapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.TestHotels;
import siu.example.com.headingout.model.hotels.HWNeighborhoods;
import siu.example.com.headingout.model.hotels.HotWireHotels;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabHotelRVAdapter extends RecyclerView.Adapter<InputTabHotelRVAdapter.HotelViewHolder>{

    private static final String TAG = InputTabHotelRVAdapter.class.getSimpleName();

    List<TestHotels> hotelList;
    HotWireHotels hotels;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView hotelCheckInDateTextView, hotelCheckOutDateTextView, hotelNightsTextView,
                hotelCurrencyCodeTextView, hotelTotalPriceTextView;
        RatingBar hotelRatingBar;

        TextView hotelNameTextView;
        TextView hotelSubTotalTextView, hotelTaxFeesTextView,  hotelRoomsTextView,
                hotelStarRatingTextView, hotelDeepLinkTextView;


        HotelViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_hotel_fragment_cardView);
            hotelNameTextView = (TextView) itemView.findViewById(R.id.input_tab_hotel_name_textView);
            hotelCheckInDateTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_checkInDate_textView);
            hotelCheckOutDateTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_checkOutDate_textView);
            hotelNightsTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_nights_textView);
            hotelCurrencyCodeTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_currencyCode_textView);
            hotelTotalPriceTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_totalPrice_textView);
            hotelRatingBar = (RatingBar) itemView.findViewById(R.id.input_tab_hotel_ratingBar);


//            hotelSubTotalTextView = (TextView)itemView.findViewById(R.id.detail_tab_hotel_subTotal_textView);
//            hotelTaxFeesTextView = (TextView)itemView.findViewById(R.id.detail_tab_hotel_taxesFees_textView);
//            hotelAvgPricePerNightTextView = (TextView)itemView.findViewById(R.id.detail_tab_avgPriceNight_textView);
//            hotelRoomsTextView = (TextView)itemView.findViewById(R.id.detail_tab_hotel_rooms_textView);
//            hotelStarRatingTextView = (TextView)itemView.findViewById(R.id.input_tab_hotel_starRating_textView);
//            hotelDeepLinkTextView = (TextView)itemView.findViewById(R.id.deatil_tab_hotel_deepLink_textView);


        }
    }

//    public InputTabHotelRVAdapter(List<TestHotels> hotelList){
//        this.hotelList = hotelList;
//    }

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
        return HotelViewHolder;
    }

    @Override
    public void onBindViewHolder(final HotelViewHolder holder, int position) {
        //holder.hotelNameTextView.setText(hotelList.get(position).getName());

        List<HWNeighborhoods> neighborhoods = hotels.getMetaData().getHotelMetaData().getNeighborhoods();

        holder.hotelNameTextView.setText("HotWireRef: " + hotels.getResult().get(position).getHWRefNumber());
        holder.hotelCheckInDateTextView.setText(hotels.getResult().get(position).getCheckInDate());
        holder.hotelCheckOutDateTextView.setText(hotels.getResult().get(position).getCheckOutDate());
        holder.hotelNightsTextView.setText(" (" + hotels.getResult().get(position).getNights() + " Nights)");
        holder.hotelCurrencyCodeTextView.setText(hotels.getResult().get(position).getCurrencyCode());
        holder.hotelTotalPriceTextView.setText(hotels.getResult().get(position).getTotalPrice());
        Log.d(TAG, "onBindViewHolder:Star Rating " + hotels.getResult().get(position).getStarRating());
        holder.hotelRatingBar.setRating(Float.parseFloat(hotels.getResult().get(position).getStarRating()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.cardView.setCardBackgroundColor(R.color.colorPrimayLight);

            }
        });


//        holder.hotelSubTotalTextView.setText(hotels.getResult().get(position).getSubTotal());
//        holder.hotelTaxFeesTextView.setText(hotels.getResult().get(position).getTaxesAndFees());
//        holder.hotelAvgPricePerNightTextView.setText(hotels.getResult().get(position).getAveragePricePerNight());
//        holder.hotelRoomsTextView.setText(hotels.getResult().get(position).getRooms());
//        holder.hotelStarRatingTextView.setText(hotels.getResult().get(position).getStarRating());
//        holder.hotelDeepLinkTextView.setText(hotels.getResult().get(position).getDeepLink());

    }

    @Override
    public int getItemCount() {
        //return hotelList.size();
        return hotels.getResult().size();
    }
}
