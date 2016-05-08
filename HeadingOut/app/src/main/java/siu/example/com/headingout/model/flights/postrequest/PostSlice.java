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

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }
}
