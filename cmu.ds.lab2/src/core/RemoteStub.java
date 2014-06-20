package core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import communication.InvocationMessage;

public class RemoteStub {
	RemoteObjectReference ror;
	
	public RemoteStub(RemoteObjectReference ror){
		this.ror = ror;
	}
	
	public Object invoke(String methodName, Object[] objects, Class[] classes) throws UnknownHostException, IOException, ClassNotFoundException{
		// open port and send
		InvocationMessage invMsg = new InvocationMessage(this.ror, methodName, objects, classes);
	
		Socket newSocket = new Socket(InetAddress.getByName(this.ror.serverIP),this.ror.serverPort);
		ObjectInputStream inobj = new ObjectInputStream(newSocket.getInputStream());
		ObjectOutputStream outObj = new ObjectOutputStream(newSocket.getOutputStream());
		
		//first send method name, then param objects, then class types
		outObj.writeObject(invMsg);
		Object in = (Object)inobj.readObject(); 
		
		// TODO if the received object is remote object, then return a stub object
		
		//close socket
		inobj.close();
		outObj.close();
		newSocket.close();
		return in;
	}
}


