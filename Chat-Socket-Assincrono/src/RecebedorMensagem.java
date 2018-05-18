import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;



public class RecebedorMensagem extends Thread{
	
	private Socket clientSocket;
	private String msg = null;

	public RecebedorMensagem(Socket clientSocket) {
		super();
		this.clientSocket = clientSocket;
	}
	
	
	@Override
	public void run() {
		try {
			
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String msg = in.readUTF();
			
			while(!msg.equalsIgnoreCase("fim") || !clientSocket.isClosed()){
				System.out.println(msg);
				msg = in.readUTF();
			}
			clientSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}