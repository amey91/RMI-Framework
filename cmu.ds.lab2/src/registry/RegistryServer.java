package registry;


import java.util.Scanner;

import communication.Communicator;

public class RegistryServer {
	// hashmap mapping bindname to ROR 
	
	public static final int DEFAULT_REGISTRY_PORT = 1099;
	
	public static void main(String args[]){
		if(args.length>1){
			log("Usage: java RegistryServer <port>.");
			log("Port is optional. If you specify port here, please specify the same port for client and server.");
			System.exit(0);
		}
		
		int portNumber = DEFAULT_REGISTRY_PORT;
		
		if(args.length == 1){
			portNumber = Integer.parseInt(args[0]);
		}
		else{
			log("Port for RegistryServer not specified. Starting on default port 1099.");
		}
		RorTable rorTable = new RorTable();
		
		//start a console for displaying bindNames on user requests
		new Thread(new RegistryDisplayer(rorTable)).start();
		
		Communicator.listenForMessages(portNumber, rorTable, RegistryProcessor.class );
	
	}//end of main
	
	// print to console
	public static void log(String a){
		System.out.println(a);
	}
	
	
}

class RegistryDisplayer extends Thread{
	
	RorTable rorTable;
	RegistryDisplayer(RorTable rorTable)
	{
		this.rorTable = rorTable;
	}
	@Override
	public void run(){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String userInput ="";
		
		while(true){
			System.out.println("At any point, enter '5' to LIST ALL BINDNAMES held by this Registry Server.");
			userInput = sc.next();
			switch(userInput){
			case "5":
				rorTable.displayRegistryMap();
        		break;
        	default:
        		System.out.println("Registry Server only accepts '5' to display bindNames offered.");
			}
		}
	}
}
