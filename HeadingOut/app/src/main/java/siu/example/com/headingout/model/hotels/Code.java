package siu.example.com.headingout.model.hotels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by samsiu on 5/8/16.
 */
@Root(name = "Code")
class Code{
    @Element(name = "Code")
    String code;

    public String getCode() {
        return code;
    }
}