package siu.example.com.headingout.model.flights;

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

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Segment> getSegment() {
        return segment;
    }

    public void setSegment(List<Segment> segment) {
        this.segment = segment;
    }
}
