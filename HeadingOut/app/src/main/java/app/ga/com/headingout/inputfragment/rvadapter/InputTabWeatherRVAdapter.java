package app.ga.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import app.ga.com.headingout.HeadingOutApplication;
import app.ga.com.headingout.MainActivity;
import app.ga.com.headingout.R;
import app.ga.com.headingout.inputfragment.InputFragment;
import app.ga.com.headingout.model.forecast.Weather;
import app.ga.com.headingout.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputTabWeatherRVAdapter extends RecyclerView.Adapter<InputTabWeatherRVAdapter.WeatherViewHolder> {

    public static final String WEATHERPOSITION = "weatherPosition";

    private static SharedPreferences sharedPref;

    private Weather weather;

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.input_tab_flight_fragment_cardView) CardView weatherCardView;
        @BindView(R.id.input_tab_weather_time_textView) TextView weatherTimeTextView;
        @BindView(R.id.input_tab_weather_avgTemp_TextView) TextView weatherAvgTempTextView;
        @BindView(R.id.input_tab_weather_textView) TextView weatherNameTextView;
        @BindView(R.id.input_tab_weather_summary_textView) TextView weatherSummaryTextView;

        WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public InputTabWeatherRVAdapter(Weather weather){
        this.weather = weather;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_tab_weather_cardview, parent, false);

        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);

        sharedPref = parent.getContext().getSharedPreferences(Utilities.PLACESPREFERENCES, Context.MODE_PRIVATE);

        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, final int position) {
        Timber.d("onBindViewHolder: inside RV Adapter " + weather.getDaily().getData().size());
        Integer size = weather.getDaily().getData().size();

        int time = weather.getDaily().getData().get(position).getTime();
        double weatherMax = weather.getDaily().getData().get(position).getTemperatureMax();
        double weatherMin = weather.getDaily().getData().get(position).getTemperatureMin();
        double weatherAvg = (weatherMax + weatherMin) / 2;

        String formattedTime = new SimpleDateFormat("MM/dd/yyyy").format(new Date(time * 1000L));

        holder.weatherTimeTextView.setText(formattedTime);
        holder.weatherAvgTempTextView.setText(String.format("%.0f" + (char) 0x00B0, weatherAvg));
        holder.weatherSummaryTextView.setText(weather.getDaily().getData().get(position).getSummary());

//        holder.weatherCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putInt(WEATHERPOSITION, position);
//                editor.apply();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return weather.getDaily().getData().size();
    }


}
