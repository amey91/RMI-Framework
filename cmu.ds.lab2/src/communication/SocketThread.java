package communication;

import java.net.Socket;

public abstract class SocketThread extends java.lang.Thread{
	public Socket clientSocket;
	public SocketThread(Socket socket)
	{
		this.clientSocket = socket;
	}
}
