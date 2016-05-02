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
public class InputTabFlightRVAdapter extends RecyclerView.Adapter<InputTabFlightRVAdapter.FlightViewHolder>{

    List<Flight> flightList;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView flightNameTextView;

        FlightViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_flight_fragment_cardView);
            flightNameTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_textView);
        }
    }

    public InputTabFlightRVAdapter(List<Flight> flightList){
        this.flightList = flightList;
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
        holder.flightNameTextView.setText(flightList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }
}
