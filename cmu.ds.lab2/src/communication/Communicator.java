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
import registry.RegistryProcessor;
import server.ServerProcessor;


public class Communicator {
	
	// accept created socket and do not close it after use
	public static void sendMessage(Socket sendingSocket, Message m) throws InterruptedException, IOException {
		if(sendingSocket == null)
			throw new IOException("Communicator received invalid socket");
		ObjectOutputStream os = new ObjectOutputStream(sendingSocket.getOutputStream());
		os.writeObject(m);
		Thread.sleep(200);
	}

	// create the socket, send message and then close the socket
	public static void sendMessage(String hostName, int port, Message m) throws InterruptedException, UnknownHostException, IOException {
		Socket sendingSocket = new Socket(InetAddress.getByName(hostName),port);
		sendMessage(sendingSocket, m);
		//close sending socket
		sendingSocket.close();
	}
	
	// take socket, receive message and do not close socket
	public static Message receiveMessage(Socket receivingSocket) throws InterruptedException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(receivingSocket.getInputStream());
		Object newObj = (Object)is.readObject();
		
		if (newObj == null){
            throw new IOException("Received a null message");
        }
		return (Message)newObj;
	}
	
	// create socket, accept message and close the socket
	public static Message sendAndReceiveMessage(String hostName, int port, Message inputMessage) throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException {
		Socket socket = new Socket(hostName,port);
		Communicator.sendMessage(socket, inputMessage);
		Message newObj = (Message)Communicator.receiveMessage(socket);
		socket.close();
		return newObj;
	}
	
	// create server socket, keep listening for requests, create thread for handling message
	@SuppressWarnings("resource")
	public static void listenForMessages(int port,Class<?> T)  {
		//initialize listening socket
		ServerSocket listeningSocket = null;
		
		Constructor<?> constructorNew = null;
		try {
			constructorNew = T.getConstructor(Socket.class);

			listeningSocket = new ServerSocket(port);
			System.out.println(T.getName()+ " listening on "+ listeningSocket.getInetAddress().getHostAddress()+ ":"+ listeningSocket.getLocalPort());
			

		} catch (NoSuchMethodException | SecurityException | IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		while(true){
			// setup the heartbeat socket for the server that listens to clients 
			try {
				System.out.println(T.getName() + " waiting for new message...");
				Socket clientSocket = listeningSocket.accept();
				SocketThread instance = (SocketThread)constructorNew.newInstance((Object)clientSocket);
				new Thread(instance).start();
				
			} catch (IOException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println("Error while opening port at registry server");				
				e.printStackTrace();
			}
		}//end of true
		
	}
		
	
}
