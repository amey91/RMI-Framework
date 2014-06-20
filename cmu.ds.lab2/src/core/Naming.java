package core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;



public class Naming {
	public static Remote440 lookup(String address) {
		if(address == null || address == ""){
			// TODO
		}
		
		if(address.indexOf(":") == -1 || address.indexOf("/")==-1){
			System.out.println("Lookuop Usage: RegistryIP:RegistryPort/bindname");
			return null;
		}
		
		// TODO get from server
		RemoteObjectReference ror = null;
		
		Class<?> stubClass = null;
		Constructor<?> constructorNew = null;
		Remote440 instance = null;
		String stubName = ror.interfaceImplemented + "_stub";
		// instantiate stub class by name 
		try{
			stubClass = Class.forName(stubName);
		} catch(ClassNotFoundException e){
			// TODO get class by HTTP
			e.printStackTrace();
		}
		try {
			constructorNew = stubClass.getConstructor(RemoteObjectReference.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			instance = (Remote440)constructorNew.newInstance((Object)ror);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}
	
	public static RemoteObjectReference[] List(String address){
		
		RemoteObjectReference rors[] = null;
		// Get rors from the registry
		return rors;
		
	}
}
