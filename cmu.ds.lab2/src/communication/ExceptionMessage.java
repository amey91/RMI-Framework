package communication;

import core.RemoteObjectReference;

public class ExceptionMessage extends Message{
	private static final long serialVersionUID = 4979239989099658604L;
	
	
	public ExceptionMessage(String exceptionMsg) {
		super(exceptionMsg);
	}
	
	
}
