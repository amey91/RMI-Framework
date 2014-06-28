package communication;

import core.RemoteObjectReference;

public class InvocationMessage extends Message{
	
	public String methodName;
	public Object[] objectArray;
	public Class<?>[] classArray;
	public boolean[] converted; // set to true if corresponding param has been converted to RoR from stub

	public InvocationMessage(RemoteObjectReference r, String s, Object[] objArr, Class<?>[] classArr, boolean[] converted) {
		super(r);
		methodName = s;
		objectArray = objArr ;
		classArray = classArr;
		type = MessageType.NONE;
		this.converted = converted;
	}	

	private static final long serialVersionUID = -2017799150612365253L;

}
