package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import core.Naming;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteStub;
import example1.CalciInterface;

// @referred http://docs.oracle.com/javase/8/docs/platform/rmi/spec/rmi-client2a.html
public class Client {
	private static String registryIp;
	private static int registryPort;
	
	public static void main(String args[]) throws Remote440Exception{
		
		if(args.length != 2){
			
			//deployed using eclipse
			log("Usage: java Client <registry_ip> <registry_port>");
			log("No worries, using localhost and dafault registry IP 1099. We assume that");
			log("Localhost might NOT WORK on AFS but localhost works on eclipse.");
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
		Remote440 lookupResult;
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
         			String servicesList = Naming.List(registryIp+ ":"+registryPort);
         			if(servicesList == "" || servicesList.length() == 0){
         				log("Registry Server does not have any objects. Try running client-option-3 after runing server-option-3...");
         				break;
         			}
         			log("\nRegistry Server has following object(s): "+ servicesList);
         			break;
         		
         		case "2": //lookup
         			log("Enter bindname to be looked up: ");
         			userInput = br.readLine();
         			lookupResult  = (Remote440)Naming.lookup(registryIp+":"+registryPort+"/"+userInput);
         			log("Object received successfully");
         			break;
         		case "3": //default tests
         			servicesList = Naming.List(registryIp+ ":"+registryPort);
         			services = servicesList.split(" ");
         			log("NAMING.LIST: Registry Server has following objects: "+ Arrays.toString(services));
         			if(services.length>0){
         				log("Using Calci2 for RMI");
             			
             			CalciInterface calci = (CalciInterface)Naming.lookup(registryIp+":"+registryPort+"/Calci2");
             			log("received: "+ ((RemoteStub)calci).getRor().getBindName());
             			log("Object received successfully");

             			
             			//System.out.println(calci.add(120, 46));
             			
             			log("Current remote int value: "+ calci.addMemory(0));
             			calci.setMemory(calci.addMemory(5));
             			log("after adding 5: "+ calci.addMemory(0));
             			
             			//System.out.println(calci.addMemory(6));
             			
             			try{
             				log("Receiving BOGUS reference to remote object (Calci999) from server using already received remote object!");
             				@SuppressWarnings("unused")
							CalciInterface newInterfaceInvalid = calci.getNewCalci("Calci999");             				
             			} catch (Remote440Exception e){
             				log("Error: "+(e.getMessage()));
             			}

             			log("Receiving remote object from server (new bindName: Calci4) using already received remote object!..");
             			CalciInterface newInterface = calci.getNewCalci("Calci4");
             			
             			log("Object received successfully");
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
	


}

