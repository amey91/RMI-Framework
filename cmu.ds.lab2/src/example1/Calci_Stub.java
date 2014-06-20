package example1;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

import core.*;

public class Calci_Stub extends RemoteStub implements CalciInterface,Remote440 {
	
	  private static Method $method_add_0;
	  private static Method $method_setMemory_0;
	  private static Method $method_addMemory_0;
	static
	  {
	    try
	    {
	    	$method_add_0 = CalciInterface.class.getMethod("add", new Class[]{Integer.TYPE,Integer.TYPE});
	    	$method_setMemory_0 = CalciInterface.class.getMethod("setMemory", new Class[]{Integer.TYPE});
	    	$method_addMemory_0 = CalciInterface.class.getMethod("addMemory", new Class[]{Integer.TYPE});
		}
	    catch (NoSuchMethodException localNoSuchMethodException)
	    {
	      throw new NoSuchMethodError("stub class initialization failed");
	    }
	  }
	public Calci_Stub(RemoteObjectReference ror) {
		super(ror);
	}

	private static final long serialVersionUID = -8687192894869080070L;

	@SuppressWarnings("finally")
	@Override
	public int add(int a, int b) throws Remote440Exception {
		int i= (Integer) null;
		try{
			i = (int)invoke("add", new Object[]{new Integer(a), new Integer(b)}, new Class[]{Integer.TYPE,Integer.TYPE});
			return i;
		} catch(Exception e){
			System.out.println("Remote Exception occured while contacting client.");
			throw new Remote440Exception(e.getMessage());
		}
		
		
	}

	@Override
	public void setMemory(int b) throws Remote440Exception {
		try{
			invoke("setMemory", new Object[]{new Integer(b)},new Class[]{Integer.TYPE});
		}
		catch (Exception e){
			throw new Remote440Exception(e.getMessage());
		}
	}

	@Override
	public int addMemory(int b) throws Remote440Exception {
		try{
			int i = (int)invoke("addMemory", new Object[]{new Integer(b)}, new Class[]{Integer.TYPE});
			return i;
		}catch(Exception e){
			throw new Remote440Exception(e.getMessage());
		}
		
	}
	
}
