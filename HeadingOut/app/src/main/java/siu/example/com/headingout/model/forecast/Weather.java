package siu.example.com.headingout.model.forecast;

import java.util.List;

/**
 * Created by samsiu on 5/6/16.
 */
public class Weather {
    double latitude;
    double longitude;
    String timezone;
    int offset;
    WeatherInfo currently;
    WeatherMinute minutely;
    WeatherDetail hourly;
    WeatherDetail daily;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public WeatherInfo getCurrently() {
        return currently;
    }

    public void setCurrently(WeatherInfo currently) {
        this.currently = currently;
    }

    public WeatherMinute getMinutely() {
        return minutely;
    }

    public void setMinutely(WeatherMinute minutely) {
        this.minutely = minutely;
    }

    public WeatherDetail getHourly() {
        return hourly;
    }

    public void setHourly(WeatherDetail hourly) {
        this.hourly = hourly;
    }

    public WeatherDetail getDaily() {
        return daily;
    }

    public void setDaily(WeatherDetail daily) {
        this.daily = daily;
    }
}
