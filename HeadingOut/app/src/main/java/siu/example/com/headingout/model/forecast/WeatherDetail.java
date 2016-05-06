package siu.example.com.headingout.model.forecast;

import java.util.List;

/**
 * Created by samsiu on 5/6/16.
 */
public class WeatherDetail {
    String summary;
    String icon;
    List<WeatherInfo> data;

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

    public List<WeatherInfo> getData() {
        return data;
    }

    public void setData(List<WeatherInfo> data) {
        this.data = data;
    }
}
