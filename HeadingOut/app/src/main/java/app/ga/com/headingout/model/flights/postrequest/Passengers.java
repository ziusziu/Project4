package app.ga.com.headingout.model.flights.postrequest;

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


    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public void setInfantInLapCount(int infantInLapCount) {
        this.infantInLapCount = infantInLapCount;
    }

    public void setInfantInSeatCount(int infantInSeatCount) {
        this.infantInSeatCount = infantInSeatCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public void setSeniorCount(int seniorCount) {
        this.seniorCount = seniorCount;
    }
}
