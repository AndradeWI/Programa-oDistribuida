
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;


public class Servidor {

	public static void main(String[] args) {

		try {
		
			ServerSocket serverSocket = new ServerSocket(8000);
			
			System.out.println("Aguardando conexão... ");
			Socket socketClient = serverSocket.accept();
			
			
			RecebedorMensagem in = new RecebedorMensagem(socketClient);
			in.start();
			
			DataOutputStream out = new DataOutputStream(socketClient.getOutputStream());
			String mensagem = JOptionPane.showInputDialog("Mensagem:");
			
			while(!socketClient.isClosed() || !mensagem.equalsIgnoreCase("fim") ){
				
				out.writeUTF(socketClient.getLocalAddress() + ":" + socketClient.getPort() + " - " + mensagem);
				
				mensagem = JOptionPane.showInputDialog("Mensagem Servidor:");

			}
			
			
			socketClient.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}