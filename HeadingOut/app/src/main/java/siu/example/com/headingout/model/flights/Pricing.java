package siu.example.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Pricing {
    String kind;
    List<Fare> fare;
    List<SegmentPricing> segmentPricing;
    String baseFareTotal;
    String saleFareTotal;
    String saleTaxTotal;
    String saleTotal;
    Passengers passenters;
    public class Passengers{
        String kind;
        int adultCount;
    }
    List<Tax> tax;
    String fareCalculation;
    String latestTicketingTime;
    String ptc;
}
