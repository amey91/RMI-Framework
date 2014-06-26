package example1;

import core.Remote440;
import core.Remote440Exception;


public interface CalciInterface extends Remote440{
	public int add(int a, int b) throws Remote440Exception ;
	public void setMemory(int b) throws Remote440Exception;
	public int addMemory(int b) throws Remote440Exception;
	public CalciInterface getNewCalci(String bnindName) throws Remote440Exception, NullPointerException;
	public String getUpperCaseString(String simpleString) throws Remote440Exception;
}
