package siu.example.com.headingout;

/**
 * Created by samsiu on 5/2/16.
 */
public class Flight {

    String name;
    String Depart;

    public Flight(String name, String depart) {
        this.name = name;
        Depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepart() {
        return Depart;
    }

    public void setDepart(String depart) {
        Depart = depart;
    }
}
