package core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import communication.Communicator;
import communication.Message;
import communication.MessageType;
import core.Remote440Exception;



public class Naming {
	public static Remote440 RorToStub(RemoteObjectReference ror) throws Remote440Exception
	{
		Class<?> stubClass = null;
		Constructor<?> constructorNew = null;
		Remote440 instance = null;
		String stubName = ror.interfaceImplemented + "_Stub";
		// instantiate stub class by name 
		try{
			stubClass = Class.forName(stubName);
		} catch(ClassNotFoundException e){
			// TODO get class by HTTP
			throw new Remote440Exception("Ror to stub failed");
		}
		try {
			constructorNew = stubClass.getConstructor(RemoteObjectReference.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new Remote440Exception("Ror to stub failed");
		}
		
		try {
			instance = (Remote440)constructorNew.newInstance((Object)ror);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new Remote440Exception("Ror to stub failed");
		}
		return instance;
	}
	public static Remote440 lookup(String address) throws Remote440Exception {
		if(address == null || address == ""){
			throw new Remote440Exception("Invalid Address");
		}
		
		if(address.indexOf(":") == -1 || address.indexOf("/")==-1){
			System.out.println("Lookup Usage: RegistryIP:RegistryPort/bindname");
			new Remote440Exception("Invalid Address");
		}
		String[] raw = address.split("/");
		String bindName = raw[1];
		String[] raw2 = raw[0].split(":");
		String ip = raw2[0];
		int port = Integer.parseInt(raw2[1]);
		

		Message newmsg = new Message(null, MessageType.LOOKUP, bindName);
		Message recvdObj;
		try {
			recvdObj = Communicator.sendAndReceiveMessage(ip.toString(),port, newmsg);
		} catch (ClassNotFoundException | InterruptedException | IOException e1) {
			e1.printStackTrace();
			throw new Remote440Exception(e1.getMessage());
		}
		
		
		if(recvdObj.type != MessageType.LOOKUP){
			System.out.println("Received object not LOOKUP");
			return null;
		}
		
		RemoteObjectReference ror = recvdObj.remoteObjectRef;
		
		
		return RorToStub(ror);
	}
	
	public static String[] List(String address) throws Remote440Exception{
		if(address == null || address == ""){
			System.out.println("Address blank. Exiting List method.");
			return  new String[0];
		}
		
		if(address.indexOf(":") == -1 || address.indexOf("/")!=-1){
			System.out.println("List Usage: RegistryIP:RegistryPort");
			return  new String[0];
		}
		
		String[] raw2 = address.split(":");
		String ip = raw2[0];
		int port = Integer.parseInt(raw2[1]);
		

		Message newmsg = new Message(null, MessageType.LIST);
		Message recvdObj;
		String names = null;
		try {
			System.out.println("Naming: sending object to "+ip + ":"+ port + " " + newmsg);
			recvdObj = Communicator.sendAndReceiveMessage(ip,port, newmsg);
			if(recvdObj.type != MessageType.LIST ){
				System.out.println("Received object not LIST");
				return new String[0];
			}
			
			names = recvdObj.comments;
		} catch (ClassNotFoundException | InterruptedException | IOException e) {
			throw new Remote440Exception(e.getMessage());
		}
		
		
		return names.split(" ");				
	}	
}
