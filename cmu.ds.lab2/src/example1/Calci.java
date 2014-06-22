package example1;

import java.io.IOException;
import java.net.UnknownHostException;

import communication.MessageType;
import server.Server;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteObjectReference;

public class Calci implements CalciInterface {
	private int memoryVar = 0;
	
	@Override
	public int add(int a, int b) {
		
		return a+b;
	}

	@Override
	public void setMemory(int b) {
		memoryVar = b;
	}

	@Override
	public int addMemory(int b) {
		return this.memoryVar + b;
		
	}

	@Override
	public CalciInterface getNewCalci(String bindName) throws Remote440Exception {
		// TODO CHANGE the way IPs are assigned
		RemoteObjectReference  ror = new RemoteObjectReference("127.0.0.1", Server.INITIAL_SERVER_PORT, bindName, "example1.Calci");
		Remote440 a = new example1.Calci();
		try {
			int i = Server.storeAndSend(ror,a, MessageType.REBIND);
		} catch (ClassNotFoundException | IOException
				| InterruptedException e) {
			throw new Remote440Exception("Could not create remote object from within remote object");
		}
		return (CalciInterface) a;
	}

}
