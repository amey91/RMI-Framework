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
			
			String className = newMsg.remoteObjectRef.getInterfaceImplemented() + "Interface";
			
			// instantiate stub class by name 
			classRef = Class.forName(className);
			
			// TODO get class by HTTP
				
			Method myMethod = classRef.getMethod(newMsg.methodName, newMsg.classArray);
						
			if(!remoteObjectManager.containsEntry(newMsg.remoteObjectRef.getBindName())){
				ExceptionMessage em = new ExceptionMessage("Bindname not found at server");
				Communicator.sendMessage(clientSocket, em);
			}else{
				String bn = newMsg.remoteObjectRef.getBindName();
				Object ok = myMethod.invoke(remoteObjectManager.getActualObject(bn), newMsg.objectArray);
				// if return object is remote reference for client (i.e. local to server),
				// then replace it with its remote reference
				if( ok instanceof Remote440)
					ok = remoteObjectManager.getRor((Remote440) ok);	
					// package the result
					ReturnMessage r = new ReturnMessage(ok);
					Communicator.sendMessage(clientSocket, r);
			}
						
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException 
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IOException | InterruptedException  e) {
			//send error message
			try {
				ExceptionMessage em = new ExceptionMessage(e.getMessage());
				Communicator.sendMessage(clientSocket, em);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}