package registry;

import java.io.IOException;
import java.net.Socket;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.Message;
import communication.MessageType;

public class RegistryProcessor extends Thread{
	RorTable rorTable;
	Socket clientSocket;
	
	public RegistryProcessor(Object rorTable, Socket a){
		this.clientSocket = a;
		this.rorTable = (RorTable)rorTable; 
	}
	
	@Override
	public void run(){
		try {
			Message m = Communicator.receiveMessage(clientSocket);

            switch(m.type){
            case LOOKUP:
            	String bindName = m.content;
            	if(rorTable.containsEntry(bindName)){
            		//object exists Thus return corresponding RoR
            		Message returnMsg = new Message(rorTable.getRor(bindName), MessageType.LOOKUP);
            		Communicator.sendMessage(clientSocket, returnMsg);
            	}
            	else{
            		//create and send exception msg
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object not found in registry.");
            		Communicator.sendMessage(clientSocket, newExceptionMsg);
            	}
            	break;
            	
            case REMOVE:
            	bindName = m.content;
            	if(rorTable.containsEntry(bindName)){
            		//object exists Thus remove the RoR
            		rorTable.removeEntry(bindName);
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
            	String bindNameList = rorTable.createBindNameList();
            	Message returnMsg = new Message(null, MessageType.LIST, bindNameList);
            	Communicator.sendMessage(clientSocket, returnMsg);
            	break;
            	
            case BIND:
            	
            	bindName = m.content;
            	if(!rorTable.containsEntry(bindName)){
            		//object does not exist. Thus insert corresponding RoR into registry map
            		rorTable.InsertEntry(bindName, m.remoteObjectRef);
            		returnMsg = new Message(null, MessageType.BIND);
            		Communicator.sendMessage(clientSocket, returnMsg);
            	}
            	else{
            		//registry already has this key. Send exception msg
            		System.out.println("Object already existes in registry. Cannot BIND.");
            		ExceptionMessage newExceptionMsg = new ExceptionMessage("Object already existes in registry. Cannot BIND.");
            		Communicator.sendMessage(clientSocket, newExceptionMsg);
            	}
            	break;
            	
            	
            case REBIND:
            	bindName = m.content;
            	//replace irrespective of existence
            	rorTable.InsertEntry(bindName, m.remoteObjectRef);
            	//send confirmation
        		returnMsg = new Message(null, MessageType.REBIND);
        		Communicator.sendMessage(clientSocket, returnMsg);
        		
        		//DEBUG info
        		rorTable.displayRegistryMap();
            	break;	
            
            default:
            	//Unknown message type. Send exception msg
        		ExceptionMessage newExceptionMsg = new ExceptionMessage("Unindentified operation received at the registry server.");
        		Communicator.sendMessage(clientSocket, newExceptionMsg);
        		
            	System.out.println("Unindentified operation received at the registry server.");
            	break;
            }//end of switch
    
            
		} catch (InterruptedException | ClassNotFoundException | IOException e) {
			System.out.println("Communication failure: " + e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}


