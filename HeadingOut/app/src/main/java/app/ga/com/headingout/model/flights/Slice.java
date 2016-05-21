package app.ga.com.headingout.model.flights;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */
public class Slice {

    String kind;
    int duration;
    List<Segment> segment;

    public String getKind() {
        return kind;
    }

    public int getDuration() {
        return duration;
    }

    public List<Segment> getSegment() {
        return segment;
    }
}
