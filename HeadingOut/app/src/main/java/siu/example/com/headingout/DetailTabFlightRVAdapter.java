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
public class DetailTabFlightRVAdapter extends RecyclerView.Adapter<DetailTabFlightRVAdapter.FlightViewHolder>{

    List<Flight> flightList;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView flightNameTextView;
        TextView flightDepartCityTextView;
        TextView flightArrivalCityTextView;

        FlightViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.detail_tab_flight_fragment_cardView);
            flightNameTextView = (TextView) itemView.findViewById(R.id.detail_tab_flight_textView);
            flightDepartCityTextView = (TextView)itemView.findViewById(R.id.detail_tab_departueCity_textView);
            flightArrivalCityTextView = (TextView)itemView.findViewById(R.id.detail_tab_arrivalCity_textView);
        }
    }

    public DetailTabFlightRVAdapter(List<Flight> flightList){
        this.flightList = flightList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_tab_flight_cardview, parent, false);
        FlightViewHolder flightViewHolder = new FlightViewHolder(view);
        return flightViewHolder;
    }

    @Override
    public void onBindViewHolder(FlightViewHolder holder, int position) {
        holder.flightNameTextView.setText("Name: "+flightList.get(position).getName());
        holder.flightDepartCityTextView.setText("Depart City: "+flightList.get(position).getDepartureCity());
        holder.flightArrivalCityTextView.setText("Arrival City: "+flightList.get(position).getArrivalCity());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }
}
