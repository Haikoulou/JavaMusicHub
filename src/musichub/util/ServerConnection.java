package musichub.util;

import java.io.*;
import java.net.*;

public class ServerConnection { //Non-functionnal
	public Object ServerConnection(String ip, int port, String request) {
		try {
			Socket socket = new Socket(ip, port);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			output.writeObject(request);
			Object result = null;
			
			input.close();
			output.close();
			socket.close();
			
			return result;
		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}
}
