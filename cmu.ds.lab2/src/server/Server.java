package server;

import java.util.concurrent.ConcurrentHashMap;

public class Server {
	// hashmap for storing String:ActualServerObject
	// it maps bindname to the actual object reference
	static ConcurrentHashMap<String, Object> serverMap = new ConcurrentHashMap<String, Object>();
	
	//overridden by cmd arguments 
	public static int INITIAL_PORT = 5555;
	
	//
	
	
	public static void main(String args[]){
		if(args.length < 2){
			System.out.println();
		}
		
	}
}
