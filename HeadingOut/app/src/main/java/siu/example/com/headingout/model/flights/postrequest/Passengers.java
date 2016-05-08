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

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getInfantInLapCount() {
        return infantInLapCount;
    }

    public void setInfantInLapCount(int infantInLapCount) {
        this.infantInLapCount = infantInLapCount;
    }

    public int getInfantInSeatCount() {
        return infantInSeatCount;
    }

    public void setInfantInSeatCount(int infantInSeatCount) {
        this.infantInSeatCount = infantInSeatCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getSeniorCount() {
        return seniorCount;
    }

    public void setSeniorCount(int seniorCount) {
        this.seniorCount = seniorCount;
    }
}
