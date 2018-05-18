package br.ifpb.tsi.pd.socket;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.Scanner;

public class ServidorThread extends Thread{
	private Socket socket = null;
	private String leitura = null;
	private String escrita = null;

	
	public ServidorThread(Socket socket) {
		this.socket = socket;
	}

	
	public void run() {
		System.out.println(this.getName()+" Conectado com: "+socket.getInetAddress()+":"+socket.getPort());
		
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
				leitura = input.readUTF();
				String recurso = null;
				String diretorio = "/home/wanderson/Downloads/servidor-socket-pd/src/br/ifpb/tsi/pd/socket/";
				if(!leitura.isEmpty()) {
				  recurso = leitura;
				  File file = new File(diretorio,recurso);
					if(file.exists()) {
			    	    //System.out.println("Status 200 \n para o recurso "+recurso);
						String texto = new Scanner(new File(diretorio,recurso), "UTF-8").useDelimiter("\\A").next();
			    	    escrita = texto;
			    	    out.writeUTF("Status 200 \n Conteúdo do recurso solicitado\n"+escrita);
					 }else {
						 escrita = "Status 404";
							out.writeUTF(escrita);
					 }
				}else {
					escrita = "Status 404";
					out.writeUTF(escrita);
				
			}
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
