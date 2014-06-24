package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import core.Naming;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteStub;
import example1.CalciInterface;

public class Client {
	//this is to ensure that each remote object one and only one stub
	//it will map bindname to client stub objects
	private static ConcurrentHashMap<String, Remote440> clientMap 
				= new ConcurrentHashMap<String, Remote440>();
	
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
		
		log("Converting 'darth - vader' to uppercase using newly received object: "+ newInterface.getUpperCaseString("darth - vader"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//take user input and take necessary action
		 String userInput;
		 String choice;
		 CalciInterface lookupResult;
         while (true) {
         	try{
         		log("Enter \n 1. List all bindnames registered with registry server "
         				+ "\n 2. Lookup remote object by bindname "
         				+ "\n 3. Display received remote objects " 
         				+ "\n 4. Delete received remote objects");
         		choice = br.readLine();
         		if(choice=="" || choice==null){
         			throw new Exception("Blank input not allowed.");
         		}
         		
         		switch(choice){
         		
         		case "1": // list bindname in registry server
         			services = Naming.List(registryIp+ ":"+registryPort);
         			log("Registry Server has following objects: "+ Arrays.toString(services));
         			break;
         		
         		case "2": //lookup
         			log("Enter bindname to be looked up: ");
         			userInput = br.readLine();
         			lookupResult  = (CalciInterface)Naming.lookup(registryIp+":"+registryPort+"/"+userInput);
         			// TODO check lookupResult for null and if !=null store it in client map
         			
         			addIntoMap(userInput,lookupResult);
         			break;
         		
         		case "3": //display
         			int iterCount = 0;
         			for(String s: clientMap.keySet()){
         				log("Remote Object No. " + iterCount++  +": " + s);
         			}
         		
         		case "4"://delete using bindname
         			log("Enter bindname to be deleted: ");
         			userInput = br.readLine();
         			int r = deleteFromMap(userInput);
         			if(r==0)
         				log(userInput + " deleted");
         			else
         				log("Error while deleting. Operation not complete. ");
         			break;
         		
         		default: 
         			log("Wrong input received. No action taken.");
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
	
	private static void addIntoMap(String bindname,Remote440 newStub){
		Client.clientMap.put(bindname,newStub);
	}
	
	// @return 0 if deleted
	// @return -1 if error
	private static int deleteFromMap(String bindname){
		if(Client.clientMap.containsKey(bindname)){
			Client.clientMap.remove(bindname);
			return 0;
		}
		else return -1;
	}
}
