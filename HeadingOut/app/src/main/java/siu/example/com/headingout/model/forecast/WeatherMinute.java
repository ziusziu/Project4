package siu.example.com.headingout.model.forecast;

import java.util.List;

/**
 * Created by samsiu on 5/6/16.
 */
public class WeatherMinute {
    String summary;
    String icon;
    List<WeatherMinuteData> data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<WeatherMinuteData> getData() {
        return data;
    }

    public void setData(List<WeatherMinuteData> data) {
        this.data = data;
    }
}
