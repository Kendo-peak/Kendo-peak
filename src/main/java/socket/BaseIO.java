package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class BaseIO {
	public static void closeIO(Socket socket, InputStream input, OutputStream output) {
		try {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}
}
 