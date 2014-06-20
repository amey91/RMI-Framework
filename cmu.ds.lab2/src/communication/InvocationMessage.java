package communication;

import core.RemoteObjectReference;

public class InvocationMessage extends Message{
	
	String methodName;
	Object[] objectArray;
	Class[] classArray;
	

	public InvocationMessage(RemoteObjectReference r) {
		super(r);
	}

	public InvocationMessage(RemoteObjectReference r, String s, Object[] objArr, Class[] classArr) {
		super(r);
		methodName = s;
		objectArray = objArr ;
		classArray = classArr;
	}
	

	private static final long serialVersionUID = -2017799150612365253L;

}
