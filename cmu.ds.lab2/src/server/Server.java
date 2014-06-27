package server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

import communication.Communicator;
import core.Remote440;
import core.Remote440Exception;

public class Server {
	
	//overridden by cmd arguments 
	public static final int DEFAULT_SERVER_PORT = 5555;
	
	public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{

		String serverIp;
		String registryIp;
		int registryPort;
		
		if(args.length != 2){
			
			//deployed using eclipse
			log("Usage: java Server <registry_ip> <registry_port>");
			registryIp = "127.0.0.1";
			registryPort = 1099;
			
			ServerSocket ss = new ServerSocket(6666);
			serverIp = ss.getInetAddress().getHostAddress();
			ss.close();
		}else{
			//deployed on unix.andrew
			registryIp = args[0];
			registryPort = Integer.parseInt(args[1]);
			try {
				serverIp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				log("Error while creating remote object");
				e1.printStackTrace();
				return;
			}
		}
		
		RemoteObjectManager remoteObjectManager = new RemoteObjectManager(registryIp, registryPort, serverIp, DEFAULT_SERVER_PORT);
		//start a console for adding/removing entries on user requests
		new Thread(new ServerUpdater(remoteObjectManager)).start();
				
		Communicator.listenForMessages(Server.DEFAULT_SERVER_PORT, remoteObjectManager, ServerProcessor.class);
		
	}//end of main
	
	public static void log(String a){
		System.out.println(a);
	}
}

class ServerUpdater extends Thread{
	
	RemoteObjectManager remoteObjectManager;
	ServerUpdater(RemoteObjectManager remoteObjectManager)
	{
		this.remoteObjectManager = remoteObjectManager;
	}
	@Override
	public void run(){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String userInput ="";
		
		while(true){
			try{
				//Remote440 a11 = new example1.Calci(remoteObjectManager);
				//remoteObjectManager.InsertEntry("example1.Calci", "Calci1", a11, true);
         		log("\n 1. Display all remote objects offered by this server"  
         				+ "\n 2. create/add objects using class and bind name"
         				+ "\n 3. Run sample add/remove RMI tests (to get started) ");
         		userInput = sc.nextLine();
         		if(userInput=="" || userInput==null){
         			throw new Exception("Blank input not allowed.");
         		}
         		int option = Integer.parseInt(userInput);
         		if(option == 1) //display 
         		{
             		/*/TODO make some list to display
             		log("Enter Class Name(example1.Calci): ");
             		
             		String className = sc.nextLine();
             		Class<?> stubClass = Class.forName(className);

             		Constructor<?> constructorNew = stubClass.getConstructor();
             		Remote440 remote = (Remote440)constructorNew.newInstance();
             		
             		log("Enter Interface implemented: ");
             		String interfaceName = sc.nextLine();
             		
             		log("Enter new BindName: ");
             		String bindName = sc.nextLine();
             		
             		remoteObjectManager.InsertEntry(interfaceName, bindName, remote, true);*/
             		
             		String a = remoteObjectManager.displayRoRs();
             		if(a.length() == 0 || a == ""){
             			log("No objects currently at server");
             		}else{
             			log("Bindnames at this server are: " + a);
             		}
             	            		
         		}
         		// TODO delete this option
         		else if(option == 99) //Delete
         		{
         			log("Enter Bind Nameof object to be deleted: ");
             		String bindName = sc.nextLine();
             		remoteObjectManager.RemoveEntry(bindName);
         		}
         		else if(option == 3) // sample cases
         		{
         			Remote440 a1 = new example1.Calci(remoteObjectManager);
         			Remote440 a2 = new example1.Calci(remoteObjectManager);
         			Remote440 a3 = new example1.Calci(remoteObjectManager);
         			Remote440 a4 = new example1.Calci(remoteObjectManager);
         			//Remote440 a5 = new test1.Calci(remoteObjectManager);
         			
         			
         			remoteObjectManager.InsertEntry("example1.CalciInterface", "Calci1", a1, true);
         			remoteObjectManager.InsertEntry("example1.CalciInterface", "Calci2", a2, true);
         			remoteObjectManager.InsertEntry("example1.CalciInterface", "Calci3", a3, true);
         			remoteObjectManager.InsertEntry("example1.CalciInterface", "Calci4", a4, true);
         			
         			
         			remoteObjectManager.RemoveEntry("Calci1");
         		}
         		else if(option == 2) // add object
         		{
     				log("Enter Class Name (example1.Calci OR test1.ZipCodeServerImpl OR test2.ZipCodeRListImpl OR test3.NameServerImpl): ");
         		
             		String className = sc.nextLine();
             		Class<?> stubClass = Class.forName(className);

             		Constructor<?> constructorNew = stubClass.getConstructor();
             		Remote440 remote = (Remote440)constructorNew.newInstance(); 
             		
             		String interfaceName = null;
					// iterate and find subclass of Remote440
					for( Class<?> c : stubClass.getInterfaces()){
						if(Remote440.class.isAssignableFrom(c)){
							interfaceName = c.getName();
							break;
						}
					}
					
					if(interfaceName == null){
						throw new Remote440Exception("Interface cannot be located.");
					}
				
             		
             		log("Interfaced to be used: " + interfaceName);
             		
             		log("Enter new BindName: ");
             		String bindName = sc.nextLine();
             		
             		if(bindName == "" || bindName == null)
             			throw new Exception("Bindname cannot be empty");
             		
             		remoteObjectManager.InsertEntry(interfaceName, bindName, remote, true);
     		}
         		else
         			log("Wrong Entry: " + userInput);
         		
         	}catch(Exception e){
         		log(e.getMessage());
         	}
		}	// end of while
	}

	private static void log(String a){
		System.out.println(a);
	}
}
