package client;

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
		String[] services = Naming.List("127.0.0.1:"+RegistryServer.INITIAL_REGISTRY_PORT);
		
		// TODO change localhost
		CalciInterface calci = (CalciInterface)Naming.lookup("127.0.0.1:"+RegistryServer.INITIAL_REGISTRY_PORT+"/"+services[0]);
		
		System.out.println(calci.add(2, 4));
		
		
	}
	
	// UI for lookup, list and input for searching bindnames
	
	
	
}
