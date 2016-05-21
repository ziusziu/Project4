package app.ga.com.headingout.detailfragment.rvadapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.model.TestHotels;

/**
 * Created by samsiu on 5/2/16.
 */
public class DetailTabHotelRVAdapter extends RecyclerView.Adapter<DetailTabHotelRVAdapter.HotelViewHolder>{

    List<TestHotels> hotelList;

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView hotelNameTextView;



        Button shareButton;

        HotelViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.detail_tab_hotel_fragment_cardView);
            hotelNameTextView = (TextView) itemView.findViewById(R.id.detail_tab_hotel_name_textView);

            shareButton = (Button)itemView.findViewById(R.id.detail_tab_hotel_share_button);
        }
    }

    public DetailTabHotelRVAdapter(List<TestHotels> hotelList){
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

        setShareButtonListener(holder);
    }

    private void setShareButtonListener(HotelViewHolder holder){
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "Description";
                String title = "Title";
                String location = "Location";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, description);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title + ": " + location);

                v.getContext().startActivity(Intent.createChooser(intent, "Share to"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }
}
