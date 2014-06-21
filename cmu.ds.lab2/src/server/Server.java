package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import communication.Communicator;
import communication.Message;
import communication.MessageType;
import core.RemoteObjectReference;


public class Server {
	// hashmap for storing String:ActualServerObject
	// it maps bindname to the actual object reference
	static ConcurrentHashMap<String, Object> serverMap = new ConcurrentHashMap<String, Object>();
	
	//overridden by cmd arguments 
	public static int INITIAL_SERVER_PORT = 5555;
	static String serverIp;
	static String registryIp;
	static int registryPort;
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		if(args.length != 2){
			log("Usage: java Server <registry_ip> <registry_port>");
			System.exit(0);
		}
		
		registryIp = args[0];
		registryPort = Integer.parseInt(args[1]);
		/*
		try {
			serverIp = InetAddress.getLocalHost().getHostAddress();
			
			
		} catch (UnknownHostException e1) {
			log("Error while creating remote object");
			e1.printStackTrace();
			return;
		}
		*/
		serverIp = "127.0.0.1";
		
		RemoteObjectReference  r1 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci1" , "example1.Calci");
		RemoteObjectReference  r2 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci2" , "example1.Calci");
		RemoteObjectReference  r3 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci3" , "example1.Calci");
		RemoteObjectReference  r4 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci4" , "example1.Calci");
		Object a = new Object();
		Object a2 = new Object();
		Object a3 = new Object();
		Object a4 = new Object();
		// TODO @test to test rebind
		int i = storeAndSend(r1,a, MessageType.BIND);
		int i2 = storeAndSend(r2,a2, MessageType.REBIND);
		int i3 = storeAndSend(r3,a3,MessageType.REBIND);
		int i4 = storeAndSend(r4,a4,MessageType.REBIND);
		
		// TODO @test  remove
		int i5 = deleteAndRemove("Calci1");
		
		Communicator.listenForMessages(Server.INITIAL_SERVER_PORT, ServerProcessor.class);
		
	}//end of main
	
	// @return 0 if successful 
	// @return return -1 if any error
	private static int storeAndSend(RemoteObjectReference r, Object newObj, MessageType mt) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		serverMap.put(r.bindname, newObj);
		// contact registry and register
		Message newmsg = new Message(r, mt, r.bindname);
		
		Message recvdObj = Communicator.sendAndReceiveMessage(Server.registryIp,Server.registryPort, newmsg);
	
		if(recvdObj.type != mt ){
			return -1;
		}
		return 0;
		
	}
	
	// @return 0 if successful. -1 if not
	public static int deleteAndRemove(String bindName) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		// contact registry and register
		Message newmsg = new Message(null, MessageType.REMOVE, bindName);
		Message inMsg = Communicator.sendAndReceiveMessage(Server.registryIp,Server.registryPort, newmsg);
		
		if(!Server.serverMap.containsKey(bindName)){
    		//object does not exists Thus remove failed
    		return -1;
    	}else
    		//check is received message is okay
    		if(inMsg.type != MessageType.REMOVE)
    			return -1;
    		else
    			Server.serverMap.remove(bindName);
		return 0;
	}
	
	
	public static void log(String a){
		System.out.println(a);
	}
}

