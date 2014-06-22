package client;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import registry.RegistryServer;
import server.Server;
import core.Naming;
import core.Remote440Exception;
import example1.CalciInterface;

public class Client {
	//this is to ensure that each remote object one and only one stub
	//it will map bindname to client stub objects
	private static ConcurrentHashMap<String, Object> remoteObjMap 
				= new ConcurrentHashMap<String, Object>();
	
	
	public static void main(String args[]) throws Remote440Exception{
		
		String[] services = Naming.List("0.0.0.0:"+RegistryServer.INITIAL_REGISTRY_PORT);
		System.out.println("Registry Server has following objects: "+ Arrays.toString(services));
		System.out.println("Using the first remote object from this list.");
		// TODO change localhost
		CalciInterface calci = (CalciInterface)Naming.lookup("127.0.0.1:"+RegistryServer.INITIAL_REGISTRY_PORT+"/"+services[0]);
		System.out.println("ADD calling");
		//System.out.println(calci.add(120, 46));
		
		System.out.println("Current remote int value: "+ calci.addMemory(0));
		calci.setMemory(calci.addMemory(5));
		System.out.println("after adding 5: "+ calci.addMemory(0));
		//System.out.println(calci.addMemory(6));
		
		System.out.println("Creating new remote object from server (new bindName: Calci9) using already received remote object!..");
		CalciInterface newInterface = calci.getNewCalci("Calci9");

		System.out.println("New remote object received. Using this object to add integers: " + newInterface.add(879,7));
		
		newInterface.setMemory(newInterface.addMemory(4));
		System.out.println("Adding 4 to new remote object variable: "+newInterface.addMemory(0));
		
	}
	
	// UI for lookup, list and input for searching bindnames
	
	
	
}
