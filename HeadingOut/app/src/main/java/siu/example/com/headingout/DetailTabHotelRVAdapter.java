package siu.example.com.headingout;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by samsiu on 5/2/16.
 */
public class DetailTabHotelRVAdapter extends RecyclerView.Adapter<DetailTabHotelRVAdapter.HotelViewHolder>{

    List<Hotel> hotelList;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView hotelNameTextView;
        TextView hotelCityTextView;
        TextView hotelStateTextView;
        TextView hotelCountryTextView;
        TextView hotelRatingTextView;

        HotelViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.detail_tab_hotel_fragment_cardView);
            hotelNameTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_name_textView);
            hotelCityTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_city_textView);
            hotelStateTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_state_textView);
            hotelCountryTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_country_textView);
            hotelRatingTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_rating_textView);
        }
    }

    public DetailTabHotelRVAdapter(List<Hotel> hotelList){
        this.hotelList = hotelList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_tab_hotel_cardview, parent, false);
        HotelViewHolder HotelViewHolder = new HotelViewHolder(view);
        return HotelViewHolder;
    }

    @Override
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        holder.hotelNameTextView.setText("Name: "+hotelList.get(position).getName());
        holder.hotelNameTextView.setText("City: "+hotelList.get(position).getCity());
        holder.hotelNameTextView.setText("State: "+hotelList.get(position).getState());
        holder.hotelNameTextView.setText("Country: "+hotelList.get(position).getCountry());
        holder.hotelNameTextView.setText("Rating: "+hotelList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }
}
