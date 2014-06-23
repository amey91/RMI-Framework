package core;

import java.io.IOException;
import java.net.InetAddress;

import communication.Communicator;
import communication.InvocationMessage;
import communication.Message;
import communication.ReturnMessage;

public class RemoteStub {
	RemoteObjectReference ror;
	
	public RemoteStub(RemoteObjectReference ror){
		this.ror = ror;
	}
	
	public Object invoke(String methodName, Object[] objects, Class<?>[] classes) throws Remote440Exception {
		// open port and send
		InvocationMessage invMsg = new InvocationMessage(this.ror, methodName, objects, classes);
		//iterate params and replace all objects that inherit Remote440 with their Ror
		
		Message returnResult;
		try {
			returnResult = Communicator.sendAndReceiveMessage(this.ror.serverIP.toString(),this.ror.serverPort, invMsg);
		} catch (ClassNotFoundException
				| InterruptedException | IOException e) {
			e.printStackTrace();
			throw new Remote440Exception(e.getMessage());
		}
		Object result = ((ReturnMessage)returnResult).result;
		//check if the return message is a remote object, then return a stub for the remote object
		if(result instanceof RemoteObjectReference)
		{
			RemoteObjectReference ror = (RemoteObjectReference)result;
			result = Naming.RorToStub(ror);
		}
		
		return result;
	}
}


