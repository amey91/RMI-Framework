package communication;

import core.Remote440Exception;


public class ExceptionMessage extends Message{
	private static final long serialVersionUID = 4979239989099658604L;
	Remote440Exception exception;

	public ExceptionMessage(Remote440Exception e) {
		exception = e;
	}
	
	public ExceptionMessage(String exceptionMessage) {
		exception = new Remote440Exception(exceptionMessage);
	}
	public Remote440Exception getException(){
		return exception;
	}
}