package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import registry.RegistryServer;
import server.Server;
import core.Naming;
import core.Remote440;
import core.Remote440Exception;
import example1.CalciInterface;

public class Client {
	//this is to ensure that each remote object one and only one stub
	//it will map bindname to client stub objects
	private static ConcurrentHashMap<String, Remote440> clientMap 
				= new ConcurrentHashMap<String, Remote440>();
	
	
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
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//take user input and take necessary action
		 String userInput;
         while (true) {
         	try{
         		log("Enter \n 1. Display received remote objects  "
         				+ "\n 2. Lookup for remote objects "
         				+ "\n 3. Delete received remote objects");
         		userInput = br.readLine();
         		if(userInput=="" || userInput==null){
         			throw new Exception("Blank input not allowed.");
         		}
         		
         		switch(userInput){
         		case "1": //display
         			
         			break;
         		case "2": //lookup
         			break;
         		case "3": //delete using bindname
         			break;
         		default: 
         			log("Wrong entry found.");
         			break;
         		}
         		
         	}catch(Exception e){
         		log(e.getMessage());
         	}
         	
         }
		
	}
	
	// UI for lookup, list and input for searching bindnames
	
	private static void log(String a){
		System.out.println(a);
	}
	
	
}
