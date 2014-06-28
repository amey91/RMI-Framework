package test3;
import core.*;
import java.lang.reflect.InvocationTargetException;

public class NameServer_Stub extends core.RemoteStub implements test3.NameServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6120181642555005224L;

	public NameServer_Stub (RemoteObjectReference ror) {
		super(ror);
	}

	@Override
	public test3.NameServer add(java.lang.String param0, core.RemoteObjectReference param1, test3.NameServer param2) throws core.Remote440Exception{
		try{
			test3.NameServer result = (test3.NameServer)invoke("add", new Object[]{param0, param1, param2}, new Class[]{java.lang.String.class, core.RemoteObjectReference.class, test3.NameServer.class});
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
	public test3.NameServer next() throws core.Remote440Exception{
		try{
			test3.NameServer result = (test3.NameServer)invoke("next", new Object[]{}, new Class[]{});
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
	public core.RemoteObjectReference match(java.lang.String param0) throws core.Remote440Exception{
		try{
			core.RemoteObjectReference result = (core.RemoteObjectReference)invoke("match", new Object[]{param0}, new Class[]{java.lang.String.class});
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
