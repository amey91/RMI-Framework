package test2;
import core.*;
import java.lang.reflect.InvocationTargetException;

public class ZipCodeRList_Stub extends core.RemoteStub implements test2.ZipCodeRList {
	public ZipCodeRList_Stub (RemoteObjectReference ror) {
		super(ror);
	}

	@Override
	public test2.ZipCodeRList add(java.lang.String param0, java.lang.String param1) throws core.Remote440Exception{
		try{
			test2.ZipCodeRList result = (test2.ZipCodeRList)invoke("add", new Object[]{param0, param1}, new Class[]{java.lang.String.class, java.lang.String.class});
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
	public test2.ZipCodeRList next() throws core.Remote440Exception{
		try{
			test2.ZipCodeRList result = (test2.ZipCodeRList)invoke("next", new Object[]{}, new Class[]{});
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
}
