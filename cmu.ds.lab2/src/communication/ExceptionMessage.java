package communication;

import core.RemoteObjectReference;

public class ExceptionMessage extends Message{
	private static final long serialVersionUID = 4979239989099658604L;
	
	
	public ExceptionMessage(String exceptionMsg) {
		super(exceptionMsg);
	}
	
	
}


/* TODO make exception abstract by replacing everything with REemoteException
 * 
 * package communication;
 

import core.Remote440Exception;

public class ExceptionMessage extends Message{
	private static final long serialVersionUID = 4979239989099658604L;
	
	private Remote440Exception e;
	
	public ExceptionMessage(Exception e) {
		this.e = new Remote440Exception(e.getMessage()); 
	}
	
	
}
*/