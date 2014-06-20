package registry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import communication.ExceptionMessage;
import communication.MessageType;


import communication.Message;
import core.RemoteObjectReference;

public class RegistryServer {
	// hashmap mapping bindname to ROR
	public static ConcurrentHashMap<String, RemoteObjectReference> registryMap;
	
	public static int INITIAL_REGISTRY_PORT = 1099;
	
	public static void main(String args[]){
		if(args.length>1){
			log("Usage: java RegistryServer <port>.");
			log("Port is optional. If you specify port here, please specify the same port for client and server.");
			System.exit(0);
		}
		if(args.length ==1){
			RegistryServer.INITIAL_REGISTRY_PORT = Integer.parseInt(args[0]);
		}
		else{
			log("Port for RegistryServer not specified. Starting on default port 1099.");
		}
		
		//initialize hashmap
		registryMap = new ConcurrentHashMap<String, RemoteObjectReference>();
		//initialize listening socket
		ServerSocket listeningSocket = null;
		
		while(true){
			// setup the heartbeat socket for the server that listens to clients 
			try {
				listeningSocket = new ServerSocket(RegistryServer.INITIAL_REGISTRY_PORT);
				log("RegistryServer waiting for new message...");
				Socket clientSocket = listeningSocket.accept();
				new Thread(new RegistryProcessor(clientSocket)).start();
				
			} catch (IOException e) {
				log("Error while opening port at registry server");
				e.printStackTrace();
			}
		}
	
	}//end of main
	
	// print to console
	public static void log(String a){
		System.out.println(a);
	}
	
	public static void displayRegistryMap(){
		
		for(String k:registryMap.keySet()){
			log("Bindname: "+k + " "+registryMap.get(k));
		}
	}
}


class RegistryProcessor extends java.lang.Thread{
	Socket clientSocket;
	
	//constructor
	public RegistryProcessor(Socket a){
		this.clientSocket = a;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		try {
			ObjectInputStream inobj = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream outObj = new ObjectOutputStream(clientSocket.getOutputStream());
			//receive object
			Object newObj = (Object)inobj.readObject();
            if (!(newObj instanceof Message)){
            	System.out.println("Registry received a unidentified/corrupted message");
            	this.suspend();
            }
            
            //message is valid. Check MessageType
            Message m = (Message)newObj;
            
            switch(m.type){
            case LOOKUP:
            	String bindName = m.comments;
            	if(RegistryServer.registryMap.containsKey(bindName)){
            		//object exists Thus return corresponding RoR
            		Message returnMsg = new Message(RegistryServer.registryMap.get(bindName), MessageType.LOOKUP);
            		outObj.writeObject(returnMsg);
            	}
            	else{
            		//create and send exception msg
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object not found in registry.");
            		outObj.writeObject(newExceptionMsg);
            		
            	}
            	break;
            	
            case REMOVE:
            	bindName = m.comments;
            	if(RegistryServer.registryMap.containsKey(bindName)){
            		//object exists Thus remove the RoR
            		RegistryServer.registryMap.remove(bindName);
            		// send pseudo message to indicate success 
            		Message returnMsg = new Message(null, MessageType.REMOVE);
            		outObj.writeObject(returnMsg);
            	}
            	else{
            		// object does not exist. Send exception
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object does not exist. Cannot delete");
            		outObj.writeObject(newExceptionMsg);
            	}
            	break;
            	
            case LIST:
            	String bindNameList = "";
            	// for each entry
            	for(String s : RegistryServer.registryMap.keySet() ){
            		//append bindname to the return string
            		bindNameList = bindNameList +  s + " ";
            	}
            	//TODO handle at client
            	Message returnMsg = new Message(null, MessageType.LIST, bindNameList);
        		outObj.writeObject(returnMsg);
            	break;
            	
            case BIND:
            	
            	bindName = m.comments;
            	if(!RegistryServer.registryMap.containsKey(bindName)){
            		//object does not exist. Thus insert corresponding RoR into registry map
            		RegistryServer.registryMap.put(bindName, m.remoteObjectRef);
            		returnMsg = new Message(null, MessageType.BIND);
            		outObj.writeObject(returnMsg);
            	}
            	else{
            		//TODO handle at server
            		//registry already has this key. Send exception msg
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object already existes in registry. Cannot BIND.");
            		outObj.writeObject(newExceptionMsg);
            	}
            	break;
            	
            	
            case REBIND:
            	bindName = m.comments;
            	//replace irrespective of existence
            	RegistryServer.registryMap.put(bindName, m.remoteObjectRef);
            	//send confirmation
        		returnMsg = new Message(null, MessageType.REBIND);
        		outObj.writeObject(returnMsg);
        		RegistryServer.displayRegistryMap();
            	break;	
            
            default:
            	//registry already has this key. Send exception msg
        		ExceptionMessage newExceptionMsg = new ExceptionMessage("Unindentified operation received at the registry server.");
        		outObj.writeObject(newExceptionMsg);
            	System.out.println("Unindentified operation received at the registry server.");
            		
            }//end of switch
            
            try {
				// give some buffer for outputstream to complete
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// do nothing
				e.printStackTrace();
			}finally{
				outObj.close();
				inobj.close();
				clientSocket.close();
			}
            
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}