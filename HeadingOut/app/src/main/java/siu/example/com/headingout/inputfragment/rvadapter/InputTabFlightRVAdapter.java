package siu.example.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.flights.Flights;
import siu.example.com.headingout.model.flights.Leg;
import siu.example.com.headingout.model.flights.Segment;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabFlightRVAdapter extends RecyclerView.Adapter<InputTabFlightRVAdapter.FlightViewHolder>{

    private static final String TAG = InputTabFlightRVAdapter.class.getSimpleName();
    Flights flights;

    public static ListView flightSegmentListView;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView flightOriginTextView, flightDestinationTextView,
                flightSaleTotalTextView, flightDurationTextView;
        LinearLayout flightCardsLinearLayout;

        FlightViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_flight_fragment_cardView);
            flightOriginTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_origin_textView);
            flightDestinationTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_destination_textView);
            flightSaleTotalTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_saleTotal_textView);
            flightDurationTextView = (TextView) itemView.findViewById(R.id.input_tab_flight_duration_textView);
            flightSegmentListView = (ListView) itemView.findViewById(R.id.input_tab_flight_segments_listView);
            flightCardsLinearLayout = (LinearLayout) itemView.findViewById(R.id.input_tab_flight_cards_linearLayout);

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
        holder.flightOriginTextView.setText("ORIGIN ____ NEED TO GRAB FROM MAIN");
        holder.flightDestinationTextView.setText("DESTINGATION ___ NEED TO GRAB FROM MAIN");
        holder.flightSaleTotalTextView.setText(flights.getTrips().getTripOption().get(position).getSaleTotal());

        int duration = flights.getTrips().getTripOption().get(position).getSlice().get(0).getDuration();
        String durationString = convertMinToHours(duration);
        Log.d(TAG, "onCreateView: hours " + durationString);

        holder.flightDurationTextView.setText(durationString);
        List<Segment> listSegment = flights.getTrips().getTripOption().get(position).getSlice().get(0).getSegment();
        List<Leg> listLeg;

        Context context = holder.flightCardsLinearLayout.getContext();


        for(Segment segment: listSegment){
            LinearLayout segmentLinearLayout = new LinearLayout(context);
            segmentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView segmentFlightCarrierTextView = new TextView(context);
            segmentFlightCarrierTextView.setText(segment.getFlight().getCarrier());
            segmentFlightCarrierTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            segmentFlightCarrierTextView.setTypeface(Typeface.DEFAULT_BOLD);
            segmentLinearLayout.addView(segmentFlightCarrierTextView);

            TextView segmentFlightNumberTextView = new TextView(context);
            segmentFlightNumberTextView.setText(segment.getFlight().getNumber());
            segmentFlightNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            segmentLinearLayout.addView(segmentFlightNumberTextView);

            TextView segmentCabinTextView = new TextView(context);
            segmentCabinTextView.setText(" (" + segment.getCabin() + ")");
            segmentCabinTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            segmentLinearLayout.addView(segmentCabinTextView);

            TextView segmentDurationTextView = new TextView(context);
            String segmentDuration = convertMinToHours(segment.getDuration());
            segmentDurationTextView.setText(" " + segmentDuration);
            segmentLinearLayout.addView(segmentDurationTextView);

            holder.flightCardsLinearLayout.addView(segmentLinearLayout);

            listLeg = segment.getLeg();
            for(Leg leg: listLeg){

                LinearLayout legDepartingLinearLayout = new LinearLayout(context);
                legDepartingLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView departingTextView = new TextView(context);
                departingTextView.setText("Departing: ");
                departingTextView.setTypeface(Typeface.DEFAULT_BOLD);
                departingTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                legDepartingLinearLayout.addView(departingTextView);

                TextView legDepartureTimeTextView = new TextView(context);
                legDepartureTimeTextView.setText(leg.getDepartureTime().substring(0, leg.getDepartureTime().length()-6));
                legDepartingLinearLayout.addView(legDepartureTimeTextView);
                holder.flightCardsLinearLayout.addView(legDepartingLinearLayout);


                LinearLayout legArrivingLinearLayout = new LinearLayout(context);
                legArrivingLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView arrivingTextView = new TextView(context);
                arrivingTextView.setText("Arriving: ");
                arrivingTextView.setTypeface(Typeface.DEFAULT_BOLD);
                arrivingTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                legArrivingLinearLayout.addView(arrivingTextView);

                TextView legArrivalTimeTextView = new TextView(context);
                legArrivalTimeTextView.setText(leg.getArrivalTime().substring(0, leg.getArrivalTime().length()-6));
                legArrivingLinearLayout.addView(legArrivalTimeTextView);

                holder.flightCardsLinearLayout.addView(legArrivingLinearLayout);

            }

            if(segment.getConnectionDuration() != 0 ){
                TextView segmentConnectionTimeTextView = new TextView(context);
                String connectionDurationString = convertMinToHours(segment.getConnectionDuration());
                segmentConnectionTimeTextView.setText("Connection Time: " + connectionDurationString);
                holder.flightCardsLinearLayout.addView(segmentConnectionTimeTextView);
            }

        }

//
//        FlightSegmentAdapter flightSegmentAdapter = new FlightSegmentAdapter(flightSegmentListView.getContext(), listSegment);
//        flightSegmentListView.setAdapter(flightSegmentAdapter);


    }

    @Override
    public int getItemCount() {
        return flights.getTrips().getTripOption().size();
    }

    private String convertMinToHours(int duration){
        Long longVal = new Long(duration);
        int hours = (int) longVal.longValue() / 60;
        int mins = (int) longVal.longValue() - (hours * 60);
        String durationString = hours + " hours " + mins + " mins ";
        Log.d(TAG, "onCreateView: hours " + durationString);
        return durationString;
    }

}
