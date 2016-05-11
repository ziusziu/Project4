package siu.example.com.headingout.inputfragment.rvadapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.FlightTest;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.Trip;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabFlightRVAdapter extends RecyclerView.Adapter<InputTabFlightRVAdapter.FlightViewHolder>{

    //List<FlightTest> flightList;
    Flights flights;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView flightNameTextView;

        FlightViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_flight_fragment_cardView);
            flightNameTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_textView);
        }
    }

    public InputTabFlightRVAdapter(Flights flights){
        this.flights = flights;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_flight_cardview, parent, false);
        FlightViewHolder flightViewHolder = new FlightViewHolder(view);
        return flightViewHolder;
    }

    @Override
    public void onBindViewHolder(FlightViewHolder holder, int position) {
        holder.flightNameTextView.setText(String.valueOf(flights.getTrips().getTripOption().get(0).getPricing().size()));
    }

    @Override
    public int getItemCount() {
        return flights.getTrips().getTripOption().size();
    }
}
