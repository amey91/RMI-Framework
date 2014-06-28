package communication;

public class ReturnMessage extends Message{

	private static final long serialVersionUID = -267119279323576131L;
	
	public Object result;
	public boolean converted;
	
	public ReturnMessage(Object r, boolean converted) {
		result = r;
		this.converted = converted;
	}

}
