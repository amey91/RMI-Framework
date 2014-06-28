package example1;

import java.io.IOException;

import communication.MessageType;
import core.Remote440;
import server.RemoteObjectManager;
import core.Remote440Exception;

public class Calci implements CalciInterface {

	private static final long serialVersionUID = 3423680540325706606L;
	
	// this variable will be called by the client but will be stored only on the server
	// i.e. this is a stateful server variable
	private int memoryVar = 0;
	
	RemoteObjectManager rom;
	public Calci(RemoteObjectManager r)
	{
		rom = r;
	}
	
	// add two stateless integers
	@Override
	public int add(int a, int b) {
		return a+b;
	}

	@Override
	public void setMemory(int b) {
		// set the private variable to given value
		memoryVar = b;
	}

	@Override
	public int addMemory(int b) {
		// adds the input variable to the private integer
		return this.memoryVar + b;
		
	}

	@Override
	public CalciInterface getNewCalci(String bindName) throws Remote440Exception, NullPointerException {
		
		CalciInterface ci = (CalciInterface)rom.getActualObject(bindName);
		// check if this object doesnt exist in the server 
		if(ci==null)
			throw new NullPointerException("Not Found");
		else
			return ci;
	}

	@Override
	public String getUpperCaseString(String simpleString) throws Remote440Exception {
		return simpleString.toUpperCase();
	}

	
	

}
