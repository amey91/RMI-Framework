package example1;

import java.io.IOException;

import communication.MessageType;
import server.Server;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteObjectReference;

public class Calci implements CalciInterface {

	private static final long serialVersionUID = 3423680540325706606L;
	
	// this variable will be called by the client but will be stored only on the server
	// i.e. this is a stateful server variable
	private int memoryVar = 0;
	
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
	public CalciInterface getNewCalci(String bindName) throws Remote440Exception {
		// TODO CHANGE the way IPs are assigned
		RemoteObjectReference  ror = new RemoteObjectReference(server.Server.serverIp, Server.INITIAL_SERVER_PORT, bindName, "example1.Calci");
		Remote440 a = new example1.Calci();
		try {
			int i = Server.storeAndSend(ror,a, MessageType.REBIND);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			// abstract away the server exception
			throw new Remote440Exception("Could not create remote object from within remote object :(");
		}
		return (CalciInterface) a;
	}

	@Override
	public String getUpperCaseString(String simpleString) throws Remote440Exception {
		return simpleString.toUpperCase();
	}

	
	

}
