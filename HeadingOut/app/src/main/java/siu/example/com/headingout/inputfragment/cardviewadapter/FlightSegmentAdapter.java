package siu.example.com.headingout.inputfragment.cardviewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.flights.Segment;

/**
 * Created by samsiu on 5/12/16.
 */
public class FlightSegmentAdapter extends ArrayAdapter<Segment> {
    List<Segment> segmentList;

    public FlightSegmentAdapter(Context context, List<Segment> objects) {
        super(context, -1, objects);
        this.segmentList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View segmentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_flight_cardview_listview, parent, false);
        TextView segmentFlightCarrierTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_flightCarrier_textView);
        TextView segmentFlightNumberTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_flightNumber_textView);
        TextView segmentCabinTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_cabin_textView);
        TextView legOriginTerminal = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_originTerminal_textView);
        TextView legDepartureTime = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_departureTime_textView);
        TextView legDestinationTerminal = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_destinationTerminal_textView);
        TextView legArrivalTime = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_arrivalTime_textView);

//        TextView segmentIdTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_id_textView);
//        TextView segmentBookingCodeTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_bookingCode_textView);
//        TextView segmentConnectionDurationTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_connectionDuration_textView);
//        TextView segmentDurationTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_duration_textView);
        segmentFlightCarrierTextView.setText(String.valueOf(segmentList.get(position).getFlight().getCarrier()));
        segmentFlightNumberTextView.setText(String.valueOf(segmentList.get(position).getFlight().getNumber()));
        segmentCabinTextView.setText(" ("+segmentList.get(position).getCabin()+")");
        legOriginTerminal.setText(segmentList.get(position).getLeg().get(0).getOriginTerminal());
        legDepartureTime.setText(segmentList.get(position).getLeg().get(0).getDepartureTime());
        legDestinationTerminal.setText(segmentList.get(position).getLeg().get(0).getDestinationTerminal());
        legArrivalTime.setText(segmentList.get(position).getLeg().get(0).getArrivalTime());

//        segmentIdTextView.setText(segmentList.get(position).getId());
//        segmentBookingCodeTextView.setText(String.valueOf(segmentList.get(position).getBookingCode()));
//        segmentConnectionDurationTextView.setText(String.valueOf(segmentList.get(position).getConnectionDuration()));
//        segmentDurationTextView.setText(String.valueOf(segmentList.get(position).getDuration()));

        return segmentView;
    }
}
