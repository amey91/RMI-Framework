package server;

import java.net.Socket;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.InvocationMessage;
import communication.Message;
import communication.ReturnMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import core.Remote440;
import core.Remote440Exception;
import core.RemoteObjectReference;

public class ServerProcessor extends Thread {
	RemoteObjectManager remoteObjectManager;
	Socket clientSocket;
	public ServerProcessor(Object remoteObjectManager, Socket clientSocket){
		this.clientSocket = clientSocket;
		this.remoteObjectManager = (RemoteObjectManager)remoteObjectManager;
	}
	
	@Override
	public void run(){
		try {

			InvocationMessage newMsg = null;

			Message recvMessage = Communicator.receiveMessage(clientSocket);
			
			//read object invocation and unmarshall elements
			newMsg = (InvocationMessage) recvMessage;
			
			Class<?> classRef = null;
			
			
			String className = newMsg.remoteObjectRef.getInterfaceImplemented();
			
			// instantiate stub class by name 
			classRef = Class.forName(className);
			
			// TODO get class by HTTP
			
			// iterate over params. If param has been converted, then re-convert to local instance
			int count =0;
			for(Boolean b: newMsg.converted){
				if(b == true){
					//If param has been converted, then re-convert to local instance
					newMsg.objectArray[count] = remoteObjectManager.getActualObject(((RemoteObjectReference)newMsg.objectArray[count]).getBindName());
				}
				count++;
			}
			
			Method myMethod = classRef.getMethod(newMsg.methodName, newMsg.classArray);
			
						
			if(!remoteObjectManager.containsEntry(newMsg.remoteObjectRef.getBindName())){
				throw new IllegalAccessException("Remote Object does not exist.");
			}else{
				String bn = newMsg.remoteObjectRef.getBindName();
				Object returnObj = myMethod.invoke(remoteObjectManager.getActualObject(bn), newMsg.objectArray);
				// if return object is remote reference for client (i.e. local to server),
				// then replace it with its remote reference
				ReturnMessage r;
				if( returnObj instanceof Remote440){
					// This has to be converted to a stub at the client
					Object rorObj = remoteObjectManager.getRor((Remote440) returnObj);
					
					//object not in the registry
					if(rorObj == null )
						rorObj = remoteObjectManager.InsertEntry(returnObj.getClass().getInterfaces()[0].toString().substring(10), "dummy", (Remote440)returnObj, true);
					
					r = new ReturnMessage(rorObj,true); 
				}
				else{
					// Not a remote object. Don't convert at client into stub
					r= new ReturnMessage(returnObj,false); 
				}
				// package the result
				Communicator.sendMessage(clientSocket, r);
			}
						
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException 
				| IllegalAccessException | IllegalArgumentException | IOException | InterruptedException | Remote440Exception e) {
			//send error message
			try {
				ExceptionMessage em = new ExceptionMessage(e.getMessage());
				Communicator.sendMessage(clientSocket, em);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			try {
				ExceptionMessage em = new ExceptionMessage(e);
				Communicator.sendMessage(clientSocket, em);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
			//e.printStackTrace(); -> results in invocation exception
		}finally{
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}