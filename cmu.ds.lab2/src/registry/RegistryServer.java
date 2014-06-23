package registry;


import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import communication.Communicator;
import core.RemoteObjectReference;

public class RegistryServer {
	// hashmap mapping bindname to ROR
	static ConcurrentHashMap<String, RemoteObjectReference> registryMap;
	
	public static int INITIAL_REGISTRY_PORT = 1099;
	
	public static void main(String args[]){
		if(args.length>1){
			log("Usage: java RegistryServer <port>.");
			log("Port is optional. If you specify port here, please specify the same port for client and server.");
			System.exit(0);
		}
		if(args.length ==1){
			RegistryServer.INITIAL_REGISTRY_PORT = Integer.parseInt(args[0]);
		}
		else{
			log("Port for RegistryServer not specified. Starting on default port 1099.");
		}
		
		//start a console for displaying bindNames on user requests
		new Thread(new RegistryDisplayer()).start();
		
		//initialize hashmap
		registryMap = new ConcurrentHashMap<String, RemoteObjectReference>();
		
		Communicator.listenForMessages(RegistryServer.INITIAL_REGISTRY_PORT, RegistryProcessor.class );
	
	}//end of main
	
	// print to console
	public static void log(String a){
		System.out.println(a);
	}
	
	public static void displayRegistryMap(){
		
		for(String k:registryMap.keySet()){
			log("Bindname: "+k + " "+registryMap.get(k));
		}
	}
}

class RegistryDisplayer extends Thread{
	
	@Override
	public void run(){
		Scanner sc = new Scanner(System.in);
		String userInput ="";
		
		while(true){
			System.out.println("At any point, enter '5' to LIST ALL BINDNAMES held by this Registry Server.");
			userInput = sc.next();
			switch(userInput){
			case "5":
        		RegistryServer.displayRegistryMap();
        		break;
        	default:
        		System.out.println("Registry Server only accepts '5' to display bindNames offered.");
			}
		}
		
	}
}
