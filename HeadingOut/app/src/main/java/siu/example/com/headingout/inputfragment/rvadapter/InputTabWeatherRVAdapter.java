package siu.example.com.headingout.inputfragment.rvadapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import siu.example.com.headingout.R;
import siu.example.com.headingout.model.FlightTest;
import siu.example.com.headingout.model.forecast.Weather;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputTabWeatherRVAdapter extends RecyclerView.Adapter<InputTabWeatherRVAdapter.WeatherViewHolder> {

    private static final String TAG = InputTabWeatherRVAdapter.class.getSimpleName();

    List<FlightTest> weatherList;

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView weatherNameTextView;

        WeatherViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_weather_fragment_cardView);
            weatherNameTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_textView);
        }
    }

    public InputTabWeatherRVAdapter(List<FlightTest> weatherList){
        this.weatherList = weatherList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_weather_cardview, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.weatherNameTextView.setText(weatherList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


}
