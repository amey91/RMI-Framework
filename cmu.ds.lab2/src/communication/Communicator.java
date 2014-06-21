package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Communicator {
	public static int sendMessage(Socket sendingSocket, Message m) throws InterruptedException, IOException {
		ObjectOutputStream os = new ObjectOutputStream(sendingSocket.getOutputStream());
		os.writeObject(m);
		Thread.sleep(200);
		return 0;
	}
	
	public static Message receiveMessage(Socket sendingSocket) throws InterruptedException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(sendingSocket.getInputStream());
		
		Object newObj = (Object)is.readObject();
		if (!(newObj instanceof Message)){
			System.out.println("Received a unidentified/corrupted message");
            return null;
        }
		return (Message)newObj;
	}

	public static int sendMessage(String hostName, int port, Message m) throws InterruptedException, UnknownHostException, IOException {
		Socket sendingSocket = new Socket(InetAddress.getByName(hostName),port);
		int returnVal = sendMessage(sendingSocket, m);

		//close sending socket
		sendingSocket.close();
		return returnVal;
		
		
	}
	
	public static Message sendAndReceiveMessage(String hostName, int port, Message inputMessage) throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException {
		Socket socket = new Socket(InetAddress.getByName(hostName),port);
		int returnVal = sendMessage(socket, inputMessage);
		
		if(returnVal!=0)
			return null;
		Message returnMessage = receiveMessage(socket); 
		//close sending socket
		socket.close();
		return returnMessage;
		
	}
	
	public static Message listenForMessage(int port) throws InterruptedException, IOException, ClassNotFoundException {
		ServerSocket listeningSocket = new ServerSocket(port);
		Socket clientSocket = listeningSocket.accept();
		Message m = receiveMessage(clientSocket);
		
		clientSocket.close();
		listeningSocket.close();
		
		return m;
	}
	
	public static Message listenForMessages(int port,Class<?> T)  {
		//initialize listening socket
		ServerSocket listeningSocket = null;
		
		while(true){
			// setup the heartbeat socket for the server that listens to clients 
			try {
				listeningSocket = new ServerSocket(port);
				System.out.println("RegistryServer waiting for new message...");
				Socket clientSocket = listeningSocket.accept();
				Constructor<?> constructorNew = T.getConstructor(Socket.class);
				
				SocketThread instance = (SocketThread)constructorNew.newInstance((Object)clientSocket);
				new Thread(instance).start();
				
			} catch (IOException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println("Error while opening port at registry server");
				e.printStackTrace();
			} finally {
				try {
					listeningSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
		
	
}
