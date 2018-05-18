package br.ifpb.tsi.pd.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Cliente2 {
	private static String leitura = null;
	private static String escrita = null;

	public static void main(String[] args) {
		
		try {
			Socket socket = new Socket("localhost", 4100);
			System.out.println("Cliente se conectou com: "+socket.getInetAddress()+":"+socket.getPort());
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			
				escrita = JOptionPane.showInputDialog("Mensagem de Cliente2 para o servidor: ");
				out.writeUTF(escrita);
				leitura = input.readUTF();
				System.out.println(leitura);
				socket.close();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
