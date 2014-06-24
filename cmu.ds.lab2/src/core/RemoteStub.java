package core;

import java.io.IOException;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.InvocationMessage;
import communication.Message;
import communication.ReturnMessage;

public class RemoteStub {
	RemoteObjectReference ror;
	
	public RemoteObjectReference getRor(){
		return this.ror;
	}
	
	public RemoteStub(RemoteObjectReference ror){
		this.ror = ror;
	}
	

	public Object invoke(String methodName, Object[] objects, Class<?>[] classes) throws Remote440Exception {
		try {
			
			// check is param contains Stub. If yes, then convert to Ror. Set the corresponding "converted" boolean to true
			// if param is RoR itself, dont change the "converted" boolean flag (let it remain false)
			boolean[] converted = new boolean[objects.length];
			int count =0;
			for(Class<?> sampleClass : classes){
				if(sampleClass == RemoteStub.class){
					converted[count] = true;
					objects[count] = ((RemoteStub) objects[count]).getRor();
				}
				count ++;
			}
			
			InvocationMessage invMsg = new InvocationMessage(this.ror, methodName, objects, classes, converted);
			//iterate params and replace all objects that inherit Remote440 with their Ror
			Message returnResult = Communicator.sendAndReceiveMessage(ror.getServerIp(),ror.getServerPort(), invMsg);
			if( returnResult instanceof ExceptionMessage)
				throw ((ExceptionMessage)returnResult).getException();
			
			// handle if return type itself is RoR
			// case 1. it is meant to be Remote obj. Case 2. it is meant to be RoR
			Object result = ((ReturnMessage)returnResult).result;
			// check if the return message is a remote object, then return a stub for the remote object
			// convert only if the object was converted from local to RoR at server
			// dont convert RoR return objects
			if(((ReturnMessage)returnResult).converted)
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


