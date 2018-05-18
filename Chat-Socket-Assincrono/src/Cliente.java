

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;


public class Cliente {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("localhost", 8000);
			System.out.println("Conectado...");

			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			RecebedorMensagem in = new RecebedorMensagem(s);
			in.start();
			
			do{
				out.writeUTF(
						s.getLocalAddress() + ":" + s.getPort() + " - " + JOptionPane.showInputDialog("Mensagem Cliente:"));
			} while (!s.isClosed() );
			
			s.close();
			System.out.println("Conexão encerrada.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}