package core;

public class RemoteObjectReference implements java.io.Serializable{
	/**
	 * autogenerated UID
	 */
	private static final long serialVersionUID = 4532216813002720501L;
	
	String serverIP;
	int serverPort;
	String bindname; // this will be matched with "HostMachineIP:Port/bindname"
	String interfaceImplemented;
	
	public RemoteObjectReference(){
		
	}
	
	public RemoteObjectReference(String s, int p, String name, String interfaceImpl){
		this.serverIP = s;
		this.serverPort = p;
		this.bindname = name;
		this.interfaceImplemented = interfaceImpl;
	}
	
}
