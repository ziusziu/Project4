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

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getFaa() {
        return faa;
    }

    public void setFaa(String faa) {
        this.faa = faa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getTimeZoneRegionName() {
        return timeZoneRegionName;
    }

    public void setTimeZoneRegionName(String timeZoneRegionName) {
        this.timeZoneRegionName = timeZoneRegionName;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public int getUtcOffsetHours() {
        return utcOffsetHours;
    }

    public void setUtcOffsetHours(int utcOffsetHours) {
        this.utcOffsetHours = utcOffsetHours;
    }

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

    public int getElevationFeet() {
        return elevationFeet;
    }

    public void setElevationFeet(int elevationFeet) {
        this.elevationFeet = elevationFeet;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getDelayIndexUrl() {
        return delayIndexUrl;
    }

    public void setDelayIndexUrl(String delayIndexUrl) {
        this.delayIndexUrl = delayIndexUrl;
    }
}
