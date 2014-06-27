package example1;
import core.*;
import java.lang.reflect.InvocationTargetException;

public class CalciInterface_Stub extends core.RemoteStub implements example1.CalciInterface {
	public CalciInterface_Stub (RemoteObjectReference ror) {
		super(ror);
	}

	@Override
	public int add(int param0, int param1) throws core.Remote440Exception{
		try{
			int result = (int)invoke("add", new Object[]{new Integer(param0), new Integer(param1)}, new Class[]{Integer.TYPE, Integer.TYPE});
			return result;
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}

	@Override
	public void setMemory(int param0) throws core.Remote440Exception{
		try{
			invoke("setMemory", new Object[]{new Integer(param0)}, new Class[]{Integer.TYPE});
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}

	@Override
	public java.lang.String getUpperCaseString(java.lang.String param0) throws core.Remote440Exception{
		try{
			java.lang.String result = (java.lang.String)invoke("getUpperCaseString", new Object[]{param0}, new Class[]{java.lang.String.class});
			return result;
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}

	@Override
	public int addMemory(int param0) throws core.Remote440Exception{
		try{
			int result = (int)invoke("addMemory", new Object[]{new Integer(param0)}, new Class[]{Integer.TYPE});
			return result;
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}

	@Override
	public example1.CalciInterface getNewCalci(java.lang.String param0) throws core.Remote440Exception, java.lang.NullPointerException{
		try{
			example1.CalciInterface result = (example1.CalciInterface)invoke("getNewCalci", new Object[]{param0}, new Class[]{java.lang.String.class});
			return result;
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			if(targetException instanceof java.lang.NullPointerException)
				throw (java.lang.NullPointerException) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}
}
