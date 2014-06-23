package example1;

import core.*;

public class Calci_Stub extends RemoteStub implements CalciInterface,Remote440 {
	
	public Calci_Stub(RemoteObjectReference ror) {
		super(ror);
	}

	private static final long serialVersionUID = -8687192894869080070L;

	@Override
	public int add(int a, int b) throws Remote440Exception {
		
		try{
			int i = (int)invoke("add", new Object[]{new Integer(a), new Integer(b)}, new Class[]{Integer.TYPE,Integer.TYPE});
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

	@Override
	public CalciInterface getNewCalci(String bindName) throws Remote440Exception {
		try{
			return	(CalciInterface) invoke("getNewCalci", new Object[]{new String(bindName)},new Class[]{String.class});
		}
		catch (Exception e){
			throw new Remote440Exception(e.getMessage());
		}
		
	}

	@Override
	public String getUpperCaseString(String simpleString) throws Remote440Exception {
		
		try{
			return (String)invoke("getUpperCaseString", new Object[]{new String(simpleString)}, new Class[] {String.class});		
		}
		catch (Exception e){
			throw new Remote440Exception("Could not conver tString.");
		}
	}

}
