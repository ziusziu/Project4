package siu.example.com.headingout.model.flights;

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

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
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

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public int getBookingCodeCount() {
        return bookingCodeCount;
    }

    public void setBookingCodeCount(int bookingCodeCount) {
        this.bookingCodeCount = bookingCodeCount;
    }

    public String getMarriedSegmentGroup() {
        return marriedSegmentGroup;
    }

    public void setMarriedSegmentGroup(String marriedSegmentGroup) {
        this.marriedSegmentGroup = marriedSegmentGroup;
    }

    public List<Leg> getLeg() {
        return leg;
    }

    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    public int getConnectionDuration() {
        return connectionDuration;
    }

    public void setConnectionDuration(int connectionDuration) {
        this.connectionDuration = connectionDuration;
    }
}
