package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

import communication.ExceptionMessage;
import communication.InvocationMessage;
import communication.Message;
import communication.MessageType;
import communication.ReturnMessage;
import core.Remote440;
import core.RemoteObjectReference;
import example1.CalciInterface;
import registry.RegistryServer;

public class Server {
	// hashmap for storing String:ActualServerObject
	// it maps bindname to the actual object reference
	static ConcurrentHashMap<String, Object> serverMap = new ConcurrentHashMap<String, Object>();
	
	//overridden by cmd arguments 
	public static int INITIAL_SERVER_PORT = 5555;
	static String serverIp;
	static String registryIp;
	static int registryPort;
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException{
		if(args.length != 2){
			log("Usage: java Server <registry_ip> <registry_port>");
			System.exit(0);
		}
		
		registryIp = args[0];
		registryPort = Integer.parseInt(args[1]);
		
		try {
			serverIp = InetAddress.getLocalHost().getHostAddress();
			
		} catch (UnknownHostException e1) {
			log("Error while creating remote object");
			e1.printStackTrace();
			return;
		}
		
		
		RemoteObjectReference  r1 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci1" , "Calci");
		RemoteObjectReference  r2 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci2" , "Calci");
		RemoteObjectReference  r3 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci3" , "Calci");
		RemoteObjectReference  r4 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci4" , "Calci");
		Object a = new Object();
		Object a2 = new Object();
		Object a3 = new Object();
		Object a4 = new Object();
		// TODO @test to test rebind
		int i = storeAndSend(r1,a);
		int i2 = storeAndSend(r2,a2);
		int i3 = storeAndSend(r3,a3);
		int i4 = storeAndSend(r4,a4);
		
		// TODO @test  remove
		int i5 = deleteAndRemove("Calci1");
		
		ServerSocket listeningSocket = null;
		
		while(true){
			// setup the invocation socket for the server that listens to clients 
			try {
				listeningSocket = new ServerSocket(Server.INITIAL_SERVER_PORT);
				log("Server waiting for new message...");
				Socket clientSocket = listeningSocket.accept();
				new Thread(new ServerProcessor(clientSocket)).start();
				
			} catch (IOException e) {
				log("Error while opening port at server");
				e.printStackTrace();
			}
		}
		
	}
	
	// @return 0 if successful 
	// @return return -1 if any error
	
	private static int storeAndSend(RemoteObjectReference r, Object newObj) throws UnknownHostException, IOException{
		serverMap.put(r.bindname, newObj);
		// contact registry and register
		Message newmsg = new Message(r, MessageType.REBIND, r.bindname);
		
		Socket registrySocket = new Socket(Server.registryIp,Server.registryPort);
		ObjectInputStream inobj = new ObjectInputStream(registrySocket.getInputStream());
		ObjectOutputStream outObj = new ObjectOutputStream(registrySocket.getOutputStream());
		outObj.writeObject(newmsg);
		
		Message recvdObj;
		try {
			recvdObj = (Message)inobj.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
		if(recvdObj.type != MessageType.REBIND ){
			return -1;
		}
		return 0;
		
	}
	
	// @return 0 if successful. -1 if not
	public static int deleteAndRemove(String bindName) throws UnknownHostException, IOException, ClassNotFoundException{
		// contact registry and register
		Message newmsg = new Message(null, MessageType.REMOVE, bindName);
		
		Socket registrySocket = new Socket(Server.registryIp,Server.registryPort);
		ObjectInputStream inobj = new ObjectInputStream(registrySocket.getInputStream());
		ObjectOutputStream outObj = new ObjectOutputStream(registrySocket.getOutputStream());
		outObj.writeObject(newmsg);
		Message inMsg = (Message) inobj.readObject();
		
		if(!Server.serverMap.containsKey(bindName)){
    		//object does not exists Thus remove failed
    		return -1;
    	}else
    		//check is received message is okay
    		if(inMsg.type != MessageType.REMOVE)
    			return -1;
    		else
    			RegistryServer.registryMap.remove(bindName);
		return 0;
	}
	
	
	public static void log(String a){
		System.out.println(a);
	}
}


class ServerProcessor extends java.lang.Thread {
	Socket clientSocket=null;
	
	public ServerProcessor(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run(){
		ObjectInputStream inobj = null;
		ObjectOutputStream outObj = null ;
		InvocationMessage newMsg = null;
		try {
			inobj = new ObjectInputStream(clientSocket.getInputStream());
			outObj = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		//read object invocation and unmarshall elements
		try {
			newMsg = (InvocationMessage) inobj.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			Class<?> classRef = null;
			
			
			String className = newMsg.remoteObjectRef.interfaceImplemented+"Interface";
			// instantiate stub class by name 
			
			classRef = Class.forName(className);
			
			// TODO get class by HTTP
				
			Method myMethod = classRef.getMethod(newMsg.methodName, newMsg.classArray);
						
			if(!Server.serverMap.containsKey(newMsg.remoteObjectRef.bindname)){
				ExceptionMessage em = new ExceptionMessage("Bindname not found at server");
				outObj.writeObject(em);	
			}else{
				Object ok = myMethod.invoke(Server.serverMap.get(newMsg.remoteObjectRef.bindname), newMsg.objectArray);
				ReturnMessage r = new ReturnMessage(ok);
				outObj.writeObject(r);
			}
						
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException 
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IOException  e) {
			//send error message
			try {
				ExceptionMessage em = new ExceptionMessage(e.getMessage());
				outObj.writeObject(em);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				inobj.close();
				outObj.close();
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
		
	}
	
}

