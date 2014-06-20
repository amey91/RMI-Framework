package communication;

import core.RemoteObjectReference;

public class ReturnMessage extends Message{

	private static final long serialVersionUID = -267119279323576131L;
	
	Object result;
	
	public ReturnMessage(Object r) {
		result = r;
	}

	
}
