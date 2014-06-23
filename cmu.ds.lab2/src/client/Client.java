package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import core.Naming;
import core.Remote440Exception;
import core.RemoteStub;
import example1.CalciInterface;

public class Client {
	//this is to ensure that each remote object one and only one stub
	//it will map bindname to client stub objects
	private static ConcurrentHashMap<String, RemoteStub> clientMap 
				= new ConcurrentHashMap<String, RemoteStub>();
	
	private static String registryIp;
	private static int registryPort;
	
	public static void main(String args[]) throws Remote440Exception{
		
		if(args.length != 2){
			
			//deployed using eclipse
			log("Usage: java Client <registry_ip> <registry_port>");
			log("No worries, using localhost and dafault registry IP 1099.");
			registryIp = "127.0.0.1";
			registryPort = 1099;
			
		}else{
			//deployed on unix.andrew
			if(args[0].indexOf("/")!=-1){
				log("please enter IP without ");
			}
			registryIp = args[0];
			registryPort = Integer.parseInt(args[1]);
			
		}
		
		String[] services = Naming.List(registryIp+ ":"+registryPort);
		log("Registry Server has following objects: "+ Arrays.toString(services));
		log("Using the first remote object from this list.");
		
		CalciInterface calci = (CalciInterface)Naming.lookup(registryIp+":"+registryPort+"/"+services[0]);
		
		//System.out.println(calci.add(120, 46));
		
		log("Current remote int value: "+ calci.addMemory(0));
		calci.setMemory(calci.addMemory(5));
		log("after adding 5: "+ calci.addMemory(0));
		
		//System.out.println(calci.addMemory(6));
		
		log("Creating new remote object from server (new bindName: Calci9) using already received remote object!..");
		CalciInterface newInterface = calci.getNewCalci("Calci9");

		log("New remote object received. Using this object to add integers: " + newInterface.add(879,7));
		
		newInterface.setMemory(newInterface.addMemory(4));
		log("Adding 4 to new remote object variable: "+newInterface.addMemory(0));
		
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
         			for(String s: clientMap.keySet()){
         				log(""+clientMap.get(s).getRor());
         			}
         			
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
	
	private void addIntoMap(String bindname , RemoteStub newStub){
		Client.clientMap.put(bindname, newStub);
	}
	
	private void deleteFromMap(String bindname){
		if(Client.clientMap.containsKey(bindname)){
			Client.clientMap.remove(bindname);
		}
	}
}
