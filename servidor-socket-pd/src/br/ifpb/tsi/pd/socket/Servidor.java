package br.ifpb.tsi.pd.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	private static String leitura = null;
	private static String escrita = null;
	private static int count = 0;

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(4100);
			
			while(true) {
				System.out.println("Aguardando nova conexão");
				Socket socket = ss.accept();
				ServidorThread st = new ServidorThread(socket);
				count++;
				st.setName("Cliente"+count);
				st.start();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
