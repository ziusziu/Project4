package siu.example.com.headingout.model.flights.postrequest;

/**
 * Created by samsiu on 5/7/16.
 */
public class PostSlice {
    String origin;
    String destination;
    String date;

    public PostSlice(String origin, String destination, String date) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
