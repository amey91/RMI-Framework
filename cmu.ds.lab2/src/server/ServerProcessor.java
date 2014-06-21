package server;

import java.net.Socket;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.InvocationMessage;
import communication.Message;
import communication.ReturnMessage;
import communication.SocketThread;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import example1.CalciInterface;

class ServerProcessor extends SocketThread {
	
	public ServerProcessor(Socket clientSocket){
		super(clientSocket);
	}
	
	@Override
	public void run(){
		try {

			InvocationMessage newMsg = null;

			Message recvMessage = Communicator.receiveMessage(clientSocket);
			
			//read object invocation and unmarshall elements
			newMsg = (InvocationMessage) recvMessage;
			
			Class<?> classRef = null;
			
			String className = newMsg.remoteObjectRef.interfaceImplemented+"Interface";
			// instantiate stub class by name 
			
			classRef = Class.forName(className);
			
			// TODO get class by HTTP
				
			Method myMethod = classRef.getMethod(newMsg.methodName, newMsg.classArray);
						
			if(!Server.serverMap.containsKey(newMsg.remoteObjectRef.bindname)){
				ExceptionMessage em = new ExceptionMessage("Bindname not found at server");
				Communicator.sendMessage(clientSocket, em);
			}else{
				Object ok = myMethod.invoke(Server.serverMap.get(newMsg.remoteObjectRef.bindname), newMsg.objectArray);
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