package core;

import java.io.IOException;
import java.net.UnknownHostException;

import communication.Communicator;
import communication.ExceptionMessage;
import communication.Message;
import communication.MessageType;
import core.Remote440Exception;


// @referred http://docs.oracle.com/javase/7/docs/api/java/rmi/Naming.html
public class Naming {
	
	private static String[] getRegistryAndBindname(String address) throws Remote440Exception {
		if(address == null || address == ""){
			throw new Remote440Exception("Invalid Address");
		}

		if(address.indexOf("/") == -1){
			new Remote440Exception("No bindname provided");
		}
		
		String[] raw = address.split("/");
		String bindName = raw[1].trim();
		
		try {
			String[] ipPort = Communicator.addressToIPPort(raw[0]);
			return new String[]{ ipPort[0], ipPort[1], bindName};
		} catch (UnknownHostException e) {
			throw new Remote440Exception(e.getMessage());
		}
	}
	
	public static Remote440 lookup(String address) throws Remote440Exception {
		
		try {
			String[] ipPortBindName = getRegistryAndBindname(address);
			
			Message newmsg = new Message(null, MessageType.LOOKUP, ipPortBindName[2]);
			Message recvdObj;
			recvdObj = Communicator.sendAndReceiveMessage(ipPortBindName[0], Integer.parseInt(ipPortBindName[1]), newmsg);
			if(recvdObj.type != MessageType.LOOKUP){
				if( recvdObj instanceof ExceptionMessage)
				{
					Exception e = ((ExceptionMessage)recvdObj).getException();
					if(e instanceof Remote440Exception)
						throw (Remote440Exception)e;
					else
						throw new Remote440Exception("Unknown Error");
				}
				else
					throw new Remote440Exception("Unknown Error");
			}
			
			RemoteObjectReference ror = recvdObj.remoteObjectRef;
			return ror.toStub();
			
		} catch (ClassNotFoundException | InterruptedException | IOException e1) {
			e1.printStackTrace();
			throw new Remote440Exception(e1.getMessage());
		}	
	}
	
	public static String[] List(String address) throws Remote440Exception {
		
		try {

			String names = null;
			String[] ipPort = Communicator.addressToIPPort(address);
			
			Message newmsg = new Message(null, MessageType.LIST);
			Message recvdObj;
			System.out.println("Naming: sending object to "+ ipPort[0] + ":"+ ipPort[1] + " " + newmsg);	
			recvdObj = Communicator.sendAndReceiveMessage(ipPort[0], Integer.parseInt(ipPort[1]), newmsg);
			if(recvdObj.type != MessageType.LIST ){
				throw new Remote440Exception("Unknown Error");
			}
			
			names = recvdObj.content;
			return names.split(" ");	
			
		} catch (ClassNotFoundException | InterruptedException | IOException e) {
			throw new Remote440Exception(e.getMessage());
		}
					
	}	
}
