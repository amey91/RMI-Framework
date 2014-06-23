package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import communication.Communicator;
import communication.Message;
import communication.MessageType;
import core.Remote440;
import core.RemoteObjectReference;


public class Server {
	// hashmap for storing String:ActualServerObject
	// it maps bindname to the actual object reference
	static ConcurrentHashMap<String, Remote440> serverMap = new ConcurrentHashMap<String, Remote440>();
	static ConcurrentHashMap<Remote440, RemoteObjectReference> serverRorMap = new ConcurrentHashMap<Remote440, RemoteObjectReference>();
	
	//overridden by cmd arguments 
	public static int INITIAL_SERVER_PORT = 5555;
	// need access to server ip from example packages
	public static String serverIp;
	static String registryIp;
	static int registryPort;
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		if(args.length != 2){
			
			//deployed using eclipse
			log("Usage: java Server <registry_ip> <registry_port>");
			registryIp = "127.0.0.1";
			registryPort = 1099;
			
			ServerSocket ssss = new ServerSocket(6666);
			// TODO CHANGE THIS IP!
			serverIp = ssss.getInetAddress().getHostAddress();
			// InetAddress.getLocalHost()
			ssss.close();
			
		}else{
			//deployed on unix.andrew
			registryIp = args[0];
			registryPort = Integer.parseInt(args[1]);
			
			try {
				serverIp = InetAddress.getLocalHost().getHostAddress();
				
				
			} catch (UnknownHostException e1) {
				log("Error while creating remote object");
				e1.printStackTrace();
				return;
			}
		}
		
		RemoteObjectReference  r1 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci1" , "example1.Calci");
		RemoteObjectReference  r2 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci2" , "example1.Calci");
		RemoteObjectReference  r3 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci3" , "example1.Calci");
		RemoteObjectReference  r4 = new RemoteObjectReference(serverIp, INITIAL_SERVER_PORT, "Calci4" , "example1.Calci");
		Remote440 a = new example1.Calci();
		Remote440 a2 = new example1.Calci();
		Remote440 a3 = new example1.Calci();
		Remote440 a4 = new example1.Calci();
		// TODO @test to test rebind
		int i = storeAndSend(r1,a, MessageType.BIND);
		int i2 = storeAndSend(r2,a2, MessageType.REBIND);
		int i3 = storeAndSend(r3,a3,MessageType.REBIND);
		int i4 = storeAndSend(r4,a4,MessageType.REBIND);
		
		// remove Calci1 object from this server as well as registry server.
		int i5 = deleteAndRemove("Calci1");
		Communicator.listenForMessages(Server.INITIAL_SERVER_PORT, ServerProcessor.class);
		
	}//end of main
	
	// @return 0 if successful 
	// @return return -1 if any error
	// TODO I changed this method to public so that I can call it from Calci to create a new remote object 
	//   from within Calci class itself
	public static int storeAndSend(RemoteObjectReference r, Remote440 newObj, MessageType mt) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		// contact registry and register
		Message newmsg = new Message(r, mt, r.bindname);
		
		Message recvdObj = Communicator.sendAndReceiveMessage(Server.registryIp,Server.registryPort, newmsg);
	
		if(recvdObj.type != mt ){
			System.out.println("fail:"+mt.toString());
			if(recvdObj == null)
				System.out.println("Recevied obj is null");
			return -1;
		}
		serverMap.put(r.bindname, newObj);
		serverRorMap.put(newObj, r);
		
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
    		{
    			Server.serverRorMap.remove(Server.serverMap.get(bindName));
    			Server.serverMap.remove(bindName);
    		}
		return 0;
	}
	
	
	public static void log(String a){
		System.out.println(a);
	}
}

