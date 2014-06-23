package core;

import java.io.IOException;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.InvocationMessage;
import communication.Message;
import communication.ReturnMessage;

public class RemoteStub {
	RemoteObjectReference ror;
	
	public RemoteStub(RemoteObjectReference ror){
		this.ror = ror;
	}
	
	public RemoteObjectReference getRor(){
		return this.ror;
	}
	
	public Object invoke(String methodName, Object[] objects, Class<?>[] classes) throws Remote440Exception {
		try {
			// open port and send
			InvocationMessage invMsg = new InvocationMessage(this.ror, methodName, objects, classes);
			//iterate params and replace all objects that inherit Remote440 with their Ror
			
			Message returnResult = Communicator.sendAndReceiveMessage(ror.getServerIp(),ror.getServerPort(), invMsg);
			if( returnResult instanceof ExceptionMessage)
				throw ((ExceptionMessage)returnResult).getException();
			
			Object result = ((ReturnMessage)returnResult).result;
			
			//check if the return message is a remote object, then return a stub for the remote object
			if(result instanceof RemoteObjectReference)
			{
				RemoteObjectReference ror = (RemoteObjectReference)result;
				result = ror.toStub();
			}
			
			return result;
			
		} catch (ClassNotFoundException
				| InterruptedException | IOException e) {
			e.printStackTrace();
			throw new Remote440Exception(e.getMessage());
		}
		
	}
}


