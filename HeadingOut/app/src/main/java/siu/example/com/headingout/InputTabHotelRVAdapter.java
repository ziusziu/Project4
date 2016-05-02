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
public class InputTabHotelRVAdapter extends RecyclerView.Adapter<InputTabHotelRVAdapter.HotelViewHolder>{

    List<Hotel> hotelList;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView hotelNameTextView;

        HotelViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_hotel_fragment_cardView);
            hotelNameTextView = (TextView) itemView.findViewById(R.id.input_tab_hotel_textView);
        }
    }

    public InputTabHotelRVAdapter(List<Hotel> hotelList){
        this.hotelList = hotelList;
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
    public void onBindViewHolder(HotelViewHolder holder, int position) {
        holder.hotelNameTextView.setText(hotelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }
}
