package app.ga.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import app.ga.com.headingout.R;
import app.ga.com.headingout.model.flights.Flights;
import app.ga.com.headingout.model.flights.Leg;
import app.ga.com.headingout.model.flights.Segment;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by samsiu on 5/2/16.
 */
public class InputTabFlightRVAdapter extends RecyclerView.Adapter<InputTabFlightRVAdapter.FlightViewHolder>{

    public static final String FLIGHTPOSITION = "flightPosition";

    private static SharedPreferences sharedPref;

    private Flights flights;

    public static class FlightViewHolder extends RecyclerView.ViewHolder {

        @Nullable @BindView(R.id.input_tab_flight_fragment_cardView) CardView flightCardView;
        @BindView(R.id.input_tab_flight_saleTotal_textView) TextView flightSaleTotalTextView;
        @BindView(R.id.input_tab_flight_duration_textView) TextView flightDurationTextView;
        @BindView(R.id.input_tab_flight_cards_linearLayout) LinearLayout flightCardsLinearLayout;

        FlightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public InputTabFlightRVAdapter(Flights flights){
        this.flights = flights;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Timber.d("Flight RV Adapter: onAttachedToRecyclerView");
    }

    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("Flight RV Adapter: onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_flight_cardview, parent, false);
        FlightViewHolder flightViewHolder = new FlightViewHolder(view);
        sharedPref = parent.getContext().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);
        return flightViewHolder;
    }


    @Override
    public void onBindViewHolder(FlightViewHolder holder, final int position) {
        Timber.d("Flight RV Adapter: onBindViewHolder");
        holder.flightSaleTotalTextView.setText(flights.getTrips().getTripOption().get(position).getSaleTotal());

        int duration = flights.getTrips().getTripOption().get(position).getSlice().get(0).getDuration();
        String durationString = Utilities.convertMinToHours(duration);
        Timber.d("onCreateView: hours " + durationString);

        holder.flightDurationTextView.setText(durationString);
        List<Segment> listSegment = flights.getTrips().getTripOption().get(position).getSlice().get(0).getSegment();

        holder.flightCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(FLIGHTPOSITION, position);
                editor.apply();
            }
        });

        appendFlightData(holder, listSegment);
    }

    /**
     * Programmatically add Flight Data to cardviews with TextViews
     * @param holder
     * @param listSegment
     */
    private void appendFlightData(FlightViewHolder holder, List<Segment> listSegment){
        List<Leg> listLeg;

        Context context = holder.flightCardsLinearLayout.getContext();
        holder.flightCardsLinearLayout.removeAllViews();

        for(Segment segment: listSegment){

            setCardViewSegment(context, holder, segment);

            listLeg = segment.getLeg();

            for(Leg leg: listLeg){
                setCardViewLegDeparting(context, holder, leg);
                setCardViewLegArriving(context, holder, leg);
            }

            if(segment.getConnectionDuration() != 0 ){
                setCardViewSegmentDuration(context, holder, segment);
            }
        }
    }

    private void setCardViewSegment(Context context, FlightViewHolder holder, Segment segment){
        LinearLayout segmentLinearLayout = new LinearLayout(context);
        segmentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        String carrierText = segment.getFlight().getCarrier();
        Utilities.addNewTextViewToLayout(context, segmentLinearLayout, carrierText, 18, Typeface.DEFAULT_BOLD);

        String flightNumber = segment.getFlight().getNumber();
        Utilities.addNewTextViewToLayout(context, segmentLinearLayout, flightNumber, 18);

        String cabin = " (" + segment.getCabin() + ")";
        Utilities.addNewTextViewToLayout(context, segmentLinearLayout, cabin, 18);

        String segmentDuration = " " + Utilities.convertMinToHours(segment.getDuration());
        Utilities.addNewTextViewToLayout(context, segmentLinearLayout, segmentDuration);

        holder.flightCardsLinearLayout.addView(segmentLinearLayout);
    }

    private void setCardViewLegDeparting(Context context, FlightViewHolder holder, Leg leg){
        LinearLayout legDepartingLinearLayout = new LinearLayout(context);
        legDepartingLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Utilities.addNewTextViewToLayout(context, legDepartingLinearLayout, "Departing: ", 15, Typeface.DEFAULT_BOLD);

        String legDepartureTime = leg.getDepartureTime().substring(0, leg.getDepartureTime().length()-6);
        Utilities.addNewTextViewToLayout(context, legDepartingLinearLayout, legDepartureTime);

        holder.flightCardsLinearLayout.addView(legDepartingLinearLayout);
    }


    private void setCardViewLegArriving(Context context, FlightViewHolder holder, Leg leg){
        LinearLayout legArrivingLinearLayout = new LinearLayout(context);
        legArrivingLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Utilities.addNewTextViewToLayout(context, legArrivingLinearLayout, "Arriving: ", 15, Typeface.DEFAULT_BOLD);

        String legArrivalTime = leg.getArrivalTime().substring(0, leg.getArrivalTime().length()-6);
        Utilities.addNewTextViewToLayout(context, legArrivingLinearLayout, legArrivalTime);

        holder.flightCardsLinearLayout.addView(legArrivingLinearLayout);
    }

    private void setCardViewSegmentDuration(Context context, FlightViewHolder holder, Segment segment){
        TextView segmentConnectionTimeTextView = new TextView(context);

        String connectionDurationString = Utilities.convertMinToHours(segment.getConnectionDuration());

        segmentConnectionTimeTextView.setText("Connection Time: " + connectionDurationString);

        holder.flightCardsLinearLayout.addView(segmentConnectionTimeTextView);
    }

    @Override
    public int getItemCount() {
        return flights.getTrips().getTripOption().size();
    }


    @Override
    public void onViewAttachedToWindow(FlightViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Timber.d("Flight RV Adapter: onViewAttachedToWinder");

    }

    @Override
    public void onViewDetachedFromWindow(FlightViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Timber.d("Flight RV Adapter: onViewDetachedFromWindow");
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Timber.d("Flight RV Adapter: onDetachedFromRecyclerView");
    }


}
