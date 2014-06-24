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

// @referred http://docs.oracle.com/javase/8/docs/platform/rmi/spec/rmi-client2a.html
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

		String[] services;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				
		
		//take user input and take necessary action
		 String userInput;
		 String choice;
		 @SuppressWarnings("unused")
		CalciInterface lookupResult;
         while (true) {
         	try{
         		log("MENU \n 1. List all bindnames registered with registry server "
         				+ "\n 2. Lookup remote object by bindname "
         				+ "\n 3. Run standard test cases (Run after sample RMI tests on Server option 3) ");
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
         			// you can implement operations on object here
         			
         			break;
         		
         		case "4": //display
         			int iterCount = 0;
         			if(clientMap.keySet().size()==0)
         				log("No objects to display");
         			else
         			for(String s: clientMap.keySet()){
         				log("Remote Object No. " + iterCount++  +": " + getObjectName((Remote440)clientMap.get(s)));
         			}
         			break;
         		
         		case "5"://delete using bindname
         			log("Enter bindname to be deleted: ");
         			userInput = br.readLine();
         			int r = deleteFromMap(userInput);
         			if(r==0)
         				log(userInput + " deleted");
         			else
         				log("Error while deleting. Operation not complete. ");
         			break;
         		case "3": //default tests
         			services = Naming.List(registryIp+ ":"+registryPort);
         			log("NAMING.LIST: Registry Server has following objects: "+ Arrays.toString(services));
         			if(services.length>0){
         				log("Length of array: "+ services.length);
         				log("Using the first remote object from this list.");
             			
             			CalciInterface calci = (CalciInterface)Naming.lookup(registryIp+":"+registryPort+"/"+services[0]);
             			
             			
             			//System.out.println(calci.add(120, 46));
             			
             			log("Current remote int value: "+ calci.addMemory(0));
             			calci.setMemory(calci.addMemory(5));
             			log("after adding 5: "+ calci.addMemory(0));
             			
             			//System.out.println(calci.addMemory(6));
             			
             			try{
             				log("Receiving bogus reference to remote object (Calci999) from server using already received remote object!");
             				@SuppressWarnings("unused")
							CalciInterface newInterfaceInvalid = calci.getNewCalci("Calci999");
             			} catch (Remote440Exception e){
             				log(e.getMessage());
             			}

             			log("Receiving remote object from server (new bindName: Calci4) using already received remote object!..");
             			CalciInterface newInterface = calci.getNewCalci("Calci4");

             			log("New remote object received. Using this object to add integers: " + newInterface.add(879,7));
             			
             			newInterface.setMemory(newInterface.addMemory(4));
             			log("Adding 4 to new remote object variable: "+newInterface.addMemory(0));
             			log("Converting 'darth - vader' to uppercase using newly received object: "+ newInterface.getUpperCaseString("darth - vader"));	
         			}else{
         				log("Since registry has no objects, thus doing nothing..");
         			}
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
	
	private static String getObjectName(Remote440 obj){
		return ((RemoteStub)obj).getRor().getBindName();
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

