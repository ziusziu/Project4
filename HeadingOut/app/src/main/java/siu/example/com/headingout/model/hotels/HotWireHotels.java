package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by samsiu on 5/7/16.
 */

@Root(name = "Hotwire")
public class HotWireHotels{
    @Element(name = "Errors", required = false)
    String errors;

    @ElementList(name="MetaData")
    List<MetaData> metaDataList;

    @ElementList(name="Result")
    List<HotelResult> resultList;

    @Element(name = "StatusCode")
    String statusCode;

    @Element(name = "StatusDesc")
    String statusDesc;

    public String getErrors() {
        return errors;
    }

    public List<MetaData> getMetaDataList() {
        return metaDataList;
    }

    public List<HotelResult> getResultList() {
        return resultList;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}

