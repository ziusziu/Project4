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
        TextView segmentCabinTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_cabin_textView);
        TextView segmentIdTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_id_textView);
        TextView segmentBookingCodeTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_bookingCode_textView);
        TextView segmentConnectionDurationTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_connectionDuration_textView);
        TextView segmentDurationTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_duration_textView);
        TextView segmentFlightCarrierTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_flightCarrier_textView);
        TextView segmentFlightNumberTextView = (TextView)segmentView.findViewById(R.id.input_tab_flight_listView_flightNumber_textView);


        segmentCabinTextView.setText(segmentList.get(position).getCabin());
        segmentIdTextView.setText(segmentList.get(position).getId());
        segmentBookingCodeTextView.setText(String.valueOf(segmentList.get(position).getBookingCode()));
        segmentConnectionDurationTextView.setText(String.valueOf(segmentList.get(position).getConnectionDuration()));
        segmentDurationTextView.setText(String.valueOf(segmentList.get(position).getDuration()));
        segmentFlightCarrierTextView.setText(String.valueOf(segmentList.get(position).getFlight().getCarrier()));
        segmentFlightNumberTextView.setText(String.valueOf(segmentList.get(position).getFlight().getNumber()));

        return segmentView;
    }
}
