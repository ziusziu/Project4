package siu.example.com.headingout.model.flights;

/**
 * Created by samsiu on 5/7/16.
 */
public class Fare {
    String kind;
    String id;
    String carrier;
    String origin;
    String destination;
    String basisCode;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBasisCode() {
        return basisCode;
    }

    public void setBasisCode(String basisCode) {
        this.basisCode = basisCode;
    }
}
