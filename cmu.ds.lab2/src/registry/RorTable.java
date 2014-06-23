package registry;

import java.util.concurrent.ConcurrentHashMap;
import core.RemoteObjectReference;

public class RorTable {
	ConcurrentHashMap<String, RemoteObjectReference> registryMap;
	
	public RorTable(){
		registryMap = new ConcurrentHashMap<String, RemoteObjectReference>();
	}
	
	public void displayRegistryMap(){	
		for(String k:registryMap.keySet()){
			log("Bindname: " + k + " " + registryMap.get(k));
		}
	}
	
	// print to console
	public static void log(String a){
		System.out.println(a);
	}
	
	public boolean containsEntry(String bindName) {
		return registryMap.containsKey(bindName);
	}
	
	public RemoteObjectReference getRor(String bindName) {
		return registryMap.get(bindName);
	}
	
	public RemoteObjectReference removeEntry(String bindName) {
		return registryMap.remove(bindName);
	}
	
	public String createBindNameList() {
		String bindNameList = "";
		for(String s : registryMap.keySet() ){
    		//append bindname to the return string
    		bindNameList = bindNameList +  s + " ";
    	}
		return bindNameList;
	}
	
	public void InsertEntry(String bindName,
			RemoteObjectReference remoteObjectRef) {
		registryMap.put(bindName,remoteObjectRef);
	}
	
}