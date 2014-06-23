package communication;

import core.RemoteObjectReference;

public class InvocationMessage extends Message{
	
	public String methodName;
	public Object[] objectArray;
	public Class<?>[] classArray;
	

	public InvocationMessage(RemoteObjectReference r) {
		super(r);
		type = MessageType.NONE;
	}

	public InvocationMessage(RemoteObjectReference r, String s, Object[] objArr, Class<?>[] classArr) {
		super(r);
		methodName = s;
		objectArray = objArr ;
		classArray = classArr;
		type = MessageType.NONE;
	}	

	private static final long serialVersionUID = -2017799150612365253L;

}
