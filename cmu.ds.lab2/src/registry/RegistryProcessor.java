package registry;

import java.io.IOException;
import java.net.Socket;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.Message;
import communication.MessageType;
import communication.SocketThread;

public class RegistryProcessor extends SocketThread{
	//constructor
	public RegistryProcessor(Socket a){
		super(a);
		this.clientSocket = a;
	}
	
	@Override
	public void run(){
		try {
			Message m = Communicator.receiveMessage(clientSocket);

            switch(m.type){
            case LOOKUP:
            	String bindName = m.comments;
            	if(RegistryServer.registryMap.containsKey(bindName)){
            		//object exists Thus return corresponding RoR
            		Message returnMsg = new Message(RegistryServer.registryMap.get(bindName), MessageType.LOOKUP);
            		Communicator.sendMessage(clientSocket, returnMsg);
            	}
            	else{
            		//create and send exception msg
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object not found in registry.");
            		Communicator.sendMessage(clientSocket, newExceptionMsg);
            	}
            	break;
            	
            case REMOVE:
            	bindName = m.comments;
            	if(RegistryServer.registryMap.containsKey(bindName)){
            		//object exists Thus remove the RoR
            		RegistryServer.registryMap.remove(bindName);
            		// send pseudo message to indicate success 
            		Message returnMsg = new Message(null, MessageType.REMOVE);
            		Communicator.sendMessage(clientSocket, returnMsg);
            	}
            	else{
            		// object does not exist. Send exception
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object does not exist. Cannot delete");
            		Communicator.sendMessage(clientSocket, newExceptionMsg);
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
            	Communicator.sendMessage(clientSocket, returnMsg);
            	break;
            	
            case BIND:
            	
            	bindName = m.comments;
            	if(!RegistryServer.registryMap.containsKey(bindName)){
            		//object does not exist. Thus insert corresponding RoR into registry map
            		RegistryServer.registryMap.put(bindName, m.remoteObjectRef);
            		returnMsg = new Message(null, MessageType.BIND);
            		Communicator.sendMessage(clientSocket, returnMsg);
            	}
            	else{
            		//TODO handle at server
            		//registry already has this key. Send exception msg
            		System.out.println("Object already existes in registry. Cannot BIND.");
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object already existes in registry. Cannot BIND.");
            		Communicator.sendMessage(clientSocket, newExceptionMsg);
            	}
            	break;
            	
            	
            case REBIND:
            	bindName = m.comments;
            	//replace irrespective of existence
            	RegistryServer.registryMap.put(bindName, m.remoteObjectRef);
            	//send confirmation
        		returnMsg = new Message(null, MessageType.REBIND);
        		Communicator.sendMessage(clientSocket, returnMsg);
        		
        		//DEBUG info
        		RegistryServer.displayRegistryMap();
            	break;	
            
            default:
            	//registry already has this key. Send exception msg
        		ExceptionMessage newExceptionMsg = new ExceptionMessage("Unindentified operation received at the registry server.");
        		Communicator.sendMessage(clientSocket, newExceptionMsg);
        		
            	System.out.println("Unindentified operation received at the registry server.");
            		
            }//end of switch
    
            
		} catch (InterruptedException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}


