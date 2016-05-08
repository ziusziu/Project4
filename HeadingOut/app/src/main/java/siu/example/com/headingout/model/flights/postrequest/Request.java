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


    public void setSlice(ArrayList<PostSlice> slice) {
        this.slice = slice;
    }

    public void setPassengers(Passengers passengers) {
        this.passengers = passengers;
    }

    public void setSolutions(int solutions) {
        this.solutions = solutions;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }
}
