package communication;

import core.RemoteObjectReference;

public class Message implements java.io.Serializable{
	//message can be of type = "invocation" | "return" | "exception"
	//we implement each of the message types by extending this class
	//auto generated
	public static final long serialVersionUID = -6759911464169257302L;	
	
	public RemoteObjectReference remoteObjectRef;
	
	// function type defined in enum MessageType 
	public MessageType type; 
	
	// general purpose string. Function depends on implementation
	public String comments;
	
	
	public Message(RemoteObjectReference r, MessageType t, String bindName){
		remoteObjectRef = r;
		type = t;
		comments = bindName;
	}
	
	public Message(RemoteObjectReference r){
		remoteObjectRef = r;
		type = MessageType.NONE ;
		comments = "";
	}
	
	public Message(RemoteObjectReference r, MessageType t){
		remoteObjectRef = r;
		type = t;
		comments = "";
	}

	public Message(){
		remoteObjectRef = null;
		comments = "";
	}
	
	public Message(String exceptionMsg){
		comments = exceptionMsg;
	}
}

