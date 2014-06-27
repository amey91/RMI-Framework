package test2;

import core.Remote440;
import core.Remote440Exception;

public interface ZipCodeRList extends Remote440 {
    public String find(String city) throws Remote440Exception;
    public ZipCodeRList add(String city, String zipcode) throws Remote440Exception;
    public ZipCodeRList next() throws Remote440Exception;
}
