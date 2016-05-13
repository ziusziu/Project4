package siu.example.com.headingout.inputfragment.rvadapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siu.example.com.headingout.HeadingOutApplication;
import siu.example.com.headingout.MainActivity;
import siu.example.com.headingout.R;
import siu.example.com.headingout.model.forecast.Weather;
import siu.example.com.headingout.model.forecast.WeatherInfoDaily;

/**
 * Created by samsiu on 5/9/16.
 */
public class InputTabWeatherRVAdapter extends RecyclerView.Adapter<InputTabWeatherRVAdapter.WeatherViewHolder> {

    private static final String TAG = InputTabWeatherRVAdapter.class.getSimpleName();

    Weather weather;
    Context mContext;
    Bus bus;

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView weatherNameTextView, weatherSummaryTextView, weatherSunriseTimeTextView, weatherSunsetTimeTextView,
                weatherPrecipIntensityTextView, weatherPrecipProbabilityTextView,
                weatherHumidityTextView, weatherWindSpeedTextView, weatherVisibilityTextView,
                weatherDewPointTextView, weatherTemperatureMinTextView,
                weatherTemperatureMinTimeTextView, weatherTemperatureMaxTextView,
                weatherTemperatureMaxTimeTextView, weatherTimeTextView, weatherAvgTempTextView;

        WeatherViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.input_tab_weather_fragment_cardView);
            weatherTimeTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_time_textView);
            weatherAvgTempTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_avgTemp_TextView);
            weatherNameTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_textView);
            weatherSummaryTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_summary_textView);
//            weatherSunriseTimeTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_sunriseTime_textView);
//            weatherSunsetTimeTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_sunsetTime_textView);
//            weatherPrecipIntensityTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_precipIntensity_textView);
//            weatherPrecipProbabilityTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_precipProbability_textView);
//            weatherHumidityTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_humidity_textView);
//            weatherWindSpeedTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_windSpeed_textView);
//            weatherVisibilityTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_visibility_textView);
//            weatherDewPointTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_dewPoint_textView);
//            weatherTemperatureMinTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_temperatureMin_TextView);
//            weatherTemperatureMinTimeTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_temperatureMinTime_textView);
//            weatherTemperatureMaxTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_temperatureMax_textView);
//            weatherTemperatureMaxTimeTextView = (TextView) itemView.findViewById(R.id.input_tab_weather_temperatureMaxTime_textView);
        }
    }

    public InputTabWeatherRVAdapter(Weather weather, Context context){
        this.weather = weather;
        this.mContext = context;
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
        Log.d(TAG, "onBindViewHolder: inside RV Adapter " + weather.getDaily().getData().size());
        Integer size = weather.getDaily().getData().size();

        int time = weather.getDaily().getData().get(position).getTime();
        double weatherMax = weather.getDaily().getData().get(position).getTemperatureMax();
        double weatherMin = weather.getDaily().getData().get(position).getTemperatureMin();
        double weatherAvg = (weatherMax + weatherMin) / 2;
        String formattedTime = new SimpleDateFormat("MM/dd/yyy").format(new Date(time * 1000L));

        holder.weatherTimeTextView.setText(formattedTime);
        holder.weatherAvgTempTextView.setText(String.valueOf(weatherAvg));
        holder.weatherSummaryTextView.setText(weather.getDaily().getData().get(position).getSummary());


//        holder.weatherNameTextView.setText(String.valueOf(weather.getDaily().getData().size()));
//        holder.weatherSunriseTimeTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getSunsetTime()));
//        holder.weatherSunsetTimeTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getSunsetTime()));
//        holder.weatherPrecipIntensityTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getPrecipIntensity()));
//        holder.weatherPrecipProbabilityTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getPrecipProbability()));
//        holder.weatherHumidityTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getHumidity()));
//        holder.weatherWindSpeedTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getWindSpeed()));
//        holder.weatherVisibilityTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getVisibility()));
//        holder.weatherDewPointTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getDewPoint()));
//        holder.weatherTemperatureMinTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getTemperatureMin()));
//        holder.weatherTemperatureMinTimeTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getTemperatureMinTime()));
//        holder.weatherTemperatureMaxTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getTemperatureMax()));
//        holder.weatherTemperatureMaxTimeTextView.setText(String.valueOf(weather.getDaily().getData().get(position).getTemperatureMaxTime()));
        bus = createBus();
        bus.post(size);
    }

    @Override
    public int getItemCount() {
        return weather.getDaily().getData().size();
    }

    private Bus createBus(){
        // Register for bus events
        HeadingOutApplication headingOutApplication = (HeadingOutApplication)((MainActivity)mContext).getApplication();
        Bus bus = headingOutApplication.provideBus();
        return bus;
    }


}
