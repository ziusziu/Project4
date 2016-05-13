package siu.example.com.headingout.model;

/**
 * Created by samsiu on 4/29/16.
 */
public class TestTrip {
    String location;
    String latitude;
    String longitude;
    String airportCode;


    public TestTrip(String location, String airportCode, String latitude, String longitude) {
        this.location = location;
        this.airportCode = airportCode;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
