package siu.example.com.headingout.model.flights.postrequest;

/**
 * Created by samsiu on 5/7/16.
 */
public class Passengers {

    int adultCount;
    int infantInLapCount;
    int infantInSeatCount;
    int childCount;
    int seniorCount;

    public Passengers(int adultCount, int infantInLapCount, int infantInSeatCount, int childCount, int seniorCount) {
        this.adultCount = adultCount;
        this.infantInLapCount = infantInLapCount;
        this.infantInSeatCount = infantInSeatCount;
        this.childCount = childCount;
        this.seniorCount = seniorCount;
    }


    public int getAdultCount() {
        return adultCount;
    }

    public int getInfantInLapCount() {
        return infantInLapCount;
    }

    public int getInfantInSeatCount() {
        return infantInSeatCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public int getSeniorCount() {
        return seniorCount;
    }
}
