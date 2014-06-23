package example1;

import server.RemoteObjectManager;
import core.Remote440Exception;

public class Calci implements CalciInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8893808116856487064L;
	
	// this variable will be called by the client but will be stored only on the server
	private int memoryVar = 0;
	RemoteObjectManager rom;
	public Calci(RemoteObjectManager r)
	{
		rom = r;
	}
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
		//RemoteObjectReference  ror = new RemoteObjectReference(server.Server.serverIp, Server.INITIAL_SERVER_PORT, bindName, "example1.Calci");
		//int i = Server.storeAndSend(ror,a, MessageType.REBIND);
		return (CalciInterface)rom.getActualObject(bindName);
	}

}
