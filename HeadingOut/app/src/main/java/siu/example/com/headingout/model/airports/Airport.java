package siu.example.com.headingout.model.airports;

/**
 * Created by samsiu on 5/6/16.
 */
public class Airport {

    String fs;
    String iata;
    String faa;
    String name;
    String city;
    String cityCode;
    String stateCode;
    String countryCode;
    String countryName;
    String regionName;
    String timeZoneRegionName;
    String localTime;
    int utcOffsetHours;
    double latitude;
    double longitude;
    int elevationFeet;
    int classification;
    Boolean active;
    String weatherUrl;
    String delayIndexUrl;

    public String getFs() {
        return fs;
    }

    public String getIata() {
        return iata;
    }

    public String getFaa() {
        return faa;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getTimeZoneRegionName() {
        return timeZoneRegionName;
    }

    public String getLocalTime() {
        return localTime;
    }

    public int getUtcOffsetHours() {
        return utcOffsetHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getElevationFeet() {
        return elevationFeet;
    }

    public int getClassification() {
        return classification;
    }

    public Boolean getActive() {
        return active;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public String getDelayIndexUrl() {
        return delayIndexUrl;
    }
}
