package app.ga.com.headingout.model.flights;

/**
 * Created by samsiu on 5/7/16.
 */
public class SegmentPricing {
    String kind;
    String fareId;
    String segmentId;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFareId() {
        return fareId;
    }

    public void setFareId(String fareId) {
        this.fareId = fareId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }
}
