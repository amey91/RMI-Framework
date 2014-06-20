package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import communication.Message;
import communication.MessageType;



public class Naming {
	public static Remote440 lookup(String address) throws UnknownHostException, IOException {
		if(address == null || address == ""){
			// TODO
		}
		
		if(address.indexOf(":") == -1 || address.indexOf("/")==-1){
			System.out.println("Lookuop Usage: RegistryIP:RegistryPort/bindname");
			return null;
		}
		String[] raw = address.split("/");
		String bindName = raw[1];
		String[] raw2 = raw[0].split(":");
		String ip = raw2[0];
		int port = Integer.parseInt(raw2[1]);
		
		
		// TODO get from server
		Socket serverS = new Socket(InetAddress.getByName(ip),port);
		ObjectInputStream inobj = new ObjectInputStream(serverS.getInputStream());
		ObjectOutputStream outObj = new ObjectOutputStream(serverS.getOutputStream());
		Message newmsg = new Message(null, MessageType.LOOKUP, bindName);
		Message recvdObj;
		try {
			recvdObj = (Message)inobj.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		if(recvdObj.type != MessageType.REBIND ){
			System.out.println("Received object not REBIND");
			return null;
		}
		
		RemoteObjectReference ror = recvdObj.remoteObjectRef;
		
		Class<?> stubClass = null;
		Constructor<?> constructorNew = null;
		Remote440 instance = null;
		String stubName = ror.interfaceImplemented + "_stub";
		// instantiate stub class by name 
		try{
			stubClass = Class.forName(stubName);
		} catch(ClassNotFoundException e){
			// TODO get class by HTTP
			e.printStackTrace();
		}
		try {
			constructorNew = stubClass.getConstructor(RemoteObjectReference.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		try {
			instance = (Remote440)constructorNew.newInstance((Object)ror);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public static String[] List(String address) throws UnknownHostException, IOException{
		if(address == null || address == ""){
			// TODO
			System.out.println("Address balnk. Exiting List method.");
			return  new String[0];
		}
		
		if(address.indexOf(":") == -1 || address.indexOf("/")==-1){
			System.out.println("Lookuop Usage: RegistryIP:RegistryPort/bindname");
			return  new String[0];
		}
		String[] raw = address.split("/");
		String bindName = raw[1];
		String[] raw2 = raw[0].split(":");
		String ip = raw2[0];
		int port = Integer.parseInt(raw2[1]);
		
		Socket serverS = new Socket(InetAddress.getByName(ip),port);
		ObjectInputStream inobj = new ObjectInputStream(serverS.getInputStream());
		ObjectOutputStream outObj = new ObjectOutputStream(serverS.getOutputStream());
		Message newmsg = new Message(null, MessageType.LOOKUP, bindName);
		Message recvdObj;
		try {
			recvdObj = (Message)inobj.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return  new String[0];
		}
		if(recvdObj.type != MessageType.LIST ){
			System.out.println("Received object not LIST");
			return new String[0];
		}
		
		String names = recvdObj.comments;
		return names.split(" ");				
	}
	
	
}
