package siu.example.com.headingout.model.flights;

/**
 * Created by samsiu on 5/7/16.
 */
public class Leg {

    String kind;
    String id;
    String aircraft;
    String arrivalTime;
    String departureTime;
    String origin;
    String destination;
    String originTerminal;
    String destinationTerminal;
    int duration;
    int mileage;
    String meal;
    Boolean secure;

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public String getAircraft() {
        return aircraft;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getOriginTerminal() {
        return originTerminal;
    }

    public String getDestinationTerminal() {
        return destinationTerminal;
    }

    public int getDuration() {
        return duration;
    }

    public int getMileage() {
        return mileage;
    }

    public String getMeal() {
        return meal;
    }

    public Boolean getSecure() {
        return secure;
    }
}
