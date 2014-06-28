package test1;
import core.*;
import java.lang.reflect.InvocationTargetException;

public class ZipCodeServer_Stub extends core.RemoteStub implements test1.ZipCodeServer {
	public ZipCodeServer_Stub (RemoteObjectReference ror) {
		super(ror);
	}

	@Override
	public java.lang.String find(java.lang.String param0) throws core.Remote440Exception{
		try{
			java.lang.String result = (java.lang.String)invoke("find", new Object[]{param0}, new Class[]{java.lang.String.class});
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
	public void initialise(test1.ZipCodeList param0) throws core.Remote440Exception{
		try{
			invoke("initialise", new Object[]{param0}, new Class[]{test1.ZipCodeList.class});
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}

	@Override
	public test1.ZipCodeList findAll() throws core.Remote440Exception{
		try{
			test1.ZipCodeList result = (test1.ZipCodeList)invoke("findAll", new Object[]{}, new Class[]{});
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
	public void printAll() throws core.Remote440Exception{
		try{
			invoke("printAll", new Object[]{}, new Class[]{});
		} catch(InvocationTargetException e){
			Throwable targetException = e.getTargetException();
			if(targetException instanceof core.Remote440Exception)
				throw (core.Remote440Exception) targetException;
			else
				throw new Remote440Exception("Unknown Exception");
		}
	}
}
