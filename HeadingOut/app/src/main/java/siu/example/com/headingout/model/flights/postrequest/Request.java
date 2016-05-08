package siu.example.com.headingout.model.flights.postrequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Request {

    ArrayList<PostSlice> slice;
    Passengers passengers;
    int solutions;
    Boolean refundable;

    public Request(ArrayList<PostSlice> slice, Passengers passengers, int solutions, Boolean refundable) {
        this.slice = slice;
        this.passengers = passengers;
        this.solutions = solutions;
        this.refundable = refundable;
    }


}
