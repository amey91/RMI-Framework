package communication;

public abstract class Message implements java.io.Serializable{
	//message can be of type = "invocation" | "return" | "exception"
	//we implement each of the message types by extending this class
	//auto generated
	public static final long serialVersionUID = -6759911464169257302L;	
	RemoteObjectReference remoteObjectRef;
	
	public Message(RemoteObjectReference r){
		remoteObjectRef = r;
	}

}
