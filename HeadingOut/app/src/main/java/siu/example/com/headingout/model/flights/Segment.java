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
    }

    String id;
    String cabin;
    String bookingCode;
    int bookingCodeCount;
    String marriedSegmentGroup;
    List<Leg> leg;
    int connectionDuration;



}
