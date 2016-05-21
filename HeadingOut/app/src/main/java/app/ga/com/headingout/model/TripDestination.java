package app.ga.com.headingout.model;

/**
 * Created by samsiu on 4/29/16.
 */
public class TripDestination {
    String location;
    String latitude;
    String longitude;
    String airportCode;
    String url;


    public TripDestination(String location, String airportCode, String latitude, String longitude, String url) {
        this.location = location;
        this.airportCode = airportCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getUrl() {
        return url;
    }
}
