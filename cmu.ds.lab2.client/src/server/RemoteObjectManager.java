package server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.Message;
import communication.MessageType;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteObjectReference;

public class RemoteObjectManager {
		// hashmap for storing String:ActualServerObject
		// it maps bindname to the actual object reference
		ConcurrentHashMap<String, Remote440> serverMap;
		ConcurrentHashMap<Remote440, RemoteObjectReference> serverRorMap;

		String registryIp;
		int registryPort;
		
		String serverIp;
		int serverPort;
		
		RemoteObjectManager(String registryIp, int registryPort, String serverIp, int serverPort) {
			serverMap = new ConcurrentHashMap<String, Remote440>();
			serverRorMap = new ConcurrentHashMap<Remote440, RemoteObjectReference>();
			this.registryIp = registryIp;
			this.registryPort = registryPort;
			
			this.serverIp = serverIp;
			this.serverPort = serverPort;
		}
		
		public RemoteObjectReference InsertEntry(String interfaceImpl, String bindName, Remote440 newObj, Boolean rebind) throws Remote440Exception{
			
			MessageType mt = (rebind==true)?MessageType.REBIND:MessageType.BIND;
			
			//Create Remote Object reference
			RemoteObjectReference r =  new RemoteObjectReference(serverIp, serverPort, bindName, interfaceImpl);
			
			// contact registry and register
			Message newmsg = new Message(r, mt, r.getBindName());
			Message recvdObj;
			try {
				recvdObj = Communicator.sendAndReceiveMessage(registryIp, registryPort, newmsg);
			} catch (ClassNotFoundException
					| InterruptedException | IOException e) {
				throw new Remote440Exception("Couldn't communicate with registry");
			}
			
			if(recvdObj.type != mt ) {
				if(recvdObj instanceof ExceptionMessage){
					Exception e = ((ExceptionMessage)recvdObj).getException();
					if(e instanceof Remote440Exception)
						throw (Remote440Exception)e;
					else
						throw new Remote440Exception("Unknown Error");
				}
				else
					throw new Remote440Exception( "Unknown error" );
			}
			
			serverMap.put(r.getBindName(), newObj);
			serverRorMap.put(newObj, r);
			return r;
		}
		
		public void RemoveEntry(String bindName) throws Remote440Exception {
			// contact registry and register
			Message newmsg = new Message(null, MessageType.REMOVE, bindName);
			Message inMsg;
			try {
				inMsg = Communicator.sendAndReceiveMessage(registryIp, registryPort, newmsg);
			} catch (ClassNotFoundException
					| InterruptedException | IOException e) {
				throw new Remote440Exception("Couln't communicate with registry");
			}
			
			
			if(!serverMap.containsKey(bindName)){
	    		//object does not exists Thus remove failed
				throw new Remote440Exception("Entry not found in server");
	    	}else if(inMsg.type != MessageType.REMOVE) {
	    		//check is received message is okay
	    		if(inMsg instanceof ExceptionMessage){
	    			Exception e = ((ExceptionMessage)inMsg).getException();
					if(e instanceof Remote440Exception)
						throw (Remote440Exception)e;
					else
						throw new Remote440Exception("Unknown Error");
	    		}
	    		else
	    			throw new Remote440Exception( "Unknown error" );
	    	}else{
    			//removed from registry, now remove from server 
    			serverRorMap.remove(serverMap.get(bindName));
    			serverMap.remove(bindName);
    		}
		}


		public Object getActualObject(String bindname) {
			return serverMap.get(bindname);
		}


		public boolean containsEntry(String bindname) {
			return serverMap.containsKey(bindname);
		}


		public RemoteObjectReference getRor(Remote440 remoteObject) {
			return serverRorMap.get(remoteObject);
		}
		
		public String displayRoRs(){
			String a="";
			for(Remote440 r : serverRorMap.keySet()){
				a = a + serverRorMap.get(r).getBindName() + " ";
			}
			return a;
		}
		
}
