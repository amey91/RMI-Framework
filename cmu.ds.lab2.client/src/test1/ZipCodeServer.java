package test1;
import core.Remote440;
import core.Remote440Exception;

public interface ZipCodeServer extends Remote440 // extends YourRemote or whatever
{
    public void initialise(ZipCodeList newlist) throws Remote440Exception;
    public String find(String city) throws Remote440Exception;;
    public ZipCodeList findAll() throws Remote440Exception;;
    public void printAll() throws Remote440Exception;;
}

