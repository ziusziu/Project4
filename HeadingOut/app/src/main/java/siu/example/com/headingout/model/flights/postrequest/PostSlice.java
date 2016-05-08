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

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
