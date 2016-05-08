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

    public ArrayList<PostSlice> getSlice() {
        return slice;
    }

    public void setSlice(ArrayList<PostSlice> slice) {
        this.slice = slice;
    }

    public Passengers getPassenters() {
        return passengers;
    }

    public void setPassenters(Passengers passenters) {
        this.passengers = passenters;
    }

    public int getSolutions() {
        return solutions;
    }

    public void setSolutions(int solutions) {
        this.solutions = solutions;
    }

    public Boolean getRefundable() {
        return refundable;
    }

    public void setRefundable(Boolean refundable) {
        this.refundable = refundable;
    }
}
