package core;

import java.io.IOException;
import java.net.InetAddress;

import communication.Communicator;
import communication.InvocationMessage;
import communication.Message;

public class RemoteStub {
	RemoteObjectReference ror;
	
	public RemoteStub(RemoteObjectReference ror){
		this.ror = ror;
	}
	
	public Object invoke(String methodName, Object[] objects, Class<?>[] classes) throws Remote440Exception {
		// open port and send
		InvocationMessage invMsg = new InvocationMessage(this.ror, methodName, objects, classes);
	
		Message returnResult;
		try {
			returnResult = Communicator.sendAndReceiveMessage(InetAddress.getByName(this.ror.serverIP).toString(),this.ror.serverPort, invMsg);
		} catch (ClassNotFoundException
				| InterruptedException | IOException e) {
			e.printStackTrace();
			throw new Remote440Exception(e.getMessage());
		}
		
		// TODO if the received object is remote object, then return `a stub object
		
		return (Object)returnResult;
	}
}


