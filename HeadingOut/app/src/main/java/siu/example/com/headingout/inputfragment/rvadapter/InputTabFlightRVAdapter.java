package siu.example.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import siu.example.com.headingout.MainActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.inputfragment.InputFragment;
import siu.example.com.headingout.inputfragment.cardviewadapter.FlightSegmentAdapter;
import siu.example.com.headingout.model.FlightTest;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.Leg;
import siu.example.com.headingout.model.flights.Segment;
import siu.example.com.headingout.model.flights.Slice;
import siu.example.com.headingout.model.flights.Trip;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabFlightRVAdapter extends RecyclerView.Adapter<InputTabFlightRVAdapter.FlightViewHolder>{

    private static final String TAG = InputTabFlightRVAdapter.class.getSimpleName();
    //List<FlightTest> flightList;
    Flights flights;

    public static ListView flightSegmentListView;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView flightOriginTextView, flightDestinationTextView,
                flightSaleTotalTextView, flightDurationTextView;

        //TextView flightNameTextView, flightIdTextView;



        FlightViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_flight_fragment_cardView);
            flightOriginTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_origin_textView);
            flightDestinationTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_destination_textView);
            flightSaleTotalTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_saleTotal_textView);
            flightDurationTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_duration_textView);
            flightSegmentListView = (ListView) itemView.findViewById(R.id.input_tab_flight_segments_listView);

//            flightIdTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_tripId_textView);
//            flightNameTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_textView);
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
//        holder.flightNameTextView.setText(String.valueOf(flights.getTrips().getTripOption().get(0).getPricing().size()));
//        holder.flightIdTextView.setText(flights.getTrips().getTripOption().get(position).getId());

        holder.flightOriginTextView.setText("ORIGIN ____ NEED TO GRAB FROM MAIN");
        holder.flightDestinationTextView.setText("DESTINGATION ___ NEED TO GRAB FROM MAIN");
        holder.flightSaleTotalTextView.setText(flights.getTrips().getTripOption().get(position).getSaleTotal());

        int duration = flights.getTrips().getTripOption().get(position).getSlice().get(0).getDuration();
        Long longVal = new Long(duration);
        int hours = (int) longVal.longValue() / 60;
        int mins = (int) longVal.longValue() - (hours * 60);
        String durationString = hours + " hours " + mins + " mins ";
        Log.d(TAG, "onCreateView: hours " + durationString);

        holder.flightDurationTextView.setText(durationString);
        List<Segment> listSegment = flights.getTrips().getTripOption().get(position).getSlice().get(0).getSegment();
        List<Leg> listLeg;

        //TODO Don't need list view, populate programmatically, INSTEAD OF USING ADAPTERS!!!!
        FlightSegmentAdapter flightSegmentAdapter = new FlightSegmentAdapter(flightSegmentListView.getContext(), listSegment);
        flightSegmentListView.setAdapter(flightSegmentAdapter);

//
//        for(Segment segment: listSegment){
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getCabin());
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getId());
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getBookingCode());
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getConnectionDuration());
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getDuration());
//            Log.d(TAG, "onBindViewHolder: SEGMENT INFO -=====>>>>" + segment.getFlight());
//            listLeg = segment.getLeg();
//            for(Leg leg: listLeg){
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getDuration());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getId());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getAircraft());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getArrivalTime());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getDepartureTime());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getDestination());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getDestinationTerminal());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getOrigin());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getOriginTerminal());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getMeal());
//                Log.d(TAG, "onBindViewHolder:=========>>>> LEG INFO -=====>>>>" + leg.getMileage());
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return flights.getTrips().getTripOption().size();
    }
}
