package client;

import java.util.concurrent.ConcurrentHashMap;

public class Client {
	//this is to ensure that each remote object one and only one stub
	//it will map bindname to client stub objects
	static ConcurrentHashMap<String, Object> remoteObjMap 
				= new ConcurrentHashMap<String, Object>();
	
	
	
	
	
}
