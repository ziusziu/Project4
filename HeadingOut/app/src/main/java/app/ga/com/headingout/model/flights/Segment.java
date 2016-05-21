package app.ga.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Segment {
    String kind;
    int duration;
    Flight flight;

    public class Flight{
        String carrier;
        String number;

        public String getCarrier() {
            return carrier;
        }

        public String getNumber() {
            return number;
        }
    }

    String id;
    String cabin;
    String bookingCode;
    int bookingCodeCount;
    String marriedSegmentGroup;
    List<Leg> leg;
    int connectionDuration;

    public String getKind() {
        return kind;
    }

    public int getDuration() {
        return duration;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getId() {
        return id;
    }

    public String getCabin() {
        return cabin;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public int getBookingCodeCount() {
        return bookingCodeCount;
    }

    public String getMarriedSegmentGroup() {
        return marriedSegmentGroup;
    }

    public List<Leg> getLeg() {
        return leg;
    }

    public int getConnectionDuration() {
        return connectionDuration;
    }
}
